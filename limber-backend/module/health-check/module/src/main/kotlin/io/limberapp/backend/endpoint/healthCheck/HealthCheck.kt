package io.limberapp.backend.endpoint.healthCheck

import com.google.inject.Inject
import io.ktor.application.ApplicationCall
import io.limberapp.backend.api.healthCheck.HealthCheckApi
import io.limberapp.backend.exception.HealthCheckFailed
import io.limberapp.backend.mapper.healthCheck.HealthCheckMapper
import io.limberapp.backend.model.healthCheck.HealthCheckModel
import io.limberapp.backend.rep.healthCheck.HealthCheckRep
import io.limberapp.backend.service.healthCheck.HealthCheckService
import io.limberapp.common.auth.Auth
import io.limberapp.common.restInterface.EndpointHandler
import io.limberapp.common.restInterface.template

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
