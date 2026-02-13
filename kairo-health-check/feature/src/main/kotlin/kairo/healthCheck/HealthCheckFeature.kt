package kairo.healthCheck

import io.ktor.server.application.Application
import kairo.feature.Feature
import kairo.feature.FeaturePriority
import kairo.feature.LifecycleHandler
import kairo.feature.lifecycle
import kairo.rest.HasRouting
import kotlin.concurrent.atomics.AtomicBoolean
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * The Health Check Feature adds 2 routes for health checks.
 * The liveness check always immediately responds with 200 OK.
 * The readiness check responds with 200 OK only after the provided [HealthCheck]s pass.
 * The health checks are run in parallel.
 */
public class HealthCheckFeature(
  healthChecks: Map<String, HealthCheck> = emptyMap(),
  includeDefaultHealthCheck: Boolean = true,
  timeout: Duration = 2.seconds,
) : Feature(), HasRouting {
  override val name: String = "Health Check"

  private val serverIsRunning: AtomicBoolean = AtomicBoolean(false)

  public val registeredHealthChecks: Map<String, HealthCheck> = buildMap {
    putAll(healthChecks)
    if (includeDefaultHealthCheck) {
      put("server", HealthCheck { check(serverIsRunning.load()) { "Server is not running." } })
    }
  }

  private val healthCheckHandler: HealthCheckHandler =
    HealthCheckHandler(
      healthChecks = registeredHealthChecks,
      timeout = timeout,
    )

  override val lifecycle: List<LifecycleHandler> =
    lifecycle {
      handler(FeaturePriority.healthCheck) {
        start { serverIsRunning.store(true) }
        stop { serverIsRunning.store(false) }
      }
    }

  override fun Application.routing() {
    with(healthCheckHandler) { routing() }
  }

  public companion object
}
