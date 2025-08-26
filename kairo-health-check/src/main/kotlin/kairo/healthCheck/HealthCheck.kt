package kairo.healthCheck

/**
 * Each health check is executed as part of the health check readiness endpoint.
 * If the health check returns without throwing an exception, it is considered successful.
 * The health checks are run in parallel.
 */
public fun interface HealthCheck {
  public suspend fun check()
}
