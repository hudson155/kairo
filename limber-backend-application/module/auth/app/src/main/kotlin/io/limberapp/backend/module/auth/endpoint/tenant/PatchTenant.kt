package io.limberapp.backend.module.auth.endpoint.tenant

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.endpoint.EndpointConfig
import com.piperframework.endpoint.EndpointConfig.PathTemplateComponent.StringComponent
import com.piperframework.endpoint.EndpointConfig.PathTemplateComponent.VariableComponent
import com.piperframework.endpoint.command.AbstractCommand
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpMethod
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.auth.mapper.tenant.TenantMapper
import io.limberapp.backend.module.auth.rep.tenant.TenantRep
import io.limberapp.backend.module.auth.service.tenant.TenantService

/**
 * Updates the tenant for the given domain.
 */
internal class PatchTenant @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val tenantService: TenantService,
    private val tenantMapper: TenantMapper
) : LimberApiEndpoint<PatchTenant.Command, TenantRep.Complete>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointConfig = endpointConfig
) {

    internal data class Command(
        val tenantDomain: String,
        val updateRep: TenantRep.Update
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        tenantDomain = call.parameters.getAsType(String::class, tenantDomain),
        updateRep = call.getAndValidateBody()
    )

    override suspend fun Handler.handle(command: Command): TenantRep.Complete {
        Authorization.Role(JwtRole.SUPERUSER).authorize()
        val update = tenantMapper.update(command.updateRep)
        val model = tenantService.update(command.tenantDomain, update)
        return tenantMapper.completeRep(model)
    }

    companion object {
        const val tenantDomain = "tenantDomain"
        val endpointConfig = EndpointConfig(
            httpMethod = HttpMethod.Patch,
            pathTemplate = listOf(StringComponent("tenants"), VariableComponent(tenantDomain))
        )
    }
}
