package io.limberapp.backend.module.orgs.endpoint.org

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpMethod
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.module.orgs.mapper.api.org.OrgMapper
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.service.org.OrgService
import com.piperframework.config.serving.ServingConfig
import com.piperframework.endpoint.ApiEndpoint
import com.piperframework.endpoint.EndpointConfig
import com.piperframework.endpoint.command.AbstractCommand
import java.util.UUID

/**
 * Updates an org's information.
 */
internal class UpdateOrg @Inject constructor(
    application: Application,
    servingConfig: com.piperframework.config.serving.ServingConfig,
    private val orgService: OrgService,
    private val orgMapper: OrgMapper
) : com.piperframework.endpoint.ApiEndpoint<UpdateOrg.Command, OrgRep.Complete>(
    application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointConfig = endpointConfig
) {

    internal data class Command(
        val orgId: UUID,
        val updateRep: OrgRep.Update
    ) : com.piperframework.endpoint.command.AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        orgId = call.parameters.getAsType(UUID::class, orgId),
        updateRep = call.getAndValidateBody()
    )

    override fun authorization(command: Command) = Authorization.OrgMember(command.orgId)

    override suspend fun handler(command: Command): OrgRep.Complete {
        val update = orgMapper.update(command.updateRep)
        val model = orgService.update(command.orgId, update)
        return orgMapper.completeRep(model)
    }

    companion object {
        const val orgId = "orgId"
        val endpointConfig = com.piperframework.endpoint.EndpointConfig(HttpMethod.Patch, "/orgs/{$orgId}")
    }
}
