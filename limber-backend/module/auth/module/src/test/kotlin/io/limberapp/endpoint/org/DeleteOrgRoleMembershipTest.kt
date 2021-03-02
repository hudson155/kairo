package io.limberapp.endpoint.org

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.api.org.OrgRoleApi
import io.limberapp.api.org.OrgRoleMembershipApi
import io.limberapp.exception.org.OrgRoleNotFound
import io.limberapp.exception.unprocessable
import io.limberapp.rep.org.OrgRoleMembershipRepFixtures
import io.limberapp.rep.org.OrgRoleRepFixtures
import io.limberapp.server.Server
import io.limberapp.testing.integration.IntegrationTest
import org.junit.jupiter.api.Test
import java.util.UUID

internal class DeleteOrgRoleMembershipTest(
    engine: TestApplicationEngine,
    server: Server<*>,
) : IntegrationTest(engine, server) {
  @Test
  fun `role does not exist`() {
    val orgRoleGuid = UUID.randomUUID()
    val userGuid = UUID.randomUUID()

    test(expectError = OrgRoleNotFound().unprocessable()) {
      orgRoleMembershipClient(OrgRoleMembershipApi.Delete(orgRoleGuid, userGuid))
    }
  }

  @Test
  fun `membership does not exist`() {
    val orgGuid = UUID.randomUUID()
    val userGuid = UUID.randomUUID()

    val orgRoleRep = OrgRoleRepFixtures.adminFixture.complete(this, orgGuid, 0)
    setup { orgRoleClient(OrgRoleApi.Post(OrgRoleRepFixtures.adminFixture.creation(orgGuid))) }

    test(expectResult = null) {
      orgRoleMembershipClient(OrgRoleMembershipApi.Delete(orgRoleRep.guid, userGuid))
    }
  }

  @Test
  fun `happy path`() {
    val orgGuid = UUID.randomUUID()
    val user0Guid = UUID.randomUUID()
    val user1Guid = UUID.randomUUID()

    val orgRoleRep = OrgRoleRepFixtures.adminFixture.complete(this, orgGuid, 0)
    setup { orgRoleClient(OrgRoleApi.Post(OrgRoleRepFixtures.adminFixture.creation(orgGuid))) }

    setup {
      orgRoleMembershipClient(OrgRoleMembershipApi.Post(
          orgRoleGuid = orgRoleRep.guid,
          rep = OrgRoleMembershipRepFixtures.fixture.creation(user0Guid),
      ))
    }

    val orgRoleMembership1Rep = OrgRoleMembershipRepFixtures.fixture.complete(this, user1Guid)
    setup {
      orgRoleMembershipClient(OrgRoleMembershipApi.Post(
          orgRoleGuid = orgRoleRep.guid,
          rep = OrgRoleMembershipRepFixtures.fixture.creation(user1Guid),
      ))
    }

    test(expectResult = Unit) {
      orgRoleMembershipClient(OrgRoleMembershipApi.Delete(orgRoleRep.guid, user0Guid))
    }

    test(expectResult = setOf(orgRoleMembership1Rep)) {
      orgRoleMembershipClient(OrgRoleMembershipApi.GetByOrgRoleGuid(orgRoleRep.guid))
    }
  }
}
