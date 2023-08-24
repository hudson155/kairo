package limber.feature.event

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.api.gax.batching.BatchingSettings
import com.google.cloud.pubsub.v1.Publisher
import com.google.cloud.pubsub.v1.TopicAdminClient
import com.google.inject.Inject
import com.google.protobuf.ByteString
import com.google.pubsub.v1.PubsubMessage
import com.google.pubsub.v1.TopicName
import limber.config.event.EventConfig
import mu.KLogger
import mu.KotlinLogging
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

public class RealEventPublisher<in T : Any>(
  private val gcpTopicName: TopicName,
  private val config: EventConfig.Publish,
  private val objectMapper: ObjectMapper,
) : EventPublisher<T>() {
  public class Factory @Inject constructor(
    private val config: EventConfig,
    private val objectMapper: ObjectMapper,
  ) : EventPublisher.Factory() {
    private val publishers: MutableMap<TopicName, RealEventPublisher<*>> = ConcurrentHashMap()

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> invoke(topicName: String): EventPublisher<T> {
      val gcpTopicName = TopicName.of(config.projectName, topicName)
      return publishers.compute(gcpTopicName) { _, value ->
        if (value != null) return@compute value
        return@compute RealEventPublisher<T>(
          gcpTopicName = gcpTopicName,
          config = checkNotNull<EventConfig.Publish>(config.publish),
          objectMapper = objectMapper,
        )
      } as RealEventPublisher<T>
    }

    override fun start() {
      TopicAdminClient.create().use { topicAdminClient ->
        publishers.values.forEach { publisher ->
          publisher.start(topicAdminClient)
        }
      }
    }

    override fun stop() {
      publishers.values.forEach { publisher ->
        publisher.beginStop()
      }
      publishers.values.forEach { publisher ->
        publisher.awaitStop()
      }
    }
  }

  private val logger: KLogger = KotlinLogging.logger {}

  private val publisher: Publisher =
    Publisher.newBuilder(gcpTopicName)
      .setBatchingSettings(
        /**
         * Batching is disabled so that messages are published immediately.
         * This could cause performance concerns down the road once we have high traffic,
         * but should be fine for now.
         */
        BatchingSettings.newBuilder()
          .setIsEnabled(false)
          .build(),
      )
      .build()

  /**
   * Topics aren't created automatically.
   * On start, getting the topic causes an exception to be thrown if the topic does not exist.
   */
  internal fun start(topicAdminClient: TopicAdminClient) {
    topicAdminClient.getTopic(publisher.topicName)
  }

  override fun publish(type: EventType, body: T) {
    val data = ByteString.copyFrom(objectMapper.writeValueAsBytes(body))
    val message = PubsubMessage.newBuilder()
      .putAllAttributes(mapOf("type" to type.name))
      .setData(data)
      .build()
    logger.info { "Publishing (no-op) event to topic $gcpTopicName. Type is $type. $body." }
    publisher.publish(message).get()
  }

  internal fun beginStop() {
    publisher.shutdown()
  }

  internal fun awaitStop() {
    publisher.awaitTermination(config.shutdownMs, TimeUnit.MILLISECONDS)
  }
}
