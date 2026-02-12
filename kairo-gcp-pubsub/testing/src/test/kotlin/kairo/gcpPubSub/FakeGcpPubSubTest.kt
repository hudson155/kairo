package kairo.gcpPubSub

internal class FakeGcpPubSubTest : AbstractGcpPubSubTest() {
  override val testTopic: String = "test-topic"

  private val fakePubSub: FakeGcpPubSub = FakeGcpPubSub()

  override val gcpPubSub: GcpPubSub = fakePubSub

  override fun getPublishedMessages(): List<Pair<String, GcpPubSubMessage>> =
    fakePubSub.getPublishedMessages()

  override fun resetPubSub() {
    fakePubSub.reset()
  }

  override fun subscribeToTestTopic(handler: suspend (GcpPubSubMessage) -> Unit) {
    // FakeGcpPubSub routes by topic name directly.
    fakePubSub.subscribe("test-topic", handler)
  }
}
