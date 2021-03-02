package io.limberapp.endpoint.org

import com.google.inject.Inject
import io.ktor.application.ApplicationCall
import io.limberapp.api.org.OrgRoleMembershipApi
import io.limberapp.auth.auth.AuthOrgMember
import io.limberapp.exception.org.OrgRoleNotFound
import io.limberapp.exception.unprocessable
import io.limberapp.permissions.org.OrgPermission
import io.limberapp.restInterface.EndpointHandler
import io.limberapp.restInterface.template
import io.limberapp.service.org.OrgRoleMembershipService
import io.limberapp.service.org.OrgRoleService
import java.util.UUID

internal class DeleteOrgRoleMembership @Inject constructor(
    private val orgRoleService: OrgRoleService,
    private val orgRoleMembershipService: OrgRoleMembershipService,
) : EndpointHandler<OrgRoleMembershipApi.Delete, Unit>(
    template = OrgRoleMembershipApi.Delete::class.template(),
) {
  override suspend fun endpoint(call: ApplicationCall): OrgRoleMembershipApi.Delete =
      OrgRoleMembershipApi.Delete(
          orgRoleGuid = call.getParam(UUID::class, "orgRoleGuid"),
          userGuid = call.getParam(UUID::class, "userGuid"),
      )

  override suspend fun Handler.handle(endpoint: OrgRoleMembershipApi.Delete) {
    auth {
      val orgRole = orgRoleService[endpoint.orgRoleGuid] ?: throw OrgRoleNotFound().unprocessable()
      AuthOrgMember(orgRole.orgGuid, permission = OrgPermission.MANAGE_ORG_ROLE_MEMBERSHIPS)
    }
    orgRoleMembershipService.delete(endpoint.orgRoleGuid, endpoint.userGuid)
  }
}
