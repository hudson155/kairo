package io.limberapp.endpoint.org

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.api.org.OrgRoleApi
import io.limberapp.api.org.OrgRoleMembershipApi
import io.limberapp.exception.org.UserIsAlreadyMemberOfOrgRole
import io.limberapp.exception.org.OrgRoleNotFound
import io.limberapp.exception.unprocessable
import io.limberapp.rep.org.OrgRoleMembershipRepFixtures
import io.limberapp.rep.org.OrgRoleRepFixtures
import io.limberapp.server.Server
import io.limberapp.testing.integration.IntegrationTest
import org.junit.jupiter.api.Test
import java.util.UUID

internal class PostOrgRoleMembershipTest(
    engine: TestApplicationEngine,
    server: Server<*>,
) : IntegrationTest(engine, server) {
  @Test
  fun `role does not exist`() {
    val orgRoleGuid = UUID.randomUUID()
    val userGuid = UUID.randomUUID()

    test(expectError = OrgRoleNotFound().unprocessable()) {
      orgRoleMembershipClient(OrgRoleMembershipApi.Post(
          orgRoleGuid = orgRoleGuid,
          rep = OrgRoleMembershipRepFixtures.fixture.creation(userGuid),
      ))
    }
  }

  @Test
  fun duplicate() {
    val orgGuid = UUID.randomUUID()
    val userGuid = UUID.randomUUID()

    val orgRoleRep = OrgRoleRepFixtures.adminFixture.complete(this, orgGuid, 0)
    setup { orgRoleClient(OrgRoleApi.Post(OrgRoleRepFixtures.adminFixture.creation(orgGuid))) }

    val orgRoleMembershipRep = OrgRoleMembershipRepFixtures.fixture.complete(this, userGuid)
    setup {
      orgRoleMembershipClient(OrgRoleMembershipApi.Post(
          orgRoleGuid = orgRoleRep.guid,
          rep = OrgRoleMembershipRepFixtures.fixture.creation(userGuid),
      ))
    }

    test(expectError = UserIsAlreadyMemberOfOrgRole()) {
      orgRoleMembershipClient(OrgRoleMembershipApi.Post(
          orgRoleGuid = orgRoleRep.guid,
          rep = OrgRoleMembershipRepFixtures.fixture.creation(userGuid),
      ))
    }

    test(expectResult = setOf(orgRoleMembershipRep)) {
      orgRoleMembershipClient(OrgRoleMembershipApi.GetByOrgRoleGuid(orgRoleRep.guid))
    }
  }

  @Test
  fun `happy path (multiple)`() {
    val orgGuid = UUID.randomUUID()
    val user0Guid = UUID.randomUUID()
    val user1Guid = UUID.randomUUID()

    var orgRoleRep = OrgRoleRepFixtures.adminFixture.complete(this, orgGuid, 0)
    setup { orgRoleClient(OrgRoleApi.Post(OrgRoleRepFixtures.adminFixture.creation(orgGuid))) }

    val orgRoleMembership0Rep = OrgRoleMembershipRepFixtures.fixture.complete(this, user0Guid)
    orgRoleRep = orgRoleRep.copy(memberCount = orgRoleRep.memberCount + 1)
    test(expectResult = orgRoleMembership0Rep) {
      orgRoleMembershipClient(OrgRoleMembershipApi.Post(
          orgRoleGuid = orgRoleRep.guid,
          rep = OrgRoleMembershipRepFixtures.fixture.creation(user0Guid),
      ))
    }

    val orgRoleMembership1Rep = OrgRoleMembershipRepFixtures.fixture.complete(this, user1Guid)
    orgRoleRep = orgRoleRep.copy(memberCount = orgRoleRep.memberCount + 1)
    test(expectResult = orgRoleMembership1Rep) {
      orgRoleMembershipClient(OrgRoleMembershipApi.Post(
          orgRoleGuid = orgRoleRep.guid,
          rep = OrgRoleMembershipRepFixtures.fixture.creation(user1Guid),
      ))
    }

    test(expectResult = setOf(orgRoleMembership0Rep, orgRoleMembership1Rep)) {
      orgRoleMembershipClient(OrgRoleMembershipApi.GetByOrgRoleGuid(orgRoleRep.guid))
    }

    test(expectResult = setOf(orgRoleRep)) { orgRoleClient(OrgRoleApi.GetByOrgGuid(orgGuid)) }
  }
}
