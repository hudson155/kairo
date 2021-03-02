package io.limberapp.endpoint.org

import com.google.inject.Inject
import io.ktor.application.ApplicationCall
import io.limberapp.api.org.OrgRoleApi
import io.limberapp.auth.auth.AuthOrgMember
import io.limberapp.exception.org.OrgRoleNotFound
import io.limberapp.mapper.org.OrgRoleMapper
import io.limberapp.permissions.org.OrgPermission
import io.limberapp.rep.org.OrgRoleRep
import io.limberapp.restInterface.EndpointHandler
import io.limberapp.restInterface.template
import io.limberapp.service.org.OrgRoleService
import java.util.UUID

internal class PatchOrgRole @Inject constructor(
    private val orgRoleService: OrgRoleService,
    private val orgRoleMapper: OrgRoleMapper,
) : EndpointHandler<OrgRoleApi.Patch, OrgRoleRep.Complete>(
    template = OrgRoleApi.Patch::class.template(),
) {
  override suspend fun endpoint(call: ApplicationCall): OrgRoleApi.Patch =
      OrgRoleApi.Patch(
          orgRoleGuid = call.getParam(UUID::class, "orgRoleGuid"),
          rep = call.getAndValidateBody(),
      )

  override suspend fun Handler.handle(endpoint: OrgRoleApi.Patch): OrgRoleRep.Complete {
    val rep = endpoint.rep.required()
    auth {
      val orgRole = orgRoleService[endpoint.orgRoleGuid] ?: throw OrgRoleNotFound()
      AuthOrgMember(orgRole.orgGuid, permission = OrgPermission.MANAGE_ORG_ROLES)
    }
    val orgRole = orgRoleService.update(endpoint.orgRoleGuid, orgRoleMapper.update(rep))
    return orgRoleMapper.completeRep(orgRole)
  }
}
