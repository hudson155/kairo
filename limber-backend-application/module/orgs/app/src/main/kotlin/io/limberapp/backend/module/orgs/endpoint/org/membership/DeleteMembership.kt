package io.limberapp.backend.module.orgs.endpoint.org.membership

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.endpoint.EndpointConfig
import com.piperframework.endpoint.command.AbstractCommand
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpMethod
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.orgs.service.org.OrgService
import java.util.UUID

/**
 * Creates a user's membership from an org.
 */
internal class DeleteMembership @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val orgService: OrgService
) : LimberApiEndpoint<DeleteMembership.Command, Unit>(
    application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointConfig = endpointConfig
) {

    internal data class Command(
        val orgId: UUID,
        val memberId: UUID
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        orgId = call.parameters.getAsType(UUID::class, orgId),
        memberId = call.parameters.getAsType(UUID::class, memberId)
    )

    override fun authorization(command: Command) = Authorization.OrgMember(command.orgId)

    override suspend fun handler(command: Command) {
        orgService.deleteMembership(
            id = command.orgId,
            memberId = command.memberId
        )
    }

    companion object {
        const val orgId = "orgId"
        const val memberId = "memberId"
        val endpointConfig = EndpointConfig(HttpMethod.Delete, "/orgs/{$orgId}/memberships/{$memberId}")
    }
}
