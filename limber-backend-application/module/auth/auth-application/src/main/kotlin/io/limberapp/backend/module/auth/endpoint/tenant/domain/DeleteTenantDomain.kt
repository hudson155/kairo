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
import io.limberapp.backend.module.auth.service.tenant.TenantDomainService
import java.util.UUID

/**
 * Deletes a domain from a tenant.
 */
internal class DeleteTenantDomain @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val tenantDomainService: TenantDomainService
) : LimberApiEndpoint<DeleteTenantDomain.Command, Unit>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointConfig = endpointConfig
) {

    internal data class Command(
        val orgId: UUID,
        val domain: String
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        orgId = call.parameters.getAsType(UUID::class, orgId),
        domain = call.parameters.getAsType(String::class, domain)
    )

    override suspend fun Handler.handle(command: Command) {
        Authorization.Role(JwtRole.SUPERUSER).authorize()
        tenantDomainService.delete(command.orgId, command.domain)
    }

    companion object {
        const val orgId = "orgId"
        const val domain = "domain"
        val endpointConfig = EndpointConfig(
            httpMethod = HttpMethod.Delete,
            pathTemplate = listOf(
                StringComponent("tenants"),
                VariableComponent(orgId),
                StringComponent("domains"),
                VariableComponent(domain)
            )
        )
    }
}
