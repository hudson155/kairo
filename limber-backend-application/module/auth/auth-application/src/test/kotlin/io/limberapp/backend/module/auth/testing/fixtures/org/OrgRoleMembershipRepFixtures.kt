package io.limberapp.backend.module.auth.testing.fixtures.org

import io.limberapp.backend.module.auth.rep.org.OrgRoleMembershipRep
import io.limberapp.backend.module.auth.testing.ResourceTest
import java.time.LocalDateTime
import java.util.*

internal object OrgRoleMembershipRepFixtures {
  data class Fixture(
    val creation: (accountGuid: UUID) -> OrgRoleMembershipRep.Creation,
    val complete: ResourceTest.(accountGuid: UUID) -> OrgRoleMembershipRep.Complete,
  )

  // There's only 1 fixture here (it's parameterized), but it's still useful for code brevity and timestamp creation.
  val fixture = Fixture(
    { accountGuid ->
      OrgRoleMembershipRep.Creation(accountGuid)
    },
    { accountGuid ->
      OrgRoleMembershipRep.Complete(
        createdDate = LocalDateTime.now(fixedClock),
        accountGuid = accountGuid
      )
    }
  )
}
