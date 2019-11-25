package io.limberapp.backend.module.orgs.endpoint.org

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpMethod
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.module.orgs.service.org.OrgService
import io.limberapp.framework.config.serving.ServingConfig
import io.limberapp.framework.endpoint.ApiEndpoint
import io.limberapp.framework.endpoint.EndpointConfig
import io.limberapp.framework.endpoint.command.AbstractCommand
import java.util.UUID

/**
 * Deletes an existing org.
 */
internal class DeleteOrg @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val orgService: OrgService
) : ApiEndpoint<DeleteOrg.Command, Unit>(
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

    override fun authorization(command: Command) = Authorization.Superuser

    override suspend fun handler(command: Command) {
        orgService.delete(command.orgId)
    }

    companion object {
        const val orgId = "orgId"
        val endpointConfig = EndpointConfig(HttpMethod.Delete, "/orgs/{$orgId}")
    }
}
