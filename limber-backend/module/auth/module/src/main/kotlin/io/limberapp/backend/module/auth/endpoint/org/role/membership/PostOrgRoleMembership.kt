package io.limberapp.backend.module.auth.endpoint.org.role.membership

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.authorization.AuthFeatureMember
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.auth.api.org.OrgRoleMembershipApi
import io.limberapp.backend.module.auth.mapper.org.OrgRoleMembershipMapper
import io.limberapp.backend.module.auth.rep.org.OrgRoleMembershipRep
import io.limberapp.backend.module.auth.service.org.OrgRoleMembershipService
import io.limberapp.common.permissions.orgPermissions.OrgPermission
import io.limberapp.common.restInterface.template
import java.util.*

internal class PostOrgRoleMembership @Inject constructor(
    application: Application,
    private val orgRoleMembershipService: OrgRoleMembershipService,
    private val orgRoleMembershipMapper: OrgRoleMembershipMapper,
) : LimberApiEndpoint<OrgRoleMembershipApi.Post, OrgRoleMembershipRep.Complete>(
    application = application,
    endpointTemplate = OrgRoleMembershipApi.Post::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = OrgRoleMembershipApi.Post(
      orgGuid = call.parameters.getAsType(UUID::class, "orgGuid"),
      orgRoleGuid = call.parameters.getAsType(UUID::class, "orgRoleGuid"),
      rep = call.getAndValidateBody()
  )

  override suspend fun Handler.handle(command: OrgRoleMembershipApi.Post): OrgRoleMembershipRep.Complete {
    val rep = command.rep.required()
    auth { AuthFeatureMember(command.orgGuid, permission = OrgPermission.MANAGE_ORG_ROLE_MEMBERSHIPS) }
    val orgRoleMembership = orgRoleMembershipService.create(
        orgGuid = command.orgGuid,
        model = orgRoleMembershipMapper.model(command.orgRoleGuid, rep)
    )
    return orgRoleMembershipMapper.completeRep(orgRoleMembership)
  }
}
