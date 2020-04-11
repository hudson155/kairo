package io.limberapp.backend.module.orgs.endpoint.org

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
import io.limberapp.backend.module.orgs.service.org.OrgService
import java.util.UUID

/**
 * Deletes an existing org.
 */
internal class DeleteOrg @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val orgService: OrgService
) : LimberApiEndpoint<DeleteOrg.Command, Unit>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointConfig = endpointConfig
) {

    internal data class Command(
        val orgId: UUID
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        orgId = call.parameters.getAsType(UUID::class, orgId)
    )

    override suspend fun Handler.handle(command: Command) {
        Authorization.Role(JwtRole.SUPERUSER).authorize()
        orgService.delete(command.orgId)
    }

    companion object {
        const val orgId = "orgId"
        val endpointConfig = EndpointConfig(
            httpMethod = HttpMethod.Delete,
            pathTemplate = listOf(StringComponent("orgs"), VariableComponent(orgId))
        )
    }
}
