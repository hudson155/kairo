package io.limberapp.backend.module.auth.endpoint.org.role

import io.limberapp.backend.authorization.permissions.orgPermissions.OrgPermissions
import io.limberapp.backend.module.auth.api.org.role.OrgRoleApi
import io.limberapp.backend.module.auth.exception.org.OrgRoleNameIsNotUnique
import io.limberapp.backend.module.auth.exception.org.OrgRoleNotFound
import io.limberapp.backend.module.auth.rep.org.OrgRoleRep
import io.limberapp.backend.module.auth.testing.ResourceTest
import io.limberapp.backend.module.auth.testing.fixtures.org.OrgRoleRepFixtures
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

internal class PatchOrgRoleTest : ResourceTest() {
  @Test
  fun orgRoleDoesNotExist() {
    val orgGuid = UUID.randomUUID()
    val orgRoleGuid = UUID.randomUUID()

    val orgRoleUpdateRep = OrgRoleRep.Update(permissions = OrgPermissions.fromBitString("110"))
    piperTest.test(
      endpoint = OrgRoleApi.Patch(orgGuid, orgRoleGuid, orgRoleUpdateRep),
      expectedException = OrgRoleNotFound()
    )
  }

  @Test
  fun duplicateName() {
    val orgGuid = UUID.randomUUID()

    val adminOrgRoleRep = OrgRoleRepFixtures.adminFixture.complete(this, 0)
    piperTest.setup(OrgRoleApi.Post(orgGuid, OrgRoleRepFixtures.adminFixture.creation()))

    val memberOrgRoleRep = OrgRoleRepFixtures.memberFixture.complete(this, 1)
    piperTest.setup(OrgRoleApi.Post(orgGuid, OrgRoleRepFixtures.memberFixture.creation()))

    val orgRoleUpdateRep = OrgRoleRep.Update(name = adminOrgRoleRep.name)
    piperTest.test(
      endpoint = OrgRoleApi.Patch(orgGuid, memberOrgRoleRep.guid, orgRoleUpdateRep),
      expectedException = OrgRoleNameIsNotUnique()
    )

    piperTest.test(OrgRoleApi.GetByOrgGuid(orgGuid)) {
      val actual = json.parseSet<OrgRoleRep.Complete>(response.content!!)
      assertEquals(setOf(adminOrgRoleRep, memberOrgRoleRep), actual)
    }
  }

  @Test
  fun happyPath() {
    val orgGuid = UUID.randomUUID()

    var orgRoleRep = OrgRoleRepFixtures.adminFixture.complete(this, 0)
    piperTest.setup(OrgRoleApi.Post(orgGuid, OrgRoleRepFixtures.adminFixture.creation()))

    val orgRoleUpdateRep = OrgRoleRep.Update(permissions = OrgPermissions.fromBitString("110"))
    orgRoleRep = orgRoleRep.copy(permissions = orgRoleUpdateRep.permissions!!)
    piperTest.test(OrgRoleApi.Patch(orgGuid, orgRoleRep.guid, orgRoleUpdateRep)) {
      val actual = json.parse<OrgRoleRep.Complete>(response.content!!)
      assertEquals(orgRoleRep, actual)
    }

    piperTest.test(OrgRoleApi.GetByOrgGuid(orgGuid)) {
      val actual = json.parseSet<OrgRoleRep.Complete>(response.content!!)
      assertEquals(setOf(orgRoleRep), actual)
    }
  }
}
