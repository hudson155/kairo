package io.limberapp.backend.module.auth.endpoint.org.role

import com.google.inject.Inject
import com.piperframework.restInterface.template
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.authorization.permissions.orgPermissions.OrgPermission
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.auth.api.org.role.OrgRoleApi
import io.limberapp.backend.module.auth.mapper.org.OrgRoleMapper
import io.limberapp.backend.module.auth.rep.org.OrgRoleRep
import io.limberapp.backend.module.auth.service.org.OrgRoleService
import java.util.*

internal class PostOrgRole @Inject constructor(
  application: Application,
  private val orgRoleService: OrgRoleService,
  private val orgRoleMapper: OrgRoleMapper,
) : LimberApiEndpoint<OrgRoleApi.Post, OrgRoleRep.Complete>(
  application = application,
  endpointTemplate = OrgRoleApi.Post::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = OrgRoleApi.Post(
    orgGuid = call.parameters.getAsType(UUID::class, "orgGuid"),
    rep = call.getAndValidateBody()
  )

  override suspend fun Handler.handle(command: OrgRoleApi.Post): OrgRoleRep.Complete {
    val rep = command.rep.required()
    Authorization.OrgMemberWithPermission(command.orgGuid, OrgPermission.MANAGE_ORG_ROLES).authorize()
    val orgRole = orgRoleService.create(orgRoleMapper.model(command.orgGuid, rep))
    return orgRoleMapper.completeRep(orgRole)
  }
}
