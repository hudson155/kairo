package io.limberapp.backend.module.auth.endpoint.org.role

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.auth.api.org.role.OrgRoleApi
import io.limberapp.backend.module.auth.rep.org.OrgRoleRep
import io.limberapp.backend.module.auth.testing.IntegrationTest
import io.limberapp.backend.module.auth.testing.fixtures.org.OrgRoleRepFixtures
import io.limberapp.common.LimberApplication
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class GetOrgRolesByOrgGuidTest(
  engine: TestApplicationEngine,
  limberServer: LimberApplication<*>,
) : IntegrationTest(engine, limberServer) {
  @Test
  fun happyPathNoneExist() {
    val orgGuid = UUID.randomUUID()

    test(OrgRoleApi.GetByOrgGuid(orgGuid)) {
      val actual = json.parseSet<OrgRoleRep.Complete>(responseContent)
      assertTrue(actual.isEmpty())
    }
  }

  @Test
  fun happyPathMultipleExist() {
    val orgGuid = UUID.randomUUID()

    val adminOrgRoleRep = OrgRoleRepFixtures.adminFixture.complete(this, 0)
    setup(OrgRoleApi.Post(orgGuid, OrgRoleRepFixtures.adminFixture.creation()))

    val memberOrgRoleRep = OrgRoleRepFixtures.memberFixture.complete(this, 1)
    setup(OrgRoleApi.Post(orgGuid, OrgRoleRepFixtures.memberFixture.creation()))

    test(OrgRoleApi.GetByOrgGuid(orgGuid)) {
      val actual = json.parseSet<OrgRoleRep.Complete>(responseContent)
      assertEquals(setOf(adminOrgRoleRep, memberOrgRoleRep), actual)
    }
  }
}
