package limber.service.healthCheck

import com.google.inject.Inject
import limber.api.HealthCheckApi
import limber.client.HealthCheckClient
import limber.model.HealthCheck
import limber.rep.HealthCheckRep
import limber.service.HealthCheckService

internal class HealthCheckServiceImpl @Inject constructor(
  private val healthCheckClient: HealthCheckClient,
) : HealthCheckService() {
  override val healthChecks: Map<String, HealthCheck> = mapOf(
    "basic" to HealthCheck(::basicHealthCheck),
    "http" to HealthCheck(::httpHealthCheck),
  )

  override var server: HealthCheckRep.State = HealthCheckRep.State.Unhealthy

  private fun basicHealthCheck(): HealthCheckRep.State =
    HealthCheckRep.State.Healthy

  private suspend fun httpHealthCheck(): HealthCheckRep.State =
    healthyIfNoException {
      healthCheckClient(HealthCheckApi.GetLiveness)
    }
}
