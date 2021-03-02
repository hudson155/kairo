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

internal class GetOrgRoleMembershipsByOrgRoleGuidTest(
    engine: TestApplicationEngine,
    server: Server<*>,
) : IntegrationTest(engine, server) {
  @Test
  fun `role does not exist`() {
    val orgRoleGuid = UUID.randomUUID()

    test(expectError = OrgRoleNotFound().unprocessable()) {
      orgRoleMembershipClient(OrgRoleMembershipApi.GetByOrgRoleGuid(orgRoleGuid))
    }
  }

  @Test
  fun `role exists, no memberships`() {
    val orgGuid = UUID.randomUUID()

    val orgRoleRep = OrgRoleRepFixtures.adminFixture.complete(this, orgGuid, 0)
    setup { orgRoleClient(OrgRoleApi.Post(OrgRoleRepFixtures.adminFixture.creation(orgGuid))) }

    test(expectResult = emptySet()) {
      orgRoleMembershipClient(OrgRoleMembershipApi.GetByOrgRoleGuid(orgRoleRep.guid))
    }
  }

  @Test
  fun `role exists, 2 memberships`() {
    val orgGuid = UUID.randomUUID()
    val user0Guid = UUID.randomUUID()
    val user1Guid = UUID.randomUUID()

    val orgRoleRep = OrgRoleRepFixtures.adminFixture.complete(this, orgGuid, 0)
    setup { orgRoleClient(OrgRoleApi.Post(OrgRoleRepFixtures.adminFixture.creation(orgGuid))) }

    val orgRoleMembership0Rep = OrgRoleMembershipRepFixtures.fixture.complete(this, user0Guid)
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

    test(expectResult = setOf(orgRoleMembership0Rep, orgRoleMembership1Rep)) {
      orgRoleMembershipClient(OrgRoleMembershipApi.GetByOrgRoleGuid(orgRoleRep.guid))
    }
  }
}
