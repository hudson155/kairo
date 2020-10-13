package io.limberapp.backend.module.auth.endpoint.org.role.membership

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.auth.api.org.role.OrgRoleApi
import io.limberapp.backend.module.auth.api.org.role.OrgRoleMembershipApi
import io.limberapp.backend.module.auth.testing.IntegrationTest
import io.limberapp.backend.module.auth.testing.fixtures.org.OrgRoleMembershipRepFixtures
import io.limberapp.backend.module.auth.testing.fixtures.org.OrgRoleRepFixtures
import io.limberapp.common.LimberApplication
import org.junit.jupiter.api.Test
import java.util.*

internal class GetOrgRoleMembershipsByOrgRoleGuidTest(
    engine: TestApplicationEngine,
    limberServer: LimberApplication<*>,
) : IntegrationTest(engine, limberServer) {
  @Test
  fun orgRoleDoesNotExist() {
    val orgGuid = UUID.randomUUID()
    val orgRoleGuid = UUID.randomUUID()

    // Create an org role anyways, to ensure that the error still happens when there is one.
    setup {
      orgRoleClient(OrgRoleApi.Post(orgGuid, OrgRoleRepFixtures.adminFixture.creation()))
    }

    test(expectResult = emptySet()) {
      orgRoleMembershipClient(OrgRoleMembershipApi.GetByOrgRoleGuid(orgGuid, orgRoleGuid))
    }
  }

  @Test
  fun happyPathTwo() {
    val orgGuid = UUID.randomUUID()
    val account0Guid = UUID.randomUUID()
    val account1Guid = UUID.randomUUID()

    val orgRoleRep = OrgRoleRepFixtures.adminFixture.complete(this, 0)
    setup {
      orgRoleClient(OrgRoleApi.Post(orgGuid, OrgRoleRepFixtures.adminFixture.creation()))
    }

    val orgRoleMembershipRep0 = OrgRoleMembershipRepFixtures.fixture.complete(this, account0Guid)
    setup {
      orgRoleMembershipClient(OrgRoleMembershipApi.Post(
          orgGuid = orgGuid,
          orgRoleGuid = orgRoleRep.guid,
          rep = OrgRoleMembershipRepFixtures.fixture.creation(account0Guid)
      ))
    }

    val orgRoleMembershipRep1 = OrgRoleMembershipRepFixtures.fixture.complete(this, account1Guid)
    setup {
      orgRoleMembershipClient(OrgRoleMembershipApi.Post(
          orgGuid = orgGuid,
          orgRoleGuid = orgRoleRep.guid,
          rep = OrgRoleMembershipRepFixtures.fixture.creation(account1Guid)
      ))
    }

    test(expectResult = setOf(orgRoleMembershipRep0, orgRoleMembershipRep1)) {
      orgRoleMembershipClient(OrgRoleMembershipApi.GetByOrgRoleGuid(orgGuid, orgRoleRep.guid))
    }
  }
}
