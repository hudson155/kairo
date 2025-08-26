package kairo.healthCheck

import kotlinx.serialization.Serializable

@Serializable
public data class HealthCheckRep(
  val success: Boolean,
  val checks: Map<String, Boolean>,
)
