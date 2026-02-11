package kairo.gcpPubSub

/**
 * Lightweight and coroutine-friendly Google Pub/Sub wrapper for Kotlin.
 * Use [DefaultGcpPubSub] in production or [LocalGcpPubSub] for local development.
 */
public abstract class GcpPubSub {
  /**
   * Publishes a message to the given topic.
   * Returns the published message ID.
   */
  public abstract suspend fun publish(topic: String, message: GcpPubSubMessage): String

  /**
   * Subscribes to messages on the given subscription.
   * The [handler] is called for each received message.
   */
  public abstract fun subscribe(subscription: String, handler: suspend (GcpPubSubMessage) -> Unit)

  public abstract fun close()
}
