package io.limberapp.backend.module.auth.endpoint.org.role

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.restInterface.template
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.authorization.permissions.OrgPermission
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.auth.api.org.role.OrgRoleApi
import io.limberapp.backend.module.auth.service.org.OrgRoleService
import java.util.UUID

/**
 * Deletes a role from an org.
 */
internal class DeleteOrgRole @Inject constructor(
  application: Application,
  servingConfig: ServingConfig,
  private val orgRoleService: OrgRoleService
) : LimberApiEndpoint<OrgRoleApi.Delete, Unit>(
  application = application,
  pathPrefix = servingConfig.apiPathPrefix,
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
