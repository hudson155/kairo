package io.limberapp.endpoint.org

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.api.org.OrgRoleApi
import io.limberapp.rep.org.OrgRoleRepFixtures
import io.limberapp.server.Server
import io.limberapp.testing.integration.IntegrationTest
import org.junit.jupiter.api.Test
import java.util.UUID

internal class DeleteOrgRoleTest(
    engine: TestApplicationEngine,
    server: Server<*>,
) : IntegrationTest(engine, server) {
  @Test
  fun `role does not exist`() {
    val orgRoleGuid = UUID.randomUUID()

    test(expectResult = null) { orgRoleClient(OrgRoleApi.Delete(orgRoleGuid)) }
  }

  @Test
  fun `role exists`() {
    val orgGuid = UUID.randomUUID()

    val adminOrgRoleRep = OrgRoleRepFixtures.adminFixture.complete(this, orgGuid, 0)
    setup { orgRoleClient(OrgRoleApi.Post(OrgRoleRepFixtures.adminFixture.creation(orgGuid))) }

    val memberOrgRoleRep = OrgRoleRepFixtures.memberFixture.complete(this, orgGuid, 1)
    setup { orgRoleClient(OrgRoleApi.Post(OrgRoleRepFixtures.memberFixture.creation(orgGuid))) }

    test(expectResult = Unit) { orgRoleClient(OrgRoleApi.Delete(memberOrgRoleRep.guid)) }

    test(expectResult = setOf(adminOrgRoleRep)) { orgRoleClient(OrgRoleApi.GetByOrgGuid(orgGuid)) }
  }
}
