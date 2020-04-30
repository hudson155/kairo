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
 * Returns all roles within the given org.
 */
internal class GetOrgRolesByOrgGuid @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val orgRoleService: OrgRoleService,
    private val orgRoleMapper: OrgRoleMapper
) : LimberApiEndpoint<OrgRoleApi.GetByOrgGuid, Set<OrgRoleRep.Complete>>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointTemplate = OrgRoleApi.GetByOrgGuid::class.template()
) {
    override suspend fun determineCommand(call: ApplicationCall) = OrgRoleApi.GetByOrgGuid(
        orgGuid = call.parameters.getAsType(UUID::class, "orgGuid")
    )

    override suspend fun Handler.handle(command: OrgRoleApi.GetByOrgGuid): Set<OrgRoleRep.Complete> {
        Authorization.OrgMember(command.orgGuid).authorize()
        val orgRoles = orgRoleService.getByOrgGuid(command.orgGuid)
        return orgRoles.map { orgRoleMapper.completeRep(it) }.toSet()
    }
}
