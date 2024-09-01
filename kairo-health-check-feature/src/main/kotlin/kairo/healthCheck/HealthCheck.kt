package kairo.healthCheck

public fun interface HealthCheck {
  public suspend fun check(): HealthCheckRep.Status
}
