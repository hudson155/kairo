package kairo.gcpPubSub

import java.util.concurrent.atomic.AtomicInteger

public class FakeGcpPubSub : GcpPubSub() {
  private val publishedMessages: MutableList<Pair<String, GcpPubSubMessage>> = mutableListOf()
  private val handlers: MutableMap<String, MutableList<suspend (GcpPubSubMessage) -> Unit>> = mutableMapOf()
  private val messageIdCounter: AtomicInteger = AtomicInteger(0)

  override suspend fun publish(topic: String, message: GcpPubSubMessage): String {
    val messageId = messageIdCounter.incrementAndGet().toString()
    publishedMessages.add(topic to message)
    val subHandlers = handlers[topic]?.toList().orEmpty()
    for (handler in subHandlers) {
      handler(message)
    }
    return messageId
  }

  override fun subscribe(subscription: String, handler: suspend (GcpPubSubMessage) -> Unit) {
    handlers.getOrPut(subscription) { mutableListOf() }.add(handler)
  }

  override fun close(): Unit = Unit

  public fun getPublishedMessages(): List<Pair<String, GcpPubSubMessage>> =
    publishedMessages.toList()

  public fun reset() {
    publishedMessages.clear()
    handlers.clear()
    messageIdCounter.set(0)
  }
}
