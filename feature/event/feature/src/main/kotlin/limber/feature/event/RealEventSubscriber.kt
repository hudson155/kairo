package limber.feature.event

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.api.gax.batching.FlowControlSettings
import com.google.cloud.pubsub.v1.AckReplyConsumer
import com.google.cloud.pubsub.v1.Subscriber
import com.google.inject.Inject
import com.google.pubsub.v1.PubsubMessage
import com.google.pubsub.v1.SubscriptionName
import limber.config.event.EventConfig
import mu.KLogger
import mu.KotlinLogging
import org.threeten.bp.Duration
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass

public class RealEventSubscriber<in T : Any>(
  private val gcpSubscriptionName: SubscriptionName,
  private val kClass: KClass<T>,
  private val config: EventConfig.Subscribe,
  private val objectMapper: ObjectMapper,
  private val handler: EventHandlerFunction<T>,
) : EventSubscriber() {
  public class Factory @Inject constructor(
    private val config: EventConfig,
    private val objectMapper: ObjectMapper,
  ) : EventSubscriber.Factory() {
    private val subscribers: MutableMap<SubscriptionName, RealEventSubscriber<*>> = ConcurrentHashMap()

    override fun <T : Any> invoke(
      subscriptionName: String,
      kClass: KClass<T>,
      handler: EventHandlerFunction<T>,
    ) {
      val gcpSubscriptionName = SubscriptionName.of(config.projectName, subscriptionName)
      subscribers.compute(gcpSubscriptionName) { _, value ->
        if (value != null) error("Duplicate subscriber with name $gcpSubscriptionName.")
        return@compute RealEventSubscriber<T>(
          gcpSubscriptionName = gcpSubscriptionName,
          kClass = kClass,
          config = checkNotNull(config.subscribe),
          objectMapper = objectMapper,
          handler = handler,
        )
      }
    }

    override fun start(): Unit = Unit

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

  internal fun beginStop() {
    subscriber.stopAsync()
  }

  internal fun awaitStop() {
    subscriber.awaitTerminated(config.shutdownMs, TimeUnit.MILLISECONDS)
  }
}
