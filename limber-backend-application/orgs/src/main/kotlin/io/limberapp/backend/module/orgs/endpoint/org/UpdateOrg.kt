package io.limberapp.backend.module.orgs.endpoint.org

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpMethod
import io.ktor.request.receive
import io.limberapp.backend.module.orgs.mapper.OrgMapper
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.service.org.OrgService
import io.limberapp.framework.endpoint.RepApiEndpoint
import io.limberapp.framework.endpoint.authorization.Authorization
import io.limberapp.framework.endpoint.command.AbstractCommand
import java.util.UUID

internal class UpdateOrg @Inject constructor(
    application: Application,
    private val orgService: OrgService
) : RepApiEndpoint<UpdateOrg.Command, OrgRep.Complete>(application, config) {

    internal data class Command(
        val orgId: UUID,
        val updateRep: OrgRep.Update
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        orgId = call.parameters.getAsType(UUID::class, "orgId"),
        updateRep = call.receive()
    )

    override fun authorization(command: Command) = Authorization.OrgMember(command.orgId)

    override suspend fun handler(command: Command): OrgRep.Complete {
        val completeModel = orgService.update(
            id = command.orgId,
            model = OrgMapper.updateModel(command.updateRep)
        )
        return OrgMapper.completeRep(completeModel)
    }

    companion object {
        private val config = Config(HttpMethod.Patch, "/orgs/{orgId}")
    }
}
