package kairo.admin.model

public data class AdminIntegrationInfo(
  val name: String,
  val type: String,
  val status: String,
  val details: Map<String, String> = emptyMap(),
)
