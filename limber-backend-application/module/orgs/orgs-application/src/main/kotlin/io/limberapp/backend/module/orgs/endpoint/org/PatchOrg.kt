package io.limberapp.backend.module.orgs.endpoint.org

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.endpoint.EndpointConfig
import com.piperframework.endpoint.EndpointConfig.PathTemplateComponent.StringComponent
import com.piperframework.endpoint.EndpointConfig.PathTemplateComponent.VariableComponent
import com.piperframework.endpoint.command.AbstractCommand
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpMethod
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.orgs.mapper.org.OrgMapper
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.service.org.OrgService
import java.util.UUID

/**
 * Updates an org's information.
 */
internal class PatchOrg @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val orgService: OrgService,
    private val orgMapper: OrgMapper
) : LimberApiEndpoint<PatchOrg.Command, OrgRep.Complete>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointConfig = endpointConfig
) {

    internal data class Command(
        val orgId: UUID,
        val updateRep: OrgRep.Update
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        orgId = call.parameters.getAsType(UUID::class, orgId),
        updateRep = call.getAndValidateBody<OrgRep.Update>().required()
    )

    override suspend fun Handler.handle(command: Command): OrgRep.Complete {
        Authorization.OrgMember(command.orgId).authorize()
        val update = orgMapper.update(command.updateRep)
        val model = orgService.update(command.orgId, update)
        return orgMapper.completeRep(model)
    }

    companion object {
        const val orgId = "orgId"
        val endpointConfig = EndpointConfig(
            httpMethod = HttpMethod.Patch,
            pathTemplate = listOf(StringComponent("orgs"), VariableComponent(orgId))
        )
    }
}
