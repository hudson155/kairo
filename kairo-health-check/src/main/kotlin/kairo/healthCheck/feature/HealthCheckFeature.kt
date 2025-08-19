package kairo.healthCheck.feature

import io.ktor.server.application.Application
import kairo.feature.Feature
import kairo.rest.RestFeature
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * The Health Check Feature binds 2 routes for health checks.
 * The liveness check always immediately responds with 200 OK.
 * The readiness check responds with 200 OK only after the provided [HealthCheck]s pass.
 * The health checks are run in parallel.
 */
public class HealthCheckFeature(
  healthChecks: Map<String, HealthCheck>,
  timeout: Duration = 2.seconds,
) : Feature(), RestFeature.HasRouting {
  override val name: String = "Health Check"

  private val healthCheckHandler: HealthCheckHandler = HealthCheckHandler(healthChecks, timeout)

  override fun Application.routing() {
    with(healthCheckHandler) { routing() }
  }
}
