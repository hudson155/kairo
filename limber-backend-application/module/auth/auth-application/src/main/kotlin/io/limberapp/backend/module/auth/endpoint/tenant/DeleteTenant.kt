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
import io.limberapp.backend.module.auth.service.tenant.TenantService
import java.util.UUID

/**
 * Deletes an existing tenant.
 */
internal class DeleteTenant @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val tenantService: TenantService
) : LimberApiEndpoint<TenantApi.Delete, Unit>(
    application, servingConfig.apiPathPrefix,
    endpointTemplate = TenantApi.Delete::class.template()
) {

    override suspend fun determineCommand(call: ApplicationCall) = TenantApi.Delete(
        orgId = call.parameters.getAsType(UUID::class, "orgId")
    )

    override suspend fun Handler.handle(command: TenantApi.Delete) {
        Authorization.Role(JwtRole.SUPERUSER).authorize()
        tenantService.delete(command.orgId)
    }
}
