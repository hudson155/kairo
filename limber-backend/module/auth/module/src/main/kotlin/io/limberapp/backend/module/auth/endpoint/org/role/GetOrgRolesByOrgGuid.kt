package io.limberapp.backend.module.auth.endpoint.org.role

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.auth.api.org.OrgRoleApi
import io.limberapp.backend.module.auth.mapper.org.OrgRoleMapper
import io.limberapp.backend.module.auth.rep.org.OrgRoleRep
import io.limberapp.backend.module.auth.service.org.OrgRoleService
import io.limberapp.common.restInterface.template
import io.limberapp.permissions.orgPermissions.OrgPermission
import java.util.*

internal class GetOrgRolesByOrgGuid @Inject constructor(
    application: Application,
    private val orgRoleService: OrgRoleService,
    private val orgRoleMapper: OrgRoleMapper,
) : LimberApiEndpoint<OrgRoleApi.GetByOrgGuid, Set<OrgRoleRep.Complete>>(
    application = application,
    endpointTemplate = OrgRoleApi.GetByOrgGuid::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = OrgRoleApi.GetByOrgGuid(
      orgGuid = call.parameters.getAsType(UUID::class, "orgGuid")
  )

  override suspend fun Handler.handle(command: OrgRoleApi.GetByOrgGuid): Set<OrgRoleRep.Complete> {
    Authorization.OrgMemberWithPermission(command.orgGuid, OrgPermission.MANAGE_ORG_ROLES).authorize()
    val orgRoles = orgRoleService.findAsSet { orgGuid(command.orgGuid) }
    return orgRoles.map { orgRoleMapper.completeRep(it) }.toSet()
  }
}
