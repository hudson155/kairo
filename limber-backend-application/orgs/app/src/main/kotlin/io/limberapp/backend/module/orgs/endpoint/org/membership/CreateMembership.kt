package io.limberapp.backend.module.orgs.endpoint.org.membership

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpMethod
import io.ktor.request.receive
import io.limberapp.backend.module.orgs.mapper.membership.MembershipMapper
import io.limberapp.backend.module.orgs.rep.membership.MembershipRep
import io.limberapp.backend.module.orgs.service.org.OrgService
import io.limberapp.framework.config.Config
import io.limberapp.framework.endpoint.ApiEndpoint
import io.limberapp.framework.endpoint.EndpointConfig
import io.limberapp.framework.endpoint.authorization.Authorization
import io.limberapp.framework.endpoint.command.AbstractCommand
import java.util.UUID

/**
 * Creates a membership for a user within an org.
 */
internal class CreateMembership @Inject constructor(
    application: Application,
    config: Config,
    private val orgService: OrgService,
    private val membershipMapper: MembershipMapper
) : ApiEndpoint<CreateMembership.Command, Unit>(
    application = application,
    pathPrefix = config.serving.apiPathPrefix,
    endpointConfig = endpointConfig
) {

    internal data class Command(
        val orgId: UUID,
        val creationRep: MembershipRep.Creation
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        orgId = call.parameters.getAsType(UUID::class, orgId),
        creationRep = call.receive()
    )

    override fun authorization(command: Command) = Authorization.OrgMember(command.orgId)

    override suspend fun handler(command: Command) {
        orgService.createMembership(
            id = command.orgId,
            entity = membershipMapper.creationEntity(command.creationRep)
        )
    }

    companion object {
        const val orgId = "orgId"
        val endpointConfig = EndpointConfig(HttpMethod.Post, "/orgs/{$orgId}/memberships")
    }
}
