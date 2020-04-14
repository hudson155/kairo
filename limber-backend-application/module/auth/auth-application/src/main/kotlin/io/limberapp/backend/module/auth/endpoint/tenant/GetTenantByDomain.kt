package io.limberapp.backend.module.auth.endpoint.tenant

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
import io.limberapp.backend.module.auth.exception.tenant.TenantNotFound
import io.limberapp.backend.module.auth.mapper.tenant.TenantMapper
import io.limberapp.backend.module.auth.rep.tenant.TenantRep
import io.limberapp.backend.module.auth.service.tenant.TenantService

/**
 * Returns the tenant for the given domain.
 */
internal class GetTenantByDomain @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val tenantService: TenantService,
    private val tenantMapper: TenantMapper
) : LimberApiEndpoint<GetTenantByDomain.Command, TenantRep.Complete>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointConfig = endpointConfig
) {

    internal data class Command(
        val tenantDomain: String
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        tenantDomain = call.parameters.getAsType(String::class, domain)
    )

    override suspend fun Handler.handle(command: Command): TenantRep.Complete {
        Authorization.Public.authorize()
        val model = tenantService.getByDomain(command.tenantDomain) ?: throw TenantNotFound()
        return tenantMapper.completeRep(model)
    }

    companion object {
        const val domain = "domain"
        val endpointConfig = EndpointConfig(
            httpMethod = HttpMethod.Get,
            pathTemplate = listOf(StringComponent("tenants"))
        )
    }
}
