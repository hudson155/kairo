package kairo.healthCheck

import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import kairo.rest.HasRouting
import kairo.rest.route
import kotlin.time.Duration

internal class HealthCheckHandler(
  healthChecks: Map<String, HealthCheck>,
  timeout: Duration,
) : HasRouting {
  private val healthCheckService: HealthCheckService = HealthCheckService(healthChecks, timeout)

  override fun Application.routing() {
    routing {
      route(HealthCheckApi.Liveness::class) {
        handle { healthCheckService.liveness() }
      }

      route(HealthCheckApi.Readiness::class) {
        handle { healthCheckService.readiness() }
      }
    }
  }
}
