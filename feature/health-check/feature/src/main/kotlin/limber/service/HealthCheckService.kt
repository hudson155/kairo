package limber.service

import limber.api.HealthCheckApi
import limber.client.HealthCheckClient
import limber.model.HealthCheck
import limber.rep.HealthCheckRep
import mu.KLogger
import mu.KotlinLogging

public abstract class HealthCheckService(private val healthCheckClient: HealthCheckClient) {
  private val logger: KLogger = KotlinLogging.logger {}

  public abstract val healthChecks: Map<String, HealthCheck>

  public var server: HealthCheckRep.State = HealthCheckRep.State.Unhealthy

  /**
   * Executes all health checks in order, sequentially. Consider parallelizing these in the future.
   */
  public suspend fun healthCheck(): HealthCheckRep =
    healthChecks.mapValues { check ->
      HealthCheckRep.Check(name = check.key, state = check.value.check())
    }.let { checks ->
      val areAllHealthy = checks.all { it.value.state === HealthCheckRep.State.Healthy }
      return@let HealthCheckRep(
        state = if (areAllHealthy) HealthCheckRep.State.Healthy else HealthCheckRep.State.Unhealthy,
        checks = checks,
      )
    }

  protected fun serverHealthCheck(): HealthCheckRep.State =
    server

  protected suspend fun httpHealthCheck(): HealthCheckRep.State =
    healthyIfNoException {
      healthCheckClient(HealthCheckApi.GetLiveness)
    }

  protected suspend fun healthyIfNoException(block: suspend () -> Unit): HealthCheckRep.State {
    @Suppress("TooGenericExceptionCaught")
    try {
      block()
      return HealthCheckRep.State.Healthy
    } catch (e: Exception) {
      logger.warn(e) { "Health check failed." }
      return HealthCheckRep.State.Unhealthy
    }
  }
}
