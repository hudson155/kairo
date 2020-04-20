package io.limberapp.backend.module.healthCheck.endpoint.healthCheck

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.restInterface.template
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.limberapp.backend.api.healthCheck.HealthCheckApi
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.healthCheck.mapper.healthCheck.HealthCheckMapper
import io.limberapp.backend.module.healthCheck.model.healthCheck.HealthCheckModel
import io.limberapp.backend.module.healthCheck.rep.healthCheck.HealthCheckRep
import io.limberapp.backend.module.healthCheck.service.healthCheck.HealthCheckService
import org.slf4j.LoggerFactory

/**
 * Performs a health check.
 */
internal class HealthCheck @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val healthCheckService: HealthCheckService,
    private val healthCheckMapper: HealthCheckMapper
) : LimberApiEndpoint<HealthCheckApi.Get, HealthCheckRep.Complete>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointTemplate = HealthCheckApi.Get::class.template()
) {

    private val logger = LoggerFactory.getLogger(HealthCheck::class.java)

    override suspend fun determineCommand(call: ApplicationCall) = HealthCheckApi.Get

    override suspend fun Handler.handle(command: HealthCheckApi.Get): HealthCheckRep.Complete {
        Authorization.Public.authorize()
        val model = healthCheckService.healthCheck()
        if (model is HealthCheckModel.UnhealthyHealthCheckModel) {
            logger.error("Health check failed: ${model.reason}", model.e)
            responseCode = HttpStatusCode.InternalServerError
        }
        return healthCheckMapper.completeRep(model)
    }
}
