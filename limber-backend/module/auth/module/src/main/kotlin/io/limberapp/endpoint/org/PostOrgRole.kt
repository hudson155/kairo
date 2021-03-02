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

internal class PostOrgRole @Inject constructor(
    private val orgRoleService: OrgRoleService,
    private val orgRoleMapper: OrgRoleMapper,
) : EndpointHandler<OrgRoleApi.Post, OrgRoleRep.Complete>(
    template = OrgRoleApi.Post::class.template(),
) {
  override suspend fun endpoint(call: ApplicationCall): OrgRoleApi.Post =
      OrgRoleApi.Post(rep = call.getAndValidateBody())

  override suspend fun Handler.handle(endpoint: OrgRoleApi.Post): OrgRoleRep.Complete {
    val rep = endpoint.rep.required()
    auth(AuthOrgMember(rep.orgGuid, permission = OrgPermission.MANAGE_ORG_ROLES))
    val orgRole = orgRoleService.create(orgRoleMapper.model(rep))
    return orgRoleMapper.completeRep(orgRole)
  }
}
