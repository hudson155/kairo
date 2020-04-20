package io.limberapp.backend.module.auth.endpoint.tenant

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.restInterface.template
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.auth.api.tenant.TenantApi
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
) : LimberApiEndpoint<TenantApi.Patch, TenantRep.Complete>(
    application, servingConfig.apiPathPrefix,
    endpointTemplate = TenantApi.Patch::class.template()
) {

    override suspend fun determineCommand(call: ApplicationCall) = TenantApi.Patch(
        orgId = call.parameters.getAsType(UUID::class, "orgId"),
        rep = call.getAndValidateBody()
    )

    override suspend fun Handler.handle(command: TenantApi.Patch): TenantRep.Complete {
        Authorization.Role(JwtRole.SUPERUSER).authorize()
        val update = tenantMapper.update(command.rep.required())
        val model = tenantService.update(command.orgId, update)
        return tenantMapper.completeRep(model)
    }
}
