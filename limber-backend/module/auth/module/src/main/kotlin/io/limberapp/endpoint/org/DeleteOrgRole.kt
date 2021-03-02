package io.limberapp.endpoint.org

import com.google.inject.Inject
import io.ktor.application.ApplicationCall
import io.limberapp.api.org.OrgRoleApi
import io.limberapp.auth.auth.AuthOrgMember
import io.limberapp.exception.org.OrgRoleNotFound
import io.limberapp.permissions.org.OrgPermission
import io.limberapp.restInterface.EndpointHandler
import io.limberapp.restInterface.template
import io.limberapp.service.org.OrgRoleService
import java.util.UUID

internal class DeleteOrgRole @Inject constructor(
    private val orgRoleService: OrgRoleService,
) : EndpointHandler<OrgRoleApi.Delete, Unit>(
    template = OrgRoleApi.Delete::class.template(),
) {
  override suspend fun endpoint(call: ApplicationCall): OrgRoleApi.Delete =
      OrgRoleApi.Delete(orgRoleGuid = call.getParam(UUID::class, "orgRoleGuid"))

  override suspend fun Handler.handle(endpoint: OrgRoleApi.Delete) {
    auth {
      val orgRole = orgRoleService[endpoint.orgRoleGuid] ?: throw OrgRoleNotFound()
      AuthOrgMember(orgRole.orgGuid, permission = OrgPermission.MANAGE_ORG_ROLES)
    }
    orgRoleService.delete(endpoint.orgRoleGuid)
  }
}
