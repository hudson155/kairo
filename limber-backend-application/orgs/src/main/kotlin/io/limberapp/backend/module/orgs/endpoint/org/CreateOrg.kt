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
import io.limberapp.framework.endpoint.authorization.AnyJwt
import io.limberapp.framework.endpoint.command.AbstractCommand

internal class CreateOrg @Inject constructor(
    application: Application,
    private val orgService: OrgService
) : RepApiEndpoint<CreateOrg.Command, OrgRep.Complete>(application, config) {

    data class Command(
        val creationRep: OrgRep.Creation
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        creationRep = call.receive()
    )

    override fun authorization(command: Command) = AnyJwt

    override suspend fun handler(command: Command): OrgRep.Complete {
        val completeModel = orgService.create(OrgMapper.creationModel(command.creationRep))
        return OrgMapper.completeRep(completeModel)
    }

    companion object {
        private val config = Config(HttpMethod.Post, "/orgs")
    }
}
