package io.limberapp.endpoint.org

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.api.org.OrgRoleApi
import io.limberapp.exception.org.OrgRoleNameIsNotUnique
import io.limberapp.rep.org.OrgRoleRepFixtures
import io.limberapp.server.Server
import io.limberapp.testing.integration.IntegrationTest
import org.junit.jupiter.api.Test
import java.util.UUID

internal class PostOrgRoleTest(
    engine: TestApplicationEngine,
    server: Server<*>,
) : IntegrationTest(engine, server) {
  @Test
  fun `duplicate role name`() {
    val orgGuid = UUID.randomUUID()

    val adminOrgRoleRep = OrgRoleRepFixtures.adminFixture.complete(this, orgGuid, 0)
    setup { orgRoleClient(OrgRoleApi.Post(OrgRoleRepFixtures.adminFixture.creation(orgGuid))) }

    test(expectError = OrgRoleNameIsNotUnique()) {
      orgRoleClient(OrgRoleApi.Post(
          rep = OrgRoleRepFixtures.memberFixture.creation(orgGuid)
              .copy(name = adminOrgRoleRep.name),
      ))
    }

    test(expectResult = setOf(adminOrgRoleRep)) { orgRoleClient(OrgRoleApi.GetByOrgGuid(orgGuid)) }
  }

  @Test
  fun `happy path (multiple)`() {
    val orgGuid = UUID.randomUUID()

    val adminOrgRoleRep = OrgRoleRepFixtures.adminFixture.complete(this, orgGuid, 0)
    test(expectResult = adminOrgRoleRep) {
      orgRoleClient(OrgRoleApi.Post(OrgRoleRepFixtures.adminFixture.creation(orgGuid)))
    }

    val memberOrgRoleRep = OrgRoleRepFixtures.memberFixture.complete(this, orgGuid, 1)
    test(expectResult = memberOrgRoleRep) {
      orgRoleClient(OrgRoleApi.Post(OrgRoleRepFixtures.memberFixture.creation(orgGuid)))
    }

    test(expectResult = setOf(adminOrgRoleRep, memberOrgRoleRep)) {
      orgRoleClient(OrgRoleApi.GetByOrgGuid(orgGuid))
    }
  }
}
