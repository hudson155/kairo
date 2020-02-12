package io.limberapp.backend.module.orgs.endpoint.org.membership

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
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.orgs.service.org.MembershipService
import java.util.UUID

/**
 * Creates a user's membership from an org.
 */
internal class DeleteMembership @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val membershipService: MembershipService
) : LimberApiEndpoint<DeleteMembership.Command, Unit>(
    application = application,
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

    override suspend fun Handler.handle(command: Command) {
        Authorization.OrgMember(command.orgId).authorize()
        membershipService.delete(
            orgId = command.orgId,
            userId = command.memberId
        )
    }

    companion object {
        const val orgId = "orgId"
        const val memberId = "memberId"
        val endpointConfig = EndpointConfig(
            httpMethod = HttpMethod.Delete,
            pathTemplate = listOf(
                StringComponent("orgs"),
                VariableComponent(orgId),
                StringComponent("memberships"),
                VariableComponent(memberId)
            )
        )
    }
}
