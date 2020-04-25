package io.limberapp.backend.module.auth.endpoint.tenant

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.restInterface.template
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.auth.api.tenant.TenantApi
import io.limberapp.backend.module.auth.exception.tenant.TenantNotFound
import io.limberapp.backend.module.auth.mapper.tenant.TenantMapper
import io.limberapp.backend.module.auth.rep.tenant.TenantRep
import io.limberapp.backend.module.auth.service.tenant.TenantService
import java.util.UUID

/**
 * Returns a single tenant.
 */
internal class GetTenant @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val tenantService: TenantService,
    private val tenantMapper: TenantMapper
) : LimberApiEndpoint<TenantApi.Get, TenantRep.Complete>(
    application, servingConfig.apiPathPrefix,
    endpointTemplate = TenantApi.Get::class.template()
) {
    override suspend fun determineCommand(call: ApplicationCall) = TenantApi.Get(
        orgGuid = call.parameters.getAsType(UUID::class, "orgGuid")
    )

    override suspend fun Handler.handle(command: TenantApi.Get): TenantRep.Complete {
        Authorization.Public.authorize()
        val model = tenantService.get(command.orgGuid) ?: throw TenantNotFound()
        return tenantMapper.completeRep(model)
    }
}
