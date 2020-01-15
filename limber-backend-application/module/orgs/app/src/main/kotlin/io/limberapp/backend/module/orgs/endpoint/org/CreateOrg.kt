package io.limberapp.backend.module.orgs.endpoint.org

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.endpoint.EndpointConfig
import com.piperframework.endpoint.EndpointConfig.PathTemplateComponent.StringComponent
import com.piperframework.endpoint.command.AbstractCommand
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpMethod
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.orgs.mapper.api.org.OrgMapper
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.service.org.OrgService

/**
 * Creates a new org.
 */
internal class CreateOrg @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val orgService: OrgService,
    private val orgMapper: OrgMapper
) : LimberApiEndpoint<CreateOrg.Command, OrgRep.Complete>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointConfig = endpointConfig
) {

    internal data class Command(
        val creationRep: OrgRep.Creation
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        creationRep = call.getAndValidateBody()
    )

    override suspend fun Handler.handle(command: Command): OrgRep.Complete {
        Authorization.Role(JwtRole.SUPERUSER).authorize()
        val model = orgMapper.model(command.creationRep)
        orgService.create(model)
        return orgMapper.completeRep(model)
    }

    companion object {
        val endpointConfig = EndpointConfig(
            httpMethod = HttpMethod.Post,
            pathTemplate = listOf(StringComponent("orgs"))
        )
    }
}
