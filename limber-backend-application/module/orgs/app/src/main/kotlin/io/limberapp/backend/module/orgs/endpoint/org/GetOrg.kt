package io.limberapp.backend.module.orgs.endpoint.org

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.endpoint.EndpointConfig
import com.piperframework.endpoint.command.AbstractCommand
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpMethod
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.orgs.exception.notFound.OrgNotFound
import io.limberapp.backend.module.orgs.mapper.api.org.OrgMapper
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.service.org.OrgService
import java.util.UUID

/**
 * Returns a single org.
 */
internal class GetOrg @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val orgService: OrgService,
    private val orgMapper: OrgMapper
) : LimberApiEndpoint<GetOrg.Command, OrgRep.Complete>(
    application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointConfig = endpointConfig
) {

    internal data class Command(
        val orgId: UUID
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        orgId = call.parameters.getAsType(UUID::class, orgId)
    )

    override fun authorization(command: Command) = Authorization.OrgMember(command.orgId)

    override suspend fun handler(command: Command): OrgRep.Complete {
        val model = orgService.get(command.orgId) ?: throw OrgNotFound()
        return orgMapper.completeRep(model)
    }

    companion object {
        const val orgId = "orgId"
        val endpointConfig = EndpointConfig(HttpMethod.Get, "/orgs/{$orgId}")
    }
}
