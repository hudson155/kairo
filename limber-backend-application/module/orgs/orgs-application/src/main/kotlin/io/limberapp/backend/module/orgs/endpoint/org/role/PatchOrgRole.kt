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
 * Deletes an org role's information.
 */
internal class PatchOrgRole @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val orgRoleService: OrgRoleService,
    private val orgRoleMapper: OrgRoleMapper
) : LimberApiEndpoint<OrgRoleApi.Patch, OrgRoleRep.Complete>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointTemplate = OrgRoleApi.Patch::class.template()
) {
    override suspend fun determineCommand(call: ApplicationCall) = OrgRoleApi.Patch(
        orgGuid = call.parameters.getAsType(UUID::class, "orgGuid"),
        orgRoleGuid = call.parameters.getAsType(UUID::class, "orgRoleGuid"),
        rep = call.getAndValidateBody()
    )

    override suspend fun Handler.handle(command: OrgRoleApi.Patch): OrgRoleRep.Complete {
        Authorization.OrgMember(command.orgGuid).authorize()
        val update = orgRoleMapper.update(command.rep.required())
        val orgRole = orgRoleService.update(command.orgGuid, command.orgRoleGuid, update)
        return orgRoleMapper.completeRep(orgRole)
    }
}
