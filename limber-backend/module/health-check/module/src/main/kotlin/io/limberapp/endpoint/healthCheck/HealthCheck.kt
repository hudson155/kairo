package io.limberapp.endpoint.healthCheck

import com.google.inject.Inject
import io.ktor.application.ApplicationCall
import io.limberapp.api.healthCheck.HealthCheckApi
import io.limberapp.auth.Auth
import io.limberapp.exception.healthCheck.HealthCheckFailed
import io.limberapp.mapper.healthCheck.HealthCheckMapper
import io.limberapp.model.healthCheck.HealthCheckModel
import io.limberapp.rep.healthCheck.HealthCheckRep
import io.limberapp.restInterface.EndpointHandler
import io.limberapp.restInterface.template
import io.limberapp.service.healthCheck.HealthCheckService

internal class HealthCheck @Inject constructor(
    private val healthCheckService: HealthCheckService,
    private val healthCheckMapper: HealthCheckMapper,
) : EndpointHandler<HealthCheckApi.Get, HealthCheckRep.Complete>(
    template = HealthCheckApi.Get::class.template(),
) {
  override suspend fun endpoint(call: ApplicationCall): HealthCheckApi.Get =
      HealthCheckApi.Get

  override suspend fun Handler.handle(endpoint: HealthCheckApi.Get): HealthCheckRep.Complete {
    auth { Auth.Allow }
    when (val healthCheck = healthCheckService.healthCheck()) {
      is HealthCheckModel.Unhealthy -> throw HealthCheckFailed(healthCheck.reason, healthCheck.e)
      is HealthCheckModel.Healthy -> return healthCheckMapper.completeRep(healthCheck)
    }
  }
}
