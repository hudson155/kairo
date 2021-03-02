package io.limberapp.endpoint.org

import com.google.inject.Inject
import io.ktor.application.ApplicationCall
import io.limberapp.api.org.OrgRoleApi
import io.limberapp.auth.auth.AuthOrgMember
import io.limberapp.mapper.org.OrgRoleMapper
import io.limberapp.permissions.org.OrgPermission
import io.limberapp.rep.org.OrgRoleRep
import io.limberapp.restInterface.EndpointHandler
import io.limberapp.restInterface.template
import io.limberapp.service.org.OrgRoleService
import java.util.UUID

internal class GetOrgRolesByOrgGuid @Inject constructor(
    private val orgRoleService: OrgRoleService,
    private val orgRoleMapper: OrgRoleMapper,
) : EndpointHandler<OrgRoleApi.GetByOrgGuid, Set<OrgRoleRep.Complete>>(
    template = OrgRoleApi.GetByOrgGuid::class.template(),
) {
  override suspend fun endpoint(call: ApplicationCall): OrgRoleApi.GetByOrgGuid =
      OrgRoleApi.GetByOrgGuid(orgGuid = call.getParam(UUID::class, "orgGuid"))

  override suspend fun Handler.handle(endpoint: OrgRoleApi.GetByOrgGuid): Set<OrgRoleRep.Complete> {
    auth(AuthOrgMember(endpoint.orgGuid, permission = OrgPermission.MANAGE_ORG_ROLES))
    val orgRoles = orgRoleService.getByOrgGuid(endpoint.orgGuid)
    return orgRoles.map { orgRoleMapper.completeRep(it) }.toSet()
  }
}
