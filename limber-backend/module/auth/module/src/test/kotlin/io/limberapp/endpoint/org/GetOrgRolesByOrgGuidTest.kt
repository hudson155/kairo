package io.limberapp.endpoint.org

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.api.org.OrgRoleApi
import io.limberapp.rep.org.OrgRoleRepFixtures
import io.limberapp.server.Server
import io.limberapp.testing.integration.IntegrationTest
import org.junit.jupiter.api.Test
import java.util.UUID

internal class GetOrgRolesByOrgGuidTest(
    engine: TestApplicationEngine,
    server: Server<*>,
) : IntegrationTest(engine, server) {
  @Test
  fun `none exist`() {
    val orgGuid = UUID.randomUUID()

    test(expectResult = emptySet()) { orgRoleClient(OrgRoleApi.GetByOrgGuid(orgGuid)) }
  }

  @Test
  fun `multiple exisst`() {
    val orgGuid = UUID.randomUUID()

    val adminOrgRoleRep = OrgRoleRepFixtures.adminFixture.complete(this, orgGuid, 0)
    setup { orgRoleClient(OrgRoleApi.Post(OrgRoleRepFixtures.adminFixture.creation(orgGuid))) }

    val memberOrgRoleRep = OrgRoleRepFixtures.memberFixture.complete(this, orgGuid, 1)
    setup { orgRoleClient(OrgRoleApi.Post(OrgRoleRepFixtures.memberFixture.creation(orgGuid))) }

    test(expectResult = setOf(adminOrgRoleRep, memberOrgRoleRep)) {
      orgRoleClient(OrgRoleApi.GetByOrgGuid(orgGuid))
    }
  }
}
