package io.limberapp.backend.module.orgs.endpoint.org.role

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.restInterface.template
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.orgs.api.org.role.OrgRoleApi
import io.limberapp.backend.module.orgs.mapper.org.OrgRoleMapper
import io.limberapp.backend.module.orgs.rep.org.OrgRoleRep
import io.limberapp.backend.module.orgs.service.org.OrgRoleService
import java.util.UUID

/**
 * Creates a new role within an org.
 */
internal class PostOrgRole @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val orgRoleService: OrgRoleService,
    private val orgRoleMapper: OrgRoleMapper
) : LimberApiEndpoint<OrgRoleApi.Post, OrgRoleRep.Complete>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointTemplate = OrgRoleApi.Post::class.template()
) {
    override suspend fun determineCommand(call: ApplicationCall) = OrgRoleApi.Post(
        orgGuid = call.parameters.getAsType(UUID::class, "orgGuid"),
        rep = call.getAndValidateBody()
    )

    override suspend fun Handler.handle(command: OrgRoleApi.Post): OrgRoleRep.Complete {
        Authorization.OrgMember(command.orgGuid).authorize()
        val orgRole = orgRoleMapper.model(command.rep.required())
        orgRoleService.create(command.orgGuid, orgRole)
        return orgRoleMapper.completeRep(orgRole)
    }
}
