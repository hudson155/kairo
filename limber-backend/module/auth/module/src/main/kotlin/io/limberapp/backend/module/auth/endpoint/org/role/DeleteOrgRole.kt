package io.limberapp.backend.module.auth.endpoint.org.role

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.authorization.permissions.orgPermissions.OrgPermission
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.auth.api.org.OrgRoleApi
import io.limberapp.backend.module.auth.service.org.OrgRoleService
import io.limberapp.common.restInterface.template
import java.util.*

internal class DeleteOrgRole @Inject constructor(
    application: Application,
    private val orgRoleService: OrgRoleService,
) : LimberApiEndpoint<OrgRoleApi.Delete, Unit>(
    application = application,
    endpointTemplate = OrgRoleApi.Delete::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = OrgRoleApi.Delete(
      orgGuid = call.parameters.getAsType(UUID::class, "orgGuid"),
      orgRoleGuid = call.parameters.getAsType(UUID::class, "orgRoleGuid")
  )

  override suspend fun Handler.handle(command: OrgRoleApi.Delete) {
    Authorization.OrgMemberWithPermission(command.orgGuid, OrgPermission.MANAGE_ORG_ROLES).authorize()
    orgRoleService.delete(command.orgGuid, command.orgRoleGuid)
  }
}
