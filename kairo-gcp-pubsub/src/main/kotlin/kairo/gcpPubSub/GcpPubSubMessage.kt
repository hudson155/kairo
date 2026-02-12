package kairo.gcpPubSub

public data class GcpPubSubMessage(
  val data: String,
  val attributes: Map<String, String> = emptyMap(),
  val orderingKey: String? = null,
)
