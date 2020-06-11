package io.limberapp.backend.module.auth.endpoint.org.role.membership

import io.limberapp.backend.module.auth.api.org.role.OrgRoleApi
import io.limberapp.backend.module.auth.api.org.role.OrgRoleMembershipApi
import io.limberapp.backend.module.auth.exception.org.AccountIsAlreadyMemberOfOrgRole
import io.limberapp.backend.module.auth.exception.org.OrgRoleNotFound
import io.limberapp.backend.module.auth.rep.org.OrgRoleMembershipRep
import io.limberapp.backend.module.auth.rep.org.OrgRoleRep
import io.limberapp.backend.module.auth.testing.ResourceTest
import io.limberapp.backend.module.auth.testing.fixtures.org.OrgRoleMembershipRepFixtures
import io.limberapp.backend.module.auth.testing.fixtures.org.OrgRoleRepFixtures
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

internal class PostOrgRoleMembershipTest : ResourceTest() {
  @Test
  fun orgRoleDoesNotExist() {
    val orgGuid = UUID.randomUUID()
    val orgRoleGuid = UUID.randomUUID()
    val accountGuid = UUID.randomUUID()

    // Create an org role anyways, to ensure that the error still happens when there is one.
    piperTest.setup(OrgRoleApi.Post(orgGuid, OrgRoleRepFixtures.adminFixture.creation()))

    piperTest.test(
      endpoint = OrgRoleMembershipApi.Post(
        orgGuid = orgGuid,
        orgRoleGuid = orgRoleGuid,
        rep = OrgRoleMembershipRepFixtures.fixture.creation(accountGuid)
      ),
      expectedException = OrgRoleNotFound()
    )
  }

  @Test
  fun duplicate() {
    val orgGuid = UUID.randomUUID()
    val accountGuid = UUID.randomUUID()

    val orgRoleRep = OrgRoleRepFixtures.adminFixture.complete(this, 0)
    piperTest.setup(OrgRoleApi.Post(orgGuid, OrgRoleRepFixtures.adminFixture.creation()))

    val orgRoleMembershipRep = OrgRoleMembershipRepFixtures.fixture.complete(this, accountGuid)
    piperTest.setup(
      endpoint = OrgRoleMembershipApi.Post(
        orgGuid = orgGuid,
        orgRoleGuid = orgRoleRep.guid,
        rep = OrgRoleMembershipRepFixtures.fixture.creation(accountGuid)
      )
    )

    piperTest.test(
      endpoint = OrgRoleMembershipApi.Post(
        orgGuid = orgGuid,
        orgRoleGuid = orgRoleRep.guid,
        rep = OrgRoleMembershipRepFixtures.fixture.creation(accountGuid)
      ),
      expectedException = AccountIsAlreadyMemberOfOrgRole()
    )

    piperTest.test(OrgRoleMembershipApi.GetByOrgRoleGuid(orgGuid, orgRoleRep.guid)) {
      val actual = json.parseSet<OrgRoleMembershipRep.Complete>(response.content!!)
      assertEquals(setOf(orgRoleMembershipRep), actual)
    }
  }

  @Test
  fun happyPath() {
    val orgGuid = UUID.randomUUID()
    val account0Guid = UUID.randomUUID()
    val account1Guid = UUID.randomUUID()

    var orgRoleRep = OrgRoleRepFixtures.adminFixture.complete(this, 0)
    piperTest.setup(OrgRoleApi.Post(orgGuid, OrgRoleRepFixtures.adminFixture.creation()))

    val orgRoleMembership0Rep = OrgRoleMembershipRepFixtures.fixture.complete(this, account0Guid)
    orgRoleRep = orgRoleRep.copy(memberCount = orgRoleRep.memberCount + 1)
    piperTest.test(
      endpoint = OrgRoleMembershipApi.Post(
        orgGuid = orgGuid,
        orgRoleGuid = orgRoleRep.guid,
        rep = OrgRoleMembershipRepFixtures.fixture.creation(account0Guid)
      )
    ) {
      val actual = json.parse<OrgRoleMembershipRep.Complete>(response.content!!)
      assertEquals(orgRoleMembership0Rep, actual)
    }
    val orgRoleMembership1Rep = OrgRoleMembershipRepFixtures.fixture.complete(this, account1Guid)
    orgRoleRep = orgRoleRep.copy(memberCount = orgRoleRep.memberCount + 1)
    piperTest.test(
      endpoint = OrgRoleMembershipApi.Post(
        orgGuid = orgGuid,
        orgRoleGuid = orgRoleRep.guid,
        rep = OrgRoleMembershipRepFixtures.fixture.creation(account1Guid)
      )
    ) {
      val actual = json.parse<OrgRoleMembershipRep.Complete>(response.content!!)
      assertEquals(orgRoleMembership1Rep, actual)
    }

    piperTest.test(OrgRoleMembershipApi.GetByOrgRoleGuid(orgGuid, orgRoleRep.guid)) {
      val actual = json.parseSet<OrgRoleMembershipRep.Complete>(response.content!!)
      assertEquals(setOf(orgRoleMembership0Rep, orgRoleMembership1Rep), actual)
    }

    piperTest.test(OrgRoleApi.GetByOrgGuid(orgGuid)) {
      val actual = json.parseSet<OrgRoleRep.Complete>(response.content!!)
      assertEquals(setOf(orgRoleRep), actual)
    }
  }
}
