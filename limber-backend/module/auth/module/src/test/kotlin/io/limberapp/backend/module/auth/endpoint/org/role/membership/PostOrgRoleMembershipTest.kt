package io.limberapp.backend.module.auth.endpoint.org.role.membership

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.auth.api.org.role.OrgRoleApi
import io.limberapp.backend.module.auth.api.org.role.OrgRoleMembershipApi
import io.limberapp.backend.module.auth.exception.org.AccountIsAlreadyMemberOfOrgRole
import io.limberapp.backend.module.auth.exception.org.OrgRoleNotFound
import io.limberapp.backend.module.auth.rep.org.OrgRoleRep
import io.limberapp.backend.module.auth.testing.IntegrationTest
import io.limberapp.backend.module.auth.testing.fixtures.org.OrgRoleMembershipRepFixtures
import io.limberapp.backend.module.auth.testing.fixtures.org.OrgRoleRepFixtures
import io.limberapp.common.LimberApplication
import io.limberapp.exception.unprocessableEntity.unprocessable
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

internal class PostOrgRoleMembershipTest(
  engine: TestApplicationEngine,
  limberServer: LimberApplication<*>,
) : IntegrationTest(engine, limberServer) {
  @Test
  fun orgRoleDoesNotExist() {
    val orgGuid = UUID.randomUUID()
    val orgRoleGuid = UUID.randomUUID()
    val accountGuid = UUID.randomUUID()

    // Create an org role anyways, to ensure that the error still happens when there is one.
    setup(OrgRoleApi.Post(orgGuid, OrgRoleRepFixtures.adminFixture.creation()))

    test(expectError = OrgRoleNotFound().unprocessable()) {
      orgRoleMembershipClient(OrgRoleMembershipApi.Post(
        orgGuid = orgGuid,
        orgRoleGuid = orgRoleGuid,
        rep = OrgRoleMembershipRepFixtures.fixture.creation(accountGuid)
      ))
    }
  }

  @Test
  fun duplicate() {
    val orgGuid = UUID.randomUUID()
    val accountGuid = UUID.randomUUID()

    val orgRoleRep = OrgRoleRepFixtures.adminFixture.complete(this, 0)
    setup(OrgRoleApi.Post(orgGuid, OrgRoleRepFixtures.adminFixture.creation()))

    val orgRoleMembershipRep = OrgRoleMembershipRepFixtures.fixture.complete(this, accountGuid)
    setup {
      orgRoleMembershipClient(OrgRoleMembershipApi.Post(
        orgGuid = orgGuid,
        orgRoleGuid = orgRoleRep.guid,
        rep = OrgRoleMembershipRepFixtures.fixture.creation(accountGuid)
      ))
    }

    test(expectError = AccountIsAlreadyMemberOfOrgRole()) {
      orgRoleMembershipClient(OrgRoleMembershipApi.Post(
        orgGuid = orgGuid,
        orgRoleGuid = orgRoleRep.guid,
        rep = OrgRoleMembershipRepFixtures.fixture.creation(accountGuid)
      ))
    }

    test(expectResult = setOf(orgRoleMembershipRep)) {
      orgRoleMembershipClient(OrgRoleMembershipApi.GetByOrgRoleGuid(orgGuid, orgRoleRep.guid))
    }
  }

  @Test
  fun happyPath() {
    val orgGuid = UUID.randomUUID()
    val account0Guid = UUID.randomUUID()
    val account1Guid = UUID.randomUUID()

    var orgRoleRep = OrgRoleRepFixtures.adminFixture.complete(this, 0)
    setup(OrgRoleApi.Post(orgGuid, OrgRoleRepFixtures.adminFixture.creation()))

    val orgRoleMembership0Rep = OrgRoleMembershipRepFixtures.fixture.complete(this, account0Guid)
    orgRoleRep = orgRoleRep.copy(memberCount = orgRoleRep.memberCount + 1)
    test(expectResult = orgRoleMembership0Rep) {
      orgRoleMembershipClient(OrgRoleMembershipApi.Post(
        orgGuid = orgGuid,
        orgRoleGuid = orgRoleRep.guid,
        rep = OrgRoleMembershipRepFixtures.fixture.creation(account0Guid)
      ))
    }
    val orgRoleMembership1Rep = OrgRoleMembershipRepFixtures.fixture.complete(this, account1Guid)
    orgRoleRep = orgRoleRep.copy(memberCount = orgRoleRep.memberCount + 1)
    test(expectResult = orgRoleMembership1Rep) {
      orgRoleMembershipClient(OrgRoleMembershipApi.Post(
        orgGuid = orgGuid,
        orgRoleGuid = orgRoleRep.guid,
        rep = OrgRoleMembershipRepFixtures.fixture.creation(account1Guid)
      ))
    }

    test(expectResult = setOf(orgRoleMembership0Rep, orgRoleMembership1Rep)) {
      orgRoleMembershipClient(OrgRoleMembershipApi.GetByOrgRoleGuid(orgGuid, orgRoleRep.guid))
    }

    test(OrgRoleApi.GetByOrgGuid(orgGuid)) {
      val actual = json.parseSet<OrgRoleRep.Complete>(responseContent)
      assertEquals(setOf(orgRoleRep), actual)
    }
  }
}
