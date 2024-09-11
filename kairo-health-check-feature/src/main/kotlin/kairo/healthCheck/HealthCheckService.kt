package kairo.healthCheck

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger: KLogger = KotlinLogging.logger {}

public abstract class HealthCheckService {
  internal var status: HealthCheckRep.Status = HealthCheckRep.Status.Unhealthy

  protected abstract val healthChecks: Map<String, HealthCheck>

  public fun liveness(): Unit =
    Unit

  public suspend fun readiness(): HealthCheckRep {
    val checks = healthChecks.mapValues { (_, healthCheck) -> HealthCheckRep.Check(status = healthCheck.check()) }
    val allChecksAreHealthy = checks.all { (_, check) -> check.status == HealthCheckRep.Status.Healthy }
    return HealthCheckRep(
      status = if (allChecksAreHealthy) HealthCheckRep.Status.Healthy else HealthCheckRep.Status.Unhealthy,
      checks = checks,
    )
  }

  protected fun serverHealthCheck(): HealthCheckRep.Status =
    status

  protected suspend fun healthyIfNoException(
    block: suspend () -> Unit,
  ): HealthCheckRep.Status {
    try {
      block()
      return HealthCheckRep.Status.Healthy
    } catch (e: Exception) {
      logger.warn(e) { "Health check failed." }
      return HealthCheckRep.Status.Unhealthy
    }
  }
}
