package limber.feature.event

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.api.gax.batching.BatchingSettings
import com.google.cloud.pubsub.v1.Publisher
import com.google.inject.Inject
import com.google.protobuf.ByteString
import com.google.pubsub.v1.PubsubMessage
import com.google.pubsub.v1.TopicName
import limber.config.sql.EventConfig
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

public class RealEventPublisher<in T : Any>(
  private val config: EventConfig.Publish,
  private val objectMapper: ObjectMapper,
  private val publisher: Publisher,
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
        return@compute createPublisher<T>(gcpTopicName)
      } as RealEventPublisher<T>
    }

    private fun <T : Any> createPublisher(gcpTopicName: TopicName): RealEventPublisher<T> {
      val publisher = Publisher.newBuilder(gcpTopicName)
        .setBatchingSettings(batchingSettings())
        .build()
      return RealEventPublisher(
        config = checkNotNull(config.publish),
        objectMapper = objectMapper,
        publisher = publisher,
      )
    }

    /**
     * Batching is disabled so that messages are published immediately.
     * This could cause performance concerns down the road once we have high traffic,
     * but should be fine for now.
     */
    private fun batchingSettings(): BatchingSettings =
      BatchingSettings.newBuilder()
        .setIsEnabled(false)
        .build()

    override fun stop() {
      publishers.values.forEach { publisher ->
        publisher.beginStop()
      }
      publishers.values.forEach { publisher ->
        publisher.awaitStop()
      }
    }
  }

  override fun publish(type: EventType, body: T) {
    val data = ByteString.copyFrom(objectMapper.writeValueAsBytes(body))
    val message = PubsubMessage.newBuilder()
      .putAllAttributes(mapOf("type" to type.name))
      .setData(data)
      .build()
    publisher.publish(message).get()
  }

  override fun beginStop() {
    publisher.shutdown()
  }

  override fun awaitStop() {
    publisher.awaitTermination(config.shutdownMs, TimeUnit.MILLISECONDS)
  }
}
