package io.limberapp.rep.org

import io.limberapp.permissions.org.OrgPermission
import io.limberapp.permissions.org.OrgPermissions
import io.limberapp.testing.integration.IntegrationTest
import java.util.UUID

internal object OrgRoleRepFixtures {
  data class Fixture(
      val creation: (orgGuid: UUID) -> OrgRoleRep.Creation,
      val complete: IntegrationTest.(orgGuid: UUID, idSeed: Int) -> OrgRoleRep.Complete,
  )

  val adminFixture: Fixture = Fixture({ orgGuid ->
    OrgRoleRep.Creation(
        orgGuid = orgGuid,
        name = "Admins",
        permissions = OrgPermissions(setOf(OrgPermission.MANAGE_ORG_ROLES)),
        isDefault = false,
    )
  }, { orgGuid, idSeed ->
    OrgRoleRep.Complete(
        guid = guids[idSeed],
        orgGuid = orgGuid,
        name = "Admins",
        slug = "admins",
        permissions = OrgPermissions(setOf(OrgPermission.MANAGE_ORG_ROLES)),
        isDefault = false,
        memberCount = 0,
    )
  })

  val maintainerFixture: Fixture = Fixture({ orgGuid ->
    OrgRoleRep.Creation(
        orgGuid = orgGuid,
        name = "Maintainer",
        permissions = OrgPermissions(setOf(OrgPermission.MANAGE_ORG_ROLES)),
        isDefault = false,
    )
  }, { orgGuid, idSeed ->
    OrgRoleRep.Complete(
        guid = guids[idSeed],
        orgGuid = orgGuid,
        name = "Maintainer",
        slug = "maintainer",
        permissions = OrgPermissions(setOf(OrgPermission.MANAGE_ORG_ROLES)),
        isDefault = false,
        memberCount = 0,
    )
  })

  val memberFixture: Fixture = Fixture({ orgGuid ->
    OrgRoleRep.Creation(
        orgGuid = orgGuid,
        name = "Members",
        permissions = OrgPermissions(setOf(OrgPermission.MODIFY_OWN_METADATA)),
        isDefault = true,
    )
  }, { orgGuid, idSeed ->
    OrgRoleRep.Complete(
        guid = guids[idSeed],
        orgGuid = orgGuid,
        name = "Members",
        slug = "members",
        permissions = OrgPermissions(setOf(OrgPermission.MODIFY_OWN_METADATA)),
        isDefault = true,
        memberCount = 0,
    )
  })
}
