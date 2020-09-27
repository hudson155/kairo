package io.limberapp.backend.module.auth.endpoint.org.role

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.authorization.permissions.orgPermissions.OrgPermissions
import io.limberapp.backend.module.auth.api.org.role.OrgRoleApi
import io.limberapp.backend.module.auth.exception.org.OrgRoleNameIsNotUnique
import io.limberapp.backend.module.auth.exception.org.OrgRoleNotFound
import io.limberapp.backend.module.auth.rep.org.OrgRoleRep
import io.limberapp.backend.module.auth.testing.IntegrationTest
import io.limberapp.backend.module.auth.testing.fixtures.org.OrgRoleRepFixtures
import io.limberapp.common.LimberApplication
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

internal class PatchOrgRoleTest(
  engine: TestApplicationEngine,
  limberServer: LimberApplication<*>,
) : IntegrationTest(engine, limberServer) {
  @Test
  fun orgRoleDoesNotExist() {
    val orgGuid = UUID.randomUUID()
    val orgRoleGuid = UUID.randomUUID()

    test(
      endpoint = OrgRoleApi.Patch(
        orgGuid = orgGuid,
        orgRoleGuid = orgRoleGuid,
        rep = OrgRoleRep.Update(permissions = OrgPermissions.fromBitString("0110")),
      ),
      expectedException = OrgRoleNotFound()
    )
  }

  @Test
  fun duplicateName() {
    val orgGuid = UUID.randomUUID()

    val adminOrgRoleRep = OrgRoleRepFixtures.adminFixture.complete(this, 0)
    setup(OrgRoleApi.Post(orgGuid, OrgRoleRepFixtures.adminFixture.creation()))

    val memberOrgRoleRep = OrgRoleRepFixtures.memberFixture.complete(this, 1)
    setup(OrgRoleApi.Post(orgGuid, OrgRoleRepFixtures.memberFixture.creation()))

    test(
      endpoint = OrgRoleApi.Patch(orgGuid, memberOrgRoleRep.guid, OrgRoleRep.Update(name = adminOrgRoleRep.name)),
      expectedException = OrgRoleNameIsNotUnique()
    )

    test(OrgRoleApi.GetByOrgGuid(orgGuid)) {
      val actual = json.parseSet<OrgRoleRep.Complete>(responseContent)
      assertEquals(setOf(adminOrgRoleRep, memberOrgRoleRep), actual)
    }
  }

  @Test
  fun happyPathPermissions() {
    val orgGuid = UUID.randomUUID()

    var orgRoleRep = OrgRoleRepFixtures.adminFixture.complete(this, 0)
    setup(OrgRoleApi.Post(orgGuid, OrgRoleRepFixtures.adminFixture.creation()))

    orgRoleRep = orgRoleRep.copy(permissions = OrgPermissions.fromBitString("0110"))
    test(
      endpoint = OrgRoleApi.Patch(
        orgGuid = orgGuid,
        orgRoleGuid = orgRoleRep.guid,
        rep = OrgRoleRep.Update(permissions = OrgPermissions.fromBitString("0110")),
      )
    ) {
      val actual = json.parse<OrgRoleRep.Complete>(responseContent)
      assertEquals(orgRoleRep, actual)
    }

    test(OrgRoleApi.GetByOrgGuid(orgGuid)) {
      val actual = json.parseSet<OrgRoleRep.Complete>(responseContent)
      assertEquals(setOf(orgRoleRep), actual)
    }
  }

  @Test
  fun happyPathIsDefault() {
    val orgGuid = UUID.randomUUID()

    var orgRoleRep = OrgRoleRepFixtures.adminFixture.complete(this, 0)
    setup(OrgRoleApi.Post(orgGuid, OrgRoleRepFixtures.adminFixture.creation()))

    orgRoleRep = orgRoleRep.copy(isDefault = true)
    test(
      endpoint = OrgRoleApi.Patch(
        orgGuid = orgGuid,
        orgRoleGuid = orgRoleRep.guid,
        rep = OrgRoleRep.Update(isDefault = true),
      )
    ) {
      val actual = json.parse<OrgRoleRep.Complete>(responseContent)
      assertEquals(orgRoleRep, actual)
    }

    test(OrgRoleApi.GetByOrgGuid(orgGuid)) {
      val actual = json.parseSet<OrgRoleRep.Complete>(responseContent)
      assertEquals(setOf(orgRoleRep), actual)
    }

    orgRoleRep = orgRoleRep.copy(isDefault = false)
    test(
      endpoint = OrgRoleApi.Patch(
        orgGuid = orgGuid,
        orgRoleGuid = orgRoleRep.guid,
        rep = OrgRoleRep.Update(isDefault = false),
      )
    ) {
      val actual = json.parse<OrgRoleRep.Complete>(responseContent)
      assertEquals(orgRoleRep, actual)
    }

    test(OrgRoleApi.GetByOrgGuid(orgGuid)) {
      val actual = json.parseSet<OrgRoleRep.Complete>(responseContent)
      assertEquals(setOf(orgRoleRep), actual)
    }
  }
}
