package io.limberapp.backend.module.orgs.endpoint.org

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpMethod
import io.limberapp.backend.module.orgs.mapper.OrgMapper
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.service.org.OrgService
import io.limberapp.framework.endpoint.NullableRepApiEndpoint
import io.limberapp.framework.endpoint.authorization.Authorization
import io.limberapp.framework.endpoint.command.AbstractCommand
import java.util.UUID

internal class GetOrgById @Inject constructor(
    application: Application,
    private val orgService: OrgService
) : NullableRepApiEndpoint<GetOrgById.Command, OrgRep.Complete>(application, config) {

    internal data class Command(
        val orgId: UUID
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        orgId = call.parameters.getAsType(UUID::class, "orgId")
    )

    override fun authorization(command: Command) = Authorization.OrgMember(command.orgId)

    override suspend fun handler(command: Command): OrgRep.Complete? {
        val completeModel = orgService.getById(command.orgId)
        return completeModel?.let { OrgMapper.completeRep(it) }
    }

    companion object {
        private val config = Config(HttpMethod.Get, "/orgs/{orgId}")
    }
}
