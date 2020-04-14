package io.limberapp.backend.module.auth.endpoint.tenant.domain

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
import io.limberapp.backend.module.auth.mapper.tenant.TenantDomainMapper
import io.limberapp.backend.module.auth.rep.tenant.TenantDomainRep
import io.limberapp.backend.module.auth.service.tenant.TenantDomainService
import java.util.UUID

/**
 * Creates a new domain within a tenant.
 */
internal class PostTenantDomain @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val tenantDomainService: TenantDomainService,
    private val tenantDomainMapper: TenantDomainMapper
) : LimberApiEndpoint<PostTenantDomain.Command, TenantDomainRep.Complete>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointConfig = endpointConfig
) {

    internal data class Command(
        val orgId: UUID,
        val creationRep: TenantDomainRep.Creation
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        orgId = call.parameters.getAsType(UUID::class, orgId),
        creationRep = call.getAndValidateBody()
    )

    override suspend fun Handler.handle(command: Command): TenantDomainRep.Complete {
        Authorization.Role(JwtRole.SUPERUSER).authorize()
        val model = tenantDomainMapper.model(command.creationRep)
        tenantDomainService.create(command.orgId, model)
        return tenantDomainMapper.completeRep(model)
    }

    companion object {
        const val orgId = "orgId"
        val endpointConfig = EndpointConfig(
            httpMethod = HttpMethod.Post,
            pathTemplate = listOf(StringComponent("tenants"), VariableComponent(orgId), StringComponent("domains"))
        )
    }
}
