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

/**
 * Creates a new tenant.
 */
internal class PostTenant @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val tenantService: TenantService,
    private val tenantMapper: TenantMapper
) : LimberApiEndpoint<TenantApi.Post, TenantRep.Complete>(
    application, servingConfig.apiPathPrefix,
    endpointTemplate = TenantApi.Post::class.template()
) {

    override suspend fun determineCommand(call: ApplicationCall) = TenantApi.Post(
        rep = call.getAndValidateBody()
    )

    override suspend fun Handler.handle(command: TenantApi.Post): TenantRep.Complete {
        Authorization.Role(JwtRole.SUPERUSER).authorize()
        val model = tenantMapper.model(command.rep.required())
        tenantService.create(model)
        return tenantMapper.completeRep(model)
    }
}
