package kairo.admin.model

public data class HealthCheckResult(
  val name: String,
  val passed: Boolean,
  val durationMs: Long,
  val error: String?,
)
