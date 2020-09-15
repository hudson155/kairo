package io.limberapp.web.state.state.orgRoleMemberships

import io.limberapp.common.types.UUID
import io.limberapp.backend.module.auth.rep.org.OrgRoleMembershipRep

internal interface OrgRoleMembershipsMutator {
  suspend fun post(orgRoleGuid: UUID, rep: OrgRoleMembershipRep.Creation): Outcome<Unit>

  suspend fun delete(orgRoleGuid: UUID, accountGuid: UUID): Outcome<Unit>
}
