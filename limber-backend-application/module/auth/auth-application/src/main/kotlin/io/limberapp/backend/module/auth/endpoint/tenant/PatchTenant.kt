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
import java.util.UUID

/**
 * Updates a tenant's information.
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
        val orgId: UUID,
        val updateRep: TenantRep.Update
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        orgId = call.parameters.getAsType(UUID::class, orgId),
        updateRep = call.getAndValidateBody<TenantRep.Update>().required()
    )

    override suspend fun Handler.handle(command: Command): TenantRep.Complete {
        Authorization.Role(JwtRole.SUPERUSER).authorize()
        val update = tenantMapper.update(command.updateRep)
        val model = tenantService.update(command.orgId, update)
        return tenantMapper.completeRep(model)
    }

    companion object {
        const val orgId = "orgId"
        val endpointConfig = EndpointConfig(
            httpMethod = HttpMethod.Patch,
            pathTemplate = listOf(StringComponent("tenants"), VariableComponent(orgId))
        )
    }
}
