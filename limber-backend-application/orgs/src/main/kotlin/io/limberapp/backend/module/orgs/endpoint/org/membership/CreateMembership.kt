package io.limberapp.backend.module.orgs.endpoint.org.membership

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpMethod
import io.ktor.request.receive
import io.limberapp.backend.module.orgs.mapper.membership.MembershipMapper
import io.limberapp.backend.module.orgs.rep.membership.MembershipRep
import io.limberapp.backend.module.orgs.service.org.OrgService
import io.limberapp.framework.endpoint.ApiEndpoint
import io.limberapp.framework.endpoint.authorization.Authorization
import io.limberapp.framework.endpoint.command.AbstractCommand
import java.util.UUID

internal class CreateMembership @Inject constructor(
    application: Application,
    private val orgService: OrgService
) : ApiEndpoint<CreateMembership.Command, Unit>(application, config) {

    internal data class Command(
        val orgId: UUID,
        val creationRep: MembershipRep.Creation
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        orgId = call.parameters.getAsType(UUID::class, "orgId"),
        creationRep = call.receive()
    )

    override fun authorization(command: Command) = Authorization.OrgMember(command.orgId)

    override suspend fun handler(command: Command) {
        orgService.createMembership(
            id = command.orgId,
            model = MembershipMapper.creationModel(command.creationRep)
        )
    }

    companion object {
        val config = Config(HttpMethod.Post, "/orgs/{orgId}/memberships")
    }
}
