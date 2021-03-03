package io.limberapp.rep.org

import io.limberapp.testing.integration.IntegrationTest
import java.util.UUID

internal object OrgRoleMembershipRepFixtures {
  data class Fixture(
      val creation: (userGuid: UUID) -> OrgRoleMembershipRep.Creation,
      val complete: IntegrationTest.(userGuid: UUID) -> OrgRoleMembershipRep.Complete,
  )

  val fixture: Fixture = Fixture({ userGuid ->
    OrgRoleMembershipRep.Creation(userGuid)
  }, { userGuid ->
    OrgRoleMembershipRep.Complete(userGuid = userGuid)
  })
}
