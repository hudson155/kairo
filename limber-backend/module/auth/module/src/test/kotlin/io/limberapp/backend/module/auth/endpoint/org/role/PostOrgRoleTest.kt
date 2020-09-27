package io.limberapp.backend.module.auth.endpoint.org.role

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.auth.api.org.role.OrgRoleApi
import io.limberapp.backend.module.auth.exception.org.OrgRoleNameIsNotUnique
import io.limberapp.backend.module.auth.rep.org.OrgRoleRep
import io.limberapp.backend.module.auth.testing.IntegrationTest
import io.limberapp.backend.module.auth.testing.fixtures.org.OrgRoleRepFixtures
import io.limberapp.common.LimberApplication
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

internal class PostOrgRoleTest(
  engine: TestApplicationEngine,
  limberServer: LimberApplication<*>,
) : IntegrationTest(engine, limberServer) {
  @Test
  fun duplicateName() {
    val orgGuid = UUID.randomUUID()

    val adminOrgRoleRep = OrgRoleRepFixtures.adminFixture.complete(this, 0)
    setup(OrgRoleApi.Post(orgGuid, OrgRoleRepFixtures.adminFixture.creation()))

    test(
      endpoint = OrgRoleApi.Post(
        orgGuid = orgGuid,
        rep = OrgRoleRepFixtures.memberFixture.creation().copy(name = adminOrgRoleRep.name)
      ),
      expectedException = OrgRoleNameIsNotUnique()
    )

    test(OrgRoleApi.GetByOrgGuid(orgGuid)) {
      val actual = json.parseSet<OrgRoleRep.Complete>(responseContent)
      assertEquals(setOf(adminOrgRoleRep), actual)
    }
  }

  @Test
  fun happyPath() {
    val orgGuid = UUID.randomUUID()

    val orgRoleRep = OrgRoleRepFixtures.adminFixture.complete(this, 0)
    test(OrgRoleApi.Post(orgGuid, OrgRoleRepFixtures.adminFixture.creation())) {
      val actual = json.parse<OrgRoleRep.Complete>(responseContent)
      assertEquals(orgRoleRep, actual)
    }

    test(OrgRoleApi.GetByOrgGuid(orgGuid)) {
      val actual = json.parseSet<OrgRoleRep.Complete>(responseContent)
      assertEquals(setOf(orgRoleRep), actual)
    }
  }
}
