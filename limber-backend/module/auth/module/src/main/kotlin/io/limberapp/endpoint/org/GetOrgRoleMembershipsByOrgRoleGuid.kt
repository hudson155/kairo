package io.limberapp.endpoint.org

import com.google.inject.Inject
import io.ktor.application.ApplicationCall
import io.limberapp.api.org.OrgRoleMembershipApi
import io.limberapp.auth.auth.AuthOrgMember
import io.limberapp.exception.org.OrgRoleNotFound
import io.limberapp.exception.unprocessable
import io.limberapp.mapper.org.OrgRoleMembershipMapper
import io.limberapp.permissions.org.OrgPermission
import io.limberapp.rep.org.OrgRoleMembershipRep
import io.limberapp.restInterface.EndpointHandler
import io.limberapp.restInterface.template
import io.limberapp.service.org.OrgRoleMembershipService
import io.limberapp.service.org.OrgRoleService
import java.util.UUID

internal class GetOrgRoleMembershipsByOrgRoleGuid @Inject constructor(
    private val orgRoleService: OrgRoleService,
    private val orgRoleMembershipService: OrgRoleMembershipService,
    private val orgRoleMembershipMapper: OrgRoleMembershipMapper,
) : EndpointHandler<OrgRoleMembershipApi.GetByOrgRoleGuid, Set<OrgRoleMembershipRep.Complete>>(
    template = OrgRoleMembershipApi.GetByOrgRoleGuid::class.template(),
) {
  override suspend fun endpoint(call: ApplicationCall): OrgRoleMembershipApi.GetByOrgRoleGuid =
      OrgRoleMembershipApi.GetByOrgRoleGuid(
          orgRoleGuid = call.getParam(UUID::class, "orgRoleGuid"),
      )

  override suspend fun Handler.handle(
      endpoint: OrgRoleMembershipApi.GetByOrgRoleGuid,
  ): Set<OrgRoleMembershipRep.Complete> {
    auth {
      val orgRole = orgRoleService[endpoint.orgRoleGuid] ?: throw OrgRoleNotFound().unprocessable()
      AuthOrgMember(orgRole.orgGuid, permission = OrgPermission.MANAGE_ORG_ROLE_MEMBERSHIPS)
    }
    val orgRoleMemberships = orgRoleMembershipService.getByOrgRoleGuid(endpoint.orgRoleGuid)
    return orgRoleMemberships.map { orgRoleMembershipMapper.completeRep(it) }.toSet()
  }
}
