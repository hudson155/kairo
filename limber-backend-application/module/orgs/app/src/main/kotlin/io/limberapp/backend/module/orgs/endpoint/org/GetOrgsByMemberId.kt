package io.limberapp.backend.module.orgs.endpoint.org

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.endpoint.EndpointConfig
import com.piperframework.endpoint.EndpointConfig.PathTemplateComponent.StringComponent
import com.piperframework.endpoint.command.AbstractCommand
import com.piperframework.module.annotation.Service
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpMethod
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.orgs.mapper.api.org.OrgMapper
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.service.org.OrgService
import java.util.UUID

/**
 * Returns all orgs that the user is a member of.
 */
internal class GetOrgsByMemberId @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    @Service private val orgService: OrgService,
    private val orgMapper: OrgMapper
) : LimberApiEndpoint<GetOrgsByMemberId.Command, List<OrgRep.Complete>>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointConfig = endpointConfig
) {

    internal data class Command(
        val memberId: UUID
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        memberId = call.parameters.getAsType(UUID::class, memberId)
    )

    override suspend fun Handler.handle(command: Command): List<OrgRep.Complete> {
        Authorization.User(command.memberId).authorize()
        val model = orgService.getByMemberId(command.memberId)
        return model.map { orgMapper.completeRep(it) }
    }

    companion object {
        const val memberId = "memberId"
        val endpointConfig = EndpointConfig(
            httpMethod = HttpMethod.Get,
            pathTemplate = listOf(StringComponent("orgs"))
        )
    }
}
