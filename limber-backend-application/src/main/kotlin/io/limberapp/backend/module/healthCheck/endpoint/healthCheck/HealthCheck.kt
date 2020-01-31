package io.limberapp.backend.module.healthCheck.endpoint.healthCheck

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.endpoint.EndpointConfig
import com.piperframework.endpoint.EndpointConfig.PathTemplateComponent.StringComponent
import com.piperframework.endpoint.command.AbstractCommand
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpMethod
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
) : LimberApiEndpoint<HealthCheck.Command, HealthCheckRep.Complete>(
    application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointConfig = endpointConfig
) {

    private val logger = LoggerFactory.getLogger(HealthCheck::class.java)

    internal object Command : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command

    override suspend fun Handler.handle(command: Command): HealthCheckRep.Complete {
        Authorization.Public.authorize()
        val model = healthCheckService.healthCheck()
        if (model is HealthCheckModel.UnhealthyHealthCheckModel) {
            logger.error("Health check failed: ${model.reason}", model.e)
        }
        return healthCheckMapper.completeRep(model)
    }

    companion object {
        val endpointConfig = EndpointConfig(
            httpMethod = HttpMethod.Get,
            pathTemplate = listOf(StringComponent("health-check"))
        )
    }
}
