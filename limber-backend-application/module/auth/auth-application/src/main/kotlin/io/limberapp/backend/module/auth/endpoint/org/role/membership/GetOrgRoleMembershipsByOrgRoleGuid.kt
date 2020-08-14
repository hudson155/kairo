package io.limberapp.backend.module.auth.endpoint.org.role.membership

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.restInterface.template
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.authorization.permissions.orgPermissions.OrgPermission
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.auth.api.org.role.OrgRoleMembershipApi
import io.limberapp.backend.module.auth.mapper.org.OrgRoleMembershipMapper
import io.limberapp.backend.module.auth.rep.org.OrgRoleMembershipRep
import io.limberapp.backend.module.auth.service.org.OrgRoleMembershipService
import java.util.*

internal class GetOrgRoleMembershipsByOrgRoleGuid @Inject constructor(
  application: Application,
  servingConfig: ServingConfig,
  private val orgRoleMembershipService: OrgRoleMembershipService,
  private val orgRoleMembershipMapper: OrgRoleMembershipMapper,
) : LimberApiEndpoint<OrgRoleMembershipApi.GetByOrgRoleGuid, Set<OrgRoleMembershipRep.Complete>>(
  application = application,
  pathPrefix = servingConfig.apiPathPrefix,
  endpointTemplate = OrgRoleMembershipApi.GetByOrgRoleGuid::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = OrgRoleMembershipApi.GetByOrgRoleGuid(
    orgGuid = call.parameters.getAsType(UUID::class, "orgGuid"),
    orgRoleGuid = call.parameters.getAsType(UUID::class, "orgRoleGuid")
  )

  override suspend fun Handler.handle(
    command: OrgRoleMembershipApi.GetByOrgRoleGuid,
  ): Set<OrgRoleMembershipRep.Complete> {
    Authorization.OrgMemberWithPermission(command.orgGuid, OrgPermission.MANAGE_ORG_ROLE_MEMBERSHIPS).authorize()
    val orgRoleMemberships = orgRoleMembershipService.findAsSet {
      orgGuid(command.orgGuid)
      orgRoleGuid(command.orgRoleGuid)
    }
    return orgRoleMemberships.map { orgRoleMembershipMapper.completeRep(it) }.toSet()
  }
}
