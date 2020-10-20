package io.limberapp.backend.module.auth.testing.fixtures.org

import io.limberapp.backend.module.auth.rep.org.OrgRoleRep
import io.limberapp.backend.module.auth.testing.IntegrationTest
import io.limberapp.common.util.time.inUTC
import io.limberapp.permissions.orgPermissions.OrgPermission
import io.limberapp.permissions.orgPermissions.OrgPermissions
import java.time.ZonedDateTime

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
            createdDate = ZonedDateTime.now(clock).inUTC(),
            name = "Admins",
            slug = "admins",
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
            createdDate = ZonedDateTime.now(clock).inUTC(),
            name = "Maintainer",
            slug = "maintainer",
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
            createdDate = ZonedDateTime.now(clock).inUTC(),
            name = "Members",
            slug = "members",
            permissions = OrgPermissions(setOf(OrgPermission.MODIFY_OWN_METADATA)),
            isDefault = true,
            memberCount = 0,
        )
      }
  )
}
