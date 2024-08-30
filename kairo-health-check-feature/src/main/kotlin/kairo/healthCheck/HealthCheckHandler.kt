package kairo.healthCheck

import com.google.inject.Inject
import kairo.rest.RestHandler

internal class HealthCheckHandler @Inject constructor(
  private val healthCheckService: HealthCheckService,
) {
  internal inner class Liveness : RestHandler<HealthCheckApi.Liveness, HealthCheckRep.Liveness>() {
    override suspend fun handle(endpoint: HealthCheckApi.Liveness): HealthCheckRep.Liveness =
      healthCheckService.liveness()
  }
}
