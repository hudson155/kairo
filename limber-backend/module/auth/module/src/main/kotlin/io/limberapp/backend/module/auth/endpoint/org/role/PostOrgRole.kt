package io.limberapp.backend.module.auth.endpoint.org.role

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.authorization.AuthFeatureMember
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.auth.api.org.OrgRoleApi
import io.limberapp.backend.module.auth.mapper.org.OrgRoleMapper
import io.limberapp.backend.module.auth.rep.org.OrgRoleRep
import io.limberapp.backend.module.auth.service.org.OrgRoleService
import io.limberapp.common.restInterface.template
import io.limberapp.permissions.orgPermissions.OrgPermission
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
    auth { AuthFeatureMember(command.orgGuid, permission = OrgPermission.MANAGE_ORG_ROLES) }
    val orgRole = orgRoleService.create(orgRoleMapper.model(command.orgGuid, rep))
    return orgRoleMapper.completeRep(orgRole)
  }
}
