package kairo.healthCheck

public data class HealthCheckRep(
  val success: Boolean,
  val checks: Map<String, Boolean>,
)
