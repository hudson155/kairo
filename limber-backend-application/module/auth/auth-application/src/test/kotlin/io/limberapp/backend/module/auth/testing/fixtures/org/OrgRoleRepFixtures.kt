package io.limberapp.backend.module.auth.testing.fixtures.org

import io.limberapp.backend.authorization.permissions.orgPermissions.OrgPermission
import io.limberapp.backend.authorization.permissions.orgPermissions.OrgPermissions
import io.limberapp.backend.module.auth.rep.org.OrgRoleRep
import io.limberapp.backend.module.auth.testing.ResourceTest
import java.time.LocalDateTime

internal object OrgRoleRepFixtures {
  data class Fixture(
    val creation: () -> OrgRoleRep.Creation,
    val complete: ResourceTest.(idSeed: Int) -> OrgRoleRep.Complete
  )

  val adminFixture = Fixture(
    {
      OrgRoleRep.Creation("Admin", OrgPermissions(setOf(OrgPermission.MANAGE_ORG_ROLES)))
    },
    { idSeed ->
      OrgRoleRep.Complete(
        guid = deterministicUuidGenerator[idSeed],
        createdDate = LocalDateTime.now(fixedClock),
        name = "Admin",
        permissions = OrgPermissions(setOf(OrgPermission.MANAGE_ORG_ROLES)),
        memberCount = 0
      )
    }
  )

  val maintainerFixture = Fixture(
    {
      OrgRoleRep.Creation("Maintainer", OrgPermissions(setOf(OrgPermission.MANAGE_ORG_ROLES)))
    },
    { idSeed ->
      OrgRoleRep.Complete(
        guid = deterministicUuidGenerator[idSeed],
        createdDate = LocalDateTime.now(fixedClock),
        name = "Maintainer",
        permissions = OrgPermissions(setOf(OrgPermission.MANAGE_ORG_ROLES)),
        memberCount = 0
      )
    }
  )

  val memberFixture = Fixture(
    {
      OrgRoleRep.Creation("Member", OrgPermissions.none())
    },
    { idSeed ->
      OrgRoleRep.Complete(
        guid = deterministicUuidGenerator[idSeed],
        createdDate = LocalDateTime.now(fixedClock),
        name = "Member",
        permissions = OrgPermissions.none(),
        memberCount = 0
      )
    }
  )
}
