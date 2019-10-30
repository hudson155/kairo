package io.limberapp.backend.module.orgs.endpoint.org

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpMethod
import io.limberapp.backend.module.orgs.mapper.org.OrgMapper
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.service.org.OrgService
import io.limberapp.framework.endpoint.RepListApiEndpoint
import io.limberapp.framework.endpoint.authorization.Authorization
import io.limberapp.framework.endpoint.command.AbstractCommand
import java.util.UUID

internal class GetOrgsByMemberId @Inject constructor(
    application: Application,
    private val orgService: OrgService
) : RepListApiEndpoint<GetOrgsByMemberId.Command, OrgRep.Complete>(application, config) {

    internal data class Command(
        val memberId: UUID
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        memberId = call.parameters.getAsType(UUID::class, "memberId")
    )

    override fun authorization(command: Command) = Authorization.Public

    override suspend fun handler(command: Command): List<OrgRep.Complete> {
        val completeModels = orgService.getByMemberId(command.memberId)
        return completeModels.map { OrgMapper.completeRep(it) }
    }

    companion object {
        private val config = Config(HttpMethod.Get, "/orgs")
    }
}
