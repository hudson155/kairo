package io.limberapp.backend.module.auth.client.org.role

import io.limberapp.backend.module.auth.api.org.role.OrgRoleMembershipApi
import io.limberapp.backend.module.auth.rep.org.OrgRoleMembershipRep
import io.limberapp.common.util.Outcome

interface OrgRoleMembershipClient {
  suspend operator fun invoke(endpoint: OrgRoleMembershipApi.Post): Outcome<OrgRoleMembershipRep.Complete>

  suspend operator fun invoke(
    endpoint: OrgRoleMembershipApi.GetByOrgRoleGuid,
  ): Outcome<Set<OrgRoleMembershipRep.Complete>>

  suspend operator fun invoke(endpoint: OrgRoleMembershipApi.Delete): Outcome<Unit>
}
