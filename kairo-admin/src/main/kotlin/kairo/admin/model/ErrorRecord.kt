package kairo.admin.model

public data class ErrorRecord(
  val timestamp: String,
  val type: String,
  val message: String,
  val statusCode: Int? = null,
  val stackTrace: String? = null,
)
