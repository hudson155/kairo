package io.limberapp.backend.module.auth.testing.fixtures.org

import io.limberapp.backend.authorization.permissions.orgPermissions.OrgPermission
import io.limberapp.backend.authorization.permissions.orgPermissions.OrgPermissions
import io.limberapp.backend.module.auth.rep.org.OrgRoleRep
import io.limberapp.backend.module.auth.testing.IntegrationTest
import java.time.LocalDateTime

internal object OrgRoleRepFixtures {
  data class Fixture(
    val creation: () -> OrgRoleRep.Creation,
    val complete: IntegrationTest.(idSeed: Int) -> OrgRoleRep.Complete,
  )

  val adminFixture = Fixture(
    {
      OrgRoleRep.Creation(
        name = "Admins",
        permissions = OrgPermissions(setOf(OrgPermission.MANAGE_ORG_ROLES)),
        isDefault = false,
      )
    },
    { idSeed ->
      OrgRoleRep.Complete(
        guid = uuidGenerator[idSeed],
        createdDate = LocalDateTime.now(clock),
        name = "Admins",
        permissions = OrgPermissions(setOf(OrgPermission.MANAGE_ORG_ROLES)),
        isDefault = false,
        memberCount = 0,
      )
    }
  )

  val maintainerFixture = Fixture(
    {
      OrgRoleRep.Creation(
        name = "Maintainer",
        permissions = OrgPermissions(setOf(OrgPermission.MANAGE_ORG_ROLES)),
        isDefault = false,
      )
    },
    { idSeed ->
      OrgRoleRep.Complete(
        guid = uuidGenerator[idSeed],
        createdDate = LocalDateTime.now(clock),
        name = "Maintainer",
        permissions = OrgPermissions(setOf(OrgPermission.MANAGE_ORG_ROLES)),
        isDefault = false,
        memberCount = 0,
      )
    }
  )

  val memberFixture = Fixture(
    {
      OrgRoleRep.Creation(
        name = "Members",
        permissions = OrgPermissions(setOf(OrgPermission.MODIFY_OWN_METADATA)),
        isDefault = true,
      )
    },
    { idSeed ->
      OrgRoleRep.Complete(
        guid = uuidGenerator[idSeed],
        createdDate = LocalDateTime.now(clock),
        name = "Members",
        permissions = OrgPermissions(setOf(OrgPermission.MODIFY_OWN_METADATA)),
        isDefault = true,
        memberCount = 0,
      )
    }
  )
}
