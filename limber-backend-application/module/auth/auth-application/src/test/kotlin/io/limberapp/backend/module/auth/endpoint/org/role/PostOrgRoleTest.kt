package io.limberapp.backend.module.auth.endpoint.org.role

import io.limberapp.backend.module.auth.api.org.role.OrgRoleApi
import io.limberapp.backend.module.auth.exception.org.OrgRoleNameIsNotUnique
import io.limberapp.backend.module.auth.rep.org.OrgRoleRep
import io.limberapp.backend.module.auth.testing.ResourceTest
import io.limberapp.backend.module.auth.testing.fixtures.org.OrgRoleRepFixtures
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

internal class PostOrgRoleTest : ResourceTest() {
  @Test
  fun duplicateName() {
    val orgGuid = UUID.randomUUID()

    val adminOrgRoleRep = OrgRoleRepFixtures.adminFixture.complete(this, 0)
    piperTest.setup(OrgRoleApi.Post(orgGuid, OrgRoleRepFixtures.adminFixture.creation()))

    piperTest.test(
      endpoint = OrgRoleApi.Post(
        orgGuid = orgGuid,
        rep = OrgRoleRepFixtures.memberFixture.creation().copy(name = adminOrgRoleRep.name)
      ),
      expectedException = OrgRoleNameIsNotUnique()
    )

    piperTest.test(OrgRoleApi.GetByOrgGuid(orgGuid)) {
      val actual = json.parseSet<OrgRoleRep.Complete>(response.content!!)
      assertEquals(setOf(adminOrgRoleRep), actual)
    }
  }

  @Test
  fun happyPath() {
    val orgGuid = UUID.randomUUID()

    val orgRoleRep = OrgRoleRepFixtures.adminFixture.complete(this, 0)
    piperTest.test(OrgRoleApi.Post(orgGuid, OrgRoleRepFixtures.adminFixture.creation())) {
      val actual = json.parse<OrgRoleRep.Complete>(response.content!!)
      assertEquals(orgRoleRep, actual)
    }

    piperTest.test(OrgRoleApi.GetByOrgGuid(orgGuid)) {
      val actual = json.parseSet<OrgRoleRep.Complete>(response.content!!)
      assertEquals(setOf(orgRoleRep), actual)
    }
  }
}
