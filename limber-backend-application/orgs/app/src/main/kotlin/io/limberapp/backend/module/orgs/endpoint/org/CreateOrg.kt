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

/**
 * Creates a new org.
 */
internal class CreateOrg @Inject constructor(
    application: Application,
    config: Config,
    private val orgService: OrgService,
    private val orgMapper: OrgMapper
) : ApiEndpoint<CreateOrg.Command, OrgRep.Complete>(
    application = application,
    pathPrefix = config.serving.apiPathPrefix,
    endpointConfig = endpointConfig
) {

    internal data class Command(
        val creationRep: OrgRep.Creation
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        creationRep = call.receive()
    )

    override fun authorization(command: Command) = Authorization.Superuser

    override suspend fun handler(command: Command): OrgRep.Complete {
        val completeEntity = orgService.create(orgMapper.creationModel(command.creationRep))
        return orgMapper.completeRep(completeEntity)
    }

    companion object {
        val endpointConfig = EndpointConfig(HttpMethod.Post, "/orgs")
    }
}
