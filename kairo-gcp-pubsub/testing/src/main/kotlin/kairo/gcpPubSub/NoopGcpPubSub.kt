package kairo.gcpPubSub

public class NoopGcpPubSub : GcpPubSub() {
  override suspend fun publish(topic: String, message: GcpPubSubMessage): Nothing {
    throw NotImplementedError("This GCP Pub/Sub is no-op.")
  }

  override fun subscribe(subscription: String, handler: suspend (GcpPubSubMessage) -> Unit): Nothing {
    throw NotImplementedError("This GCP Pub/Sub is no-op.")
  }

  override fun close(): Unit = Unit
}
