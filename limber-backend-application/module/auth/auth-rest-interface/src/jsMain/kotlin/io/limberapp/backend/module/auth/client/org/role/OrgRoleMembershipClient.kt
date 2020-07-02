package io.limberapp.backend.module.auth.client.org.role

import io.limberapp.backend.module.auth.api.org.role.OrgRoleMembershipApi
import io.limberapp.backend.module.auth.rep.org.OrgRoleMembershipRep

interface OrgRoleMembershipClient {
  suspend operator fun invoke(endpoint: OrgRoleMembershipApi.Post): Result<OrgRoleMembershipRep.Complete>

  suspend operator fun invoke(
    endpoint: OrgRoleMembershipApi.GetByOrgRoleGuid
  ): Result<Set<OrgRoleMembershipRep.Complete>>

  suspend operator fun invoke(endpoint: OrgRoleMembershipApi.Delete): Result<Unit>
}
