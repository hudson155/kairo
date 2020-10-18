package io.limberapp.backend.module.auth.endpoint.org.role.membership

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.auth.api.org.OrgRoleMembershipApi
import io.limberapp.backend.module.auth.mapper.org.OrgRoleMembershipMapper
import io.limberapp.backend.module.auth.rep.org.OrgRoleMembershipRep
import io.limberapp.backend.module.auth.service.org.OrgRoleMembershipService
import io.limberapp.common.restInterface.template
import io.limberapp.permissions.orgPermissions.OrgPermission
import java.util.*

internal class GetOrgRoleMembershipsByOrgRoleGuid @Inject constructor(
    application: Application,
    private val orgRoleMembershipService: OrgRoleMembershipService,
    private val orgRoleMembershipMapper: OrgRoleMembershipMapper,
) : LimberApiEndpoint<OrgRoleMembershipApi.GetByOrgRoleGuid, Set<OrgRoleMembershipRep.Complete>>(
    application = application,
    endpointTemplate = OrgRoleMembershipApi.GetByOrgRoleGuid::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = OrgRoleMembershipApi.GetByOrgRoleGuid(
      orgGuid = call.parameters.getAsType(UUID::class, "orgGuid"),
      orgRoleGuid = call.parameters.getAsType(UUID::class, "orgRoleGuid")
  )

  override suspend fun Handler.handle(
      command: OrgRoleMembershipApi.GetByOrgRoleGuid,
  ): Set<OrgRoleMembershipRep.Complete> {
    Authorization.OrgMemberWithPermission(command.orgGuid, OrgPermission.MANAGE_ORG_ROLE_MEMBERSHIPS).authorize()
    val orgRoleMemberships = orgRoleMembershipService.getByOrgRoleGuid(
        orgGuid = command.orgGuid,
        orgRoleGuid = command.orgRoleGuid
    )
    return orgRoleMemberships.map { orgRoleMembershipMapper.completeRep(it) }.toSet()
  }
}
