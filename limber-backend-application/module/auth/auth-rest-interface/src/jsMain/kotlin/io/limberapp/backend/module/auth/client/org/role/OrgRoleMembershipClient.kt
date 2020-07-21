package io.limberapp.backend.module.auth.client.org.role

import com.piperframework.util.Outcome
import io.limberapp.backend.module.auth.api.org.role.OrgRoleMembershipApi
import io.limberapp.backend.module.auth.rep.org.OrgRoleMembershipRep

interface OrgRoleMembershipClient {
  suspend operator fun invoke(endpoint: OrgRoleMembershipApi.Post): Outcome<OrgRoleMembershipRep.Complete>

  suspend operator fun invoke(
    endpoint: OrgRoleMembershipApi.GetByOrgRoleGuid
  ): Outcome<Set<OrgRoleMembershipRep.Complete>>

  suspend operator fun invoke(endpoint: OrgRoleMembershipApi.Delete): Outcome<Unit>
}
