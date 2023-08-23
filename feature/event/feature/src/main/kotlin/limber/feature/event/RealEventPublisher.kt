package limber.feature.event

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.api.gax.batching.BatchingSettings
import com.google.api.gax.retrying.RetrySettings
import com.google.cloud.pubsub.v1.Publisher
import com.google.inject.Inject
import com.google.protobuf.ByteString
import com.google.pubsub.v1.PubsubMessage
import com.google.pubsub.v1.TopicName
import limber.config.sql.EventConfig
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

public class RealEventPublisher<in T : Any>(
  private val objectMapper: ObjectMapper,
  private val publisher: Publisher,
) : EventPublisher<T>() {
  public class Factory @Inject constructor(
    private val config: EventConfig,
    private val objectMapper: ObjectMapper,
  ) : EventPublisher.Factory {
    private val publishers: MutableMap<TopicName, Publisher> = ConcurrentHashMap()

    override fun <T : Any> invoke(topicName: String): EventPublisher<T> {
      val gcpTopicName = TopicName.of(config.projectName, topicName)
      val publisher = publishers.getOrPut(gcpTopicName) {
        return@getOrPut Publisher.newBuilder(gcpTopicName)
          .setBatchingSettings(BatchingSettings.newBuilder().setIsEnabled(false).build())
          .setRetrySettings(RetrySettings.newBuilder().build())
          .build()
      }
      return RealEventPublisher(objectMapper, publisher)
    }

    override fun close() {
      publishers.forEach { (_, publisher) ->
        publisher.shutdown()
      }
      publishers.forEach { (_, publisher) ->
        publisher.awaitTermination(checkNotNull(config.publish).shutdownMs, TimeUnit.MILLISECONDS)
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
}
