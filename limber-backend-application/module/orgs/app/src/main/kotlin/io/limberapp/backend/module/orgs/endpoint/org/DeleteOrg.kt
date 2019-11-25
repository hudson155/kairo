package io.limberapp.backend.module.orgs.endpoint.org

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpMethod
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.module.orgs.service.org.OrgService
import com.piperframework.config.serving.ServingConfig
import com.piperframework.endpoint.ApiEndpoint
import com.piperframework.endpoint.EndpointConfig
import com.piperframework.endpoint.command.AbstractCommand
import java.util.UUID

/**
 * Deletes an existing org.
 */
internal class DeleteOrg @Inject constructor(
    application: Application,
    servingConfig: com.piperframework.config.serving.ServingConfig,
    private val orgService: OrgService
) : com.piperframework.endpoint.ApiEndpoint<DeleteOrg.Command, Unit>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointConfig = endpointConfig
) {

    internal data class Command(
        val orgId: UUID
    ) : com.piperframework.endpoint.command.AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        orgId = call.parameters.getAsType(UUID::class, orgId)
    )

    override fun authorization(command: Command) = Authorization.Superuser

    override suspend fun handler(command: Command) {
        orgService.delete(command.orgId)
    }

    companion object {
        const val orgId = "orgId"
        val endpointConfig = com.piperframework.endpoint.EndpointConfig(HttpMethod.Delete, "/orgs/{$orgId}")
    }
}
