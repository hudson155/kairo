package kairo.gcpPubSub

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

private val logger: KLogger = KotlinLogging.logger {}

public class LocalGcpPubSub(
  private val topicToSubscriptions: Map<String, List<String>> = emptyMap(),
) : GcpPubSub() {
  private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
  private val publishedMessages: MutableList<Pair<String, GcpPubSubMessage>> = mutableListOf()
  private val handlers: MutableMap<String, MutableList<suspend (GcpPubSubMessage) -> Unit>> = mutableMapOf()
  private val messageIdCounter: AtomicInteger = AtomicInteger(0)

  override suspend fun publish(topic: String, message: GcpPubSubMessage): String {
    logger.debug { "Publishing message locally to topic: $topic." }
    val messageId = messageIdCounter.incrementAndGet().toString()
    synchronized(publishedMessages) {
      publishedMessages.add(topic to message)
    }
    val subscriptions = topicToSubscriptions[topic].orEmpty()
    for (subscription in subscriptions) {
      val subHandlers = synchronized(handlers) { handlers[subscription]?.toList() } ?: continue
      for (handler in subHandlers) {
        scope.launch {
          try {
            handler(message)
          } catch (e: CancellationException) {
            throw e
          } catch (e: Exception) {
            logger.error(e) { "Error processing local message for subscription: $subscription." }
          }
        }
      }
    }
    return messageId
  }

  override fun subscribe(subscription: String, handler: suspend (GcpPubSubMessage) -> Unit) {
    logger.info { "Subscribing locally to subscription: $subscription." }
    synchronized(handlers) {
      handlers.getOrPut(subscription) { mutableListOf() }.add(handler)
    }
  }

  override fun close() {
    scope.cancel()
  }

  public fun getPublishedMessages(): List<Pair<String, GcpPubSubMessage>> =
    synchronized(publishedMessages) { publishedMessages.toList() }

  public fun reset() {
    synchronized(publishedMessages) { publishedMessages.clear() }
    messageIdCounter.set(0)
  }
}
