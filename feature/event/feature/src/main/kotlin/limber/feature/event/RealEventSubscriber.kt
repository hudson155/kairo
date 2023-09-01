package limber.feature.event

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.api.gax.batching.FlowControlSettings
import com.google.api.gax.rpc.NotFoundException
import com.google.cloud.pubsub.v1.AckReplyConsumer
import com.google.cloud.pubsub.v1.Subscriber
import com.google.cloud.pubsub.v1.SubscriptionAdminClient
import com.google.pubsub.v1.PubsubMessage
import com.google.pubsub.v1.RetryPolicy
import com.google.pubsub.v1.Subscription
import com.google.pubsub.v1.SubscriptionName
import com.google.pubsub.v1.TopicName
import limber.config.event.EventConfig
import mu.KLogger
import mu.KotlinLogging
import org.threeten.bp.Duration
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass
import com.google.protobuf.Duration as ProtobufDuration

public class RealEventSubscriber<in T : Any>(
  private val gcpTopicName: TopicName,
  private val gcpSubscriptionName: SubscriptionName,
  private val kClass: KClass<T>,
  private val config: EventConfig.Subscribe,
  private val objectMapper: ObjectMapper,
  private val handler: EventHandlerFunction<T>,
) : EventSubscriber() {
  public class Factory(
    private val config: EventConfig,
    private val objectMapper: ObjectMapper,
  ) : EventSubscriber.Factory() {
    private val subscribers: MutableMap<SubscriptionName, RealEventSubscriber<*>> = ConcurrentHashMap()

    override fun <T : Any> invoke(
      topicName: String,
      subscriptionName: String,
      kClass: KClass<T>,
      handler: EventHandlerFunction<T>,
    ) {
      val gcpTopicName = TopicName.of(
        config.projectName,
        topicName,
      )
      val gcpSubscriptionName = SubscriptionName.of(
        config.projectName,
        subscriptionDescription(subscriptionName, topicName),
      )
      subscribers.compute(gcpSubscriptionName) { _, value ->
        if (value != null) error("Duplicate subscriber with name $gcpSubscriptionName.")
        return@compute RealEventSubscriber<T>(
          gcpTopicName = gcpTopicName,
          gcpSubscriptionName = gcpSubscriptionName,
          kClass = kClass,
          config = checkNotNull(config.subscribe),
          objectMapper = objectMapper,
          handler = handler,
        )
      }
    }

    override fun start() {
      SubscriptionAdminClient.create().use { subscriptionAdminClient ->
        subscribers.values.forEach { subscriber ->
          subscriber.start(subscriptionAdminClient)
        }
      }
    }

    override fun stop() {
      subscribers.values.forEach { subscriber ->
        subscriber.beginStop()
      }
      subscribers.values.forEach { subscriber ->
        subscriber.awaitStop()
      }
    }
  }

  private val logger: KLogger = KotlinLogging.logger {}

  private val subscriber: Subscriber =
    Subscriber.newBuilder(gcpSubscriptionName.toString(), ::receiver)
      .setFlowControlSettings(
        FlowControlSettings.newBuilder()
          .setMaxOutstandingElementCount(config.maxOutstandingElements)
          .setMaxOutstandingRequestBytes(config.maxOutstandingBytes)
          .build(),
      )
      .setMaxAckExtensionPeriod(Duration.ZERO)
      .build()

  @Suppress("TooGenericExceptionCaught")
  private fun receiver(message: PubsubMessage, consumer: AckReplyConsumer) {
    try {
      val type = EventType.valueOf(message.getAttributesOrThrow("type"))
      val body = objectMapper.readValue(message.data.toByteArray(), kClass.java)
      logger.info { "Handling event from subscription $gcpSubscriptionName. Type is $type. $body." }
      handler(type, body)
      consumer.ack()
    } catch (e: Exception) {
      logger.error(e) { "NACKing event due to exception." }
      consumer.nack()
    }
  }

  /**
   * Subscriptions are created automatically.
   */
  internal fun start(subscriptionAdminClient: SubscriptionAdminClient) {
    val expected = Subscription.newBuilder()
      .setName(gcpSubscriptionName.toString())
      .setTopic(gcpTopicName.toString())
      .setRetryPolicy(
        RetryPolicy.newBuilder()
          .setMinimumBackoff(protobufDuration(ms = config.minimumBackoffMs))
          .setMaximumBackoff(protobufDuration(ms = config.maximumBackoffMs))
          .build(),
      )
      .build()

    val existing = try {
      subscriptionAdminClient.getSubscription(subscriber.subscriptionNameString)
    } catch (_: NotFoundException) {
      null
    }

    if (existing == null) {
      subscriptionAdminClient.createSubscription(expected)
    }
  }

  private fun protobufDuration(ms: Long): com.google.protobuf.Duration =
    ProtobufDuration.newBuilder()
      .setSeconds(ms / 1000)
      .setNanos((ms % 1000).toInt() * 1000 * 1000)
      .build()

  internal fun beginStop() {
    subscriber.stopAsync()
  }

  internal fun awaitStop() {
    subscriber.awaitTerminated(config.shutdownMs, TimeUnit.MILLISECONDS)
  }
}
