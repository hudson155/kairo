package kairo.gcpPubSub

internal class LocalGcpPubSubTest : AbstractGcpPubSubTest() {
  override val testTopic: String = "test-topic"

  private val localPubSub: LocalGcpPubSub =
    LocalGcpPubSub(topicToSubscriptions = mapOf("test-topic" to listOf("test-sub")))

  override val gcpPubSub: GcpPubSub = localPubSub

  override fun getPublishedMessages(): List<Pair<String, GcpPubSubMessage>> =
    localPubSub.getPublishedMessages()

  override fun resetPubSub() {
    localPubSub.reset()
  }

  override fun subscribeToTestTopic(handler: suspend (GcpPubSubMessage) -> Unit) {
    localPubSub.subscribe("test-sub", handler)
  }
}
