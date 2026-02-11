package kairo.gcpPubSub

import com.google.api.core.ApiFutureToListenableFuture
import com.google.cloud.pubsub.v1.MessageReceiver
import com.google.cloud.pubsub.v1.Publisher
import com.google.cloud.pubsub.v1.Subscriber
import com.google.protobuf.ByteString
import com.google.pubsub.v1.ProjectSubscriptionName
import com.google.pubsub.v1.PubsubMessage
import com.google.pubsub.v1.TopicName
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import java.util.concurrent.ConcurrentHashMap
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.runBlocking

private val logger: KLogger = KotlinLogging.logger {}

public class DefaultGcpPubSub(
  private val projectId: String,
) : GcpPubSub() {
  private val publishers: ConcurrentHashMap<String, Publisher> = ConcurrentHashMap()
  private val subscribers: MutableList<Subscriber> = mutableListOf()

  override suspend fun publish(topic: String, message: GcpPubSubMessage): String {
    logger.debug { "Publishing message to topic: $topic." }
    val publisher = publishers.getOrPut(topic) {
      Publisher.newBuilder(TopicName.of(projectId, topic)).build()
    }
    val pubsubMessage = PubsubMessage.newBuilder().apply {
      data = ByteString.copyFromUtf8(message.data)
      putAllAttributes(message.attributes)
      message.orderingKey?.let { orderingKey = it }
    }.build()
    return ApiFutureToListenableFuture(publisher.publish(pubsubMessage)).await()
  }

  override fun subscribe(subscription: String, handler: suspend (GcpPubSubMessage) -> Unit) {
    logger.info { "Subscribing to subscription: $subscription." }
    val receiver = MessageReceiver { message, consumer ->
      try {
        runBlocking {
          handler(
            GcpPubSubMessage(
              data = message.data.toStringUtf8(),
              attributes = message.attributesMap,
              orderingKey = message.orderingKey.takeIf { it.isNotEmpty() },
            ),
          )
        }
        consumer.ack()
      } catch (e: Exception) {
        logger.error(e) { "Error processing message from subscription: $subscription." }
        consumer.nack()
      }
    }
    val subscriber = Subscriber.newBuilder(
      ProjectSubscriptionName.of(projectId, subscription),
      receiver,
    ).build()
    synchronized(subscribers) {
      subscribers.add(subscriber)
    }
    subscriber.startAsync()
  }

  override fun close() {
    publishers.values.forEach { it.shutdown() }
    synchronized(subscribers) {
      subscribers.forEach { it.stopAsync() }
    }
  }
}
