package io.limberapp.backend.module.auth.endpoint.org.role.membership

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.authorization.permissions.orgPermissions.OrgPermission
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.auth.api.org.OrgRoleMembershipApi
import io.limberapp.backend.module.auth.service.org.OrgRoleMembershipService
import io.limberapp.common.restInterface.template
import java.util.*

internal class DeleteOrgRoleMembership @Inject constructor(
    application: Application,
    private val orgRoleMembershipService: OrgRoleMembershipService,
) : LimberApiEndpoint<OrgRoleMembershipApi.Delete, Unit>(
    application = application,
    endpointTemplate = OrgRoleMembershipApi.Delete::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = OrgRoleMembershipApi.Delete(
      orgGuid = call.parameters.getAsType(UUID::class, "orgGuid"),
      orgRoleGuid = call.parameters.getAsType(UUID::class, "orgRoleGuid"),
      accountGuid = call.parameters.getAsType(UUID::class, "accountGuid")
  )

  override suspend fun Handler.handle(command: OrgRoleMembershipApi.Delete) {
    Authorization.OrgMemberWithPermission(command.orgGuid, OrgPermission.MANAGE_ORG_ROLE_MEMBERSHIPS).authorize()
    orgRoleMembershipService.delete(command.orgGuid, command.orgRoleGuid, command.accountGuid)
  }
}
