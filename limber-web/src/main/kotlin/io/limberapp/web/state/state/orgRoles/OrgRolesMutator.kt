package io.limberapp.web.state.state.orgRoles

import com.piperframework.types.UUID
import com.piperframework.util.Outcome
import io.limberapp.backend.module.auth.rep.org.OrgRoleRep

internal interface OrgRolesMutator {
  suspend fun post(rep: OrgRoleRep.Creation): Outcome<Unit>

  suspend fun patch(orgRoleGuid: UUID, rep: OrgRoleRep.Update): Outcome<Unit>

  fun localMemberCountChanged(orgRoleGuid: UUID, by: Int)

  suspend fun delete(orgRoleGuid: UUID): Outcome<Unit>
}
