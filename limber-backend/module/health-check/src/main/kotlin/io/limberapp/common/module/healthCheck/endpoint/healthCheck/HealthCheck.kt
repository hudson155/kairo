package io.limberapp.common.module.healthCheck.endpoint.healthCheck

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.common.module.healthCheck.api.healthCheck.HealthCheckApi
import io.limberapp.common.module.healthCheck.mapper.healthCheck.HealthCheckMapper
import io.limberapp.common.module.healthCheck.model.healthCheck.HealthCheckModel
import io.limberapp.common.module.healthCheck.rep.healthCheck.HealthCheckRep
import io.limberapp.common.module.healthCheck.service.healthCheck.HealthCheckService
import io.limberapp.common.restInterface.template
import org.slf4j.LoggerFactory

internal class HealthCheck @Inject constructor(
  application: Application,
  private val healthCheckService: HealthCheckService,
  private val healthCheckMapper: HealthCheckMapper,
) : LimberApiEndpoint<HealthCheckApi.Get, HealthCheckRep.Complete>(
  application = application,
  endpointTemplate = HealthCheckApi.Get::class.template()
) {
  private val logger = LoggerFactory.getLogger(HealthCheck::class.java)

  override suspend fun determineCommand(call: ApplicationCall) = HealthCheckApi.Get

  override suspend fun Handler.handle(command: HealthCheckApi.Get): HealthCheckRep.Complete {
    Authorization.Public.authorize()
    when (val healthCheck = healthCheckService.healthCheck()) {
      is HealthCheckModel.UnhealthyHealthCheckModel -> {
        logger.error("Health check failed: ${healthCheck.reason}", healthCheck.e)
        responseCode = HttpStatusCode.InternalServerError
        return healthCheckMapper.completeRep(healthCheck)
      }
      HealthCheckModel.HealthyHealthCheckModel -> return healthCheckMapper.completeRep(healthCheck)
    }
  }
}
