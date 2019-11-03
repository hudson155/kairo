package io.limberapp.backend.module.orgs.endpoint.org.membership

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpMethod
import io.limberapp.backend.module.orgs.service.org.OrgService
import io.limberapp.framework.endpoint.ApiEndpoint
import io.limberapp.framework.endpoint.authorization.Authorization
import io.limberapp.framework.endpoint.command.AbstractCommand
import java.util.UUID

internal class DeleteMembership @Inject constructor(
    application: Application,
    private val orgService: OrgService
) : ApiEndpoint<DeleteMembership.Command, Unit>(application, config) {

    internal data class Command(
        val orgId: UUID,
        val memberId: UUID
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        orgId = call.parameters.getAsType(UUID::class, "orgId"),
        memberId = call.parameters.getAsType(UUID::class, "memberId")
    )

    override fun authorization(command: Command) = Authorization.OrgMember(command.orgId)

    override suspend fun handler(command: Command) {
        orgService.deleteMembership(
            id = command.orgId,
            memberId = command.memberId
        )
    }

    companion object {
        val config = Config(HttpMethod.Delete, "/orgs/{orgId}/memberships/{memberId}")
    }
}
