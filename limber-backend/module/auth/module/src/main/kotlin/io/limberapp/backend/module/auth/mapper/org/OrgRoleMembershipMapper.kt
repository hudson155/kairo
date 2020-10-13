package io.limberapp.backend.module.auth.mapper.org

import com.google.inject.Inject
import io.limberapp.backend.module.auth.model.org.OrgRoleMembershipModel
import io.limberapp.backend.module.auth.rep.org.OrgRoleMembershipRep
import java.time.Clock
import java.time.LocalDateTime
import java.util.*

internal class OrgRoleMembershipMapper @Inject constructor(
    private val clock: Clock,
) {
  fun model(orgRoleGuid: UUID, rep: OrgRoleMembershipRep.Creation) = OrgRoleMembershipModel(
      createdDate = LocalDateTime.now(clock),
      orgRoleGuid = orgRoleGuid,
      accountGuid = rep.accountGuid
  )

  fun completeRep(model: OrgRoleMembershipModel) = OrgRoleMembershipRep.Complete(
      createdDate = model.createdDate,
      accountGuid = model.accountGuid
  )
}
