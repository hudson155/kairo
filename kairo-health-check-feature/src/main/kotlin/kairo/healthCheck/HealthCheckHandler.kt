package kairo.healthCheck

import com.google.inject.Inject
import kairo.rest.RestHandler

internal class HealthCheckHandler @Inject constructor(
  private val healthCheckService: HealthCheckService,
) {
  internal inner class Liveness : RestHandler<HealthCheckApi.Liveness, Unit>() {
    override suspend fun handle(endpoint: HealthCheckApi.Liveness) {
      healthCheckService.liveness()
    }
  }

  internal inner class Readiness : RestHandler<HealthCheckApi.Readiness, HealthCheckRep>() {
    override suspend fun handle(endpoint: HealthCheckApi.Readiness): HealthCheckRep =
      healthCheckService.readiness()
  }
}
