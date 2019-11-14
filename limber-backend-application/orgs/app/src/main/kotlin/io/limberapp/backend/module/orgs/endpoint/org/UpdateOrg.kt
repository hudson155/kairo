package io.limberapp.backend.module.orgs.endpoint.org

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpMethod
import io.ktor.request.receive
import io.limberapp.backend.module.orgs.mapper.api.org.OrgMapper
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.service.org.OrgService
import io.limberapp.framework.config.Config
import io.limberapp.framework.endpoint.ApiEndpoint
import io.limberapp.framework.endpoint.EndpointConfig
import io.limberapp.framework.endpoint.authorization.Authorization
import io.limberapp.framework.endpoint.command.AbstractCommand
import java.util.UUID

/**
 * Updates an org's information.
 */
internal class UpdateOrg @Inject constructor(
    application: Application,
    config: Config,
    private val orgService: OrgService,
    private val orgMapper: OrgMapper
) : ApiEndpoint<UpdateOrg.Command, OrgRep.Complete>(
    application,
    pathPrefix = config.serving.apiPathPrefix,
    endpointConfig = endpointConfig
) {

    internal data class Command(
        val orgId: UUID,
        val updateRep: OrgRep.Update
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        orgId = call.parameters.getAsType(UUID::class, orgId),
        updateRep = call.receive()
    )

    override fun authorization(command: Command) = Authorization.OrgMember(command.orgId)

    override suspend fun handler(command: Command): OrgRep.Complete {
        val completeEntity = orgService.update(
            id = command.orgId,
            entity = orgMapper.updateEntity(command.updateRep)
        )
        return orgMapper.completeRep(completeEntity)
    }

    companion object {
        const val orgId = "orgId"
        val endpointConfig = EndpointConfig(HttpMethod.Patch, "/orgs/{$orgId}")
    }
}
