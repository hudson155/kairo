package io.limberapp.mapper.org

import com.google.inject.Inject
import io.limberapp.model.org.OrgRoleMembershipModel
import io.limberapp.rep.org.OrgRoleMembershipRep
import java.time.Clock
import java.time.ZonedDateTime
import java.util.UUID

internal class OrgRoleMembershipMapper @Inject constructor(
    private val clock: Clock,
) {
  fun model(orgRoleGuid: UUID, rep: OrgRoleMembershipRep.Creation): OrgRoleMembershipModel =
      OrgRoleMembershipModel(
          createdDate = ZonedDateTime.now(clock),
          orgRoleGuid = orgRoleGuid,
          userGuid = rep.userGuid,
      )

  fun completeRep(model: OrgRoleMembershipModel): OrgRoleMembershipRep.Complete =
      OrgRoleMembershipRep.Complete(userGuid = model.userGuid)
}
