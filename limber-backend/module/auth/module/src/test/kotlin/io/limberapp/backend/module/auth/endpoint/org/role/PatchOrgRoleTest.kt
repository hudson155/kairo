package io.limberapp.backend.module.auth.endpoint.org.role

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.authorization.permissions.orgPermissions.OrgPermissions
import io.limberapp.backend.module.auth.api.org.role.OrgRoleApi
import io.limberapp.backend.module.auth.exception.org.OrgRoleNameIsNotUnique
import io.limberapp.backend.module.auth.rep.org.OrgRoleRep
import io.limberapp.backend.module.auth.testing.IntegrationTest
import io.limberapp.backend.module.auth.testing.fixtures.org.OrgRoleRepFixtures
import io.limberapp.common.LimberApplication
import org.junit.jupiter.api.Test
import java.util.*

internal class PatchOrgRoleTest(
    engine: TestApplicationEngine,
    limberServer: LimberApplication<*>,
) : IntegrationTest(engine, limberServer) {
  @Test
  fun orgRoleDoesNotExist() {
    val orgGuid = UUID.randomUUID()
    val orgRoleGuid = UUID.randomUUID()

    test(expectResult = null) {
      orgRoleClient(OrgRoleApi.Patch(
          orgGuid = orgGuid,
          orgRoleGuid = orgRoleGuid,
          rep = OrgRoleRep.Update(permissions = OrgPermissions.fromBitString("0110")),
      ))
    }
  }

  @Test
  fun duplicateName() {
    val orgGuid = UUID.randomUUID()

    val adminOrgRoleRep = OrgRoleRepFixtures.adminFixture.complete(this, 0)
    setup { orgRoleClient(OrgRoleApi.Post(orgGuid, OrgRoleRepFixtures.adminFixture.creation())) }

    val memberOrgRoleRep = OrgRoleRepFixtures.memberFixture.complete(this, 1)
    setup { orgRoleClient(OrgRoleApi.Post(orgGuid, OrgRoleRepFixtures.memberFixture.creation())) }

    test(expectError = OrgRoleNameIsNotUnique()) {
      orgRoleClient(OrgRoleApi.Patch(orgGuid, memberOrgRoleRep.guid, OrgRoleRep.Update(name = adminOrgRoleRep.name)))
    }

    test(expectResult = setOf(adminOrgRoleRep, memberOrgRoleRep)) {
      orgRoleClient(OrgRoleApi.GetByOrgGuid(orgGuid))
    }
  }

  @Test
  fun happyPathPermissions() {
    val orgGuid = UUID.randomUUID()

    var orgRoleRep = OrgRoleRepFixtures.adminFixture.complete(this, 0)
    setup {
      orgRoleClient(OrgRoleApi.Post(orgGuid, OrgRoleRepFixtures.adminFixture.creation()))
    }

    orgRoleRep = orgRoleRep.copy(permissions = OrgPermissions.fromBitString("0110"))
    test(expectResult = orgRoleRep) {
      orgRoleClient(OrgRoleApi.Patch(
          orgGuid = orgGuid,
          orgRoleGuid = orgRoleRep.guid,
          rep = OrgRoleRep.Update(permissions = OrgPermissions.fromBitString("0110")),
      ))
    }

    test(expectResult = setOf(orgRoleRep)) {
      orgRoleClient(OrgRoleApi.GetByOrgGuid(orgGuid))
    }
  }

  @Test
  fun happyPathIsDefault() {
    val orgGuid = UUID.randomUUID()

    var orgRoleRep = OrgRoleRepFixtures.adminFixture.complete(this, 0)
    setup {
      orgRoleClient(OrgRoleApi.Post(orgGuid, OrgRoleRepFixtures.adminFixture.creation()))
    }

    orgRoleRep = orgRoleRep.copy(isDefault = true)
    test(expectResult = orgRoleRep) {
      orgRoleClient(OrgRoleApi.Patch(
          orgGuid = orgGuid,
          orgRoleGuid = orgRoleRep.guid,
          rep = OrgRoleRep.Update(isDefault = true),
      ))
    }

    test(expectResult = setOf(orgRoleRep)) {
      orgRoleClient(OrgRoleApi.GetByOrgGuid(orgGuid))
    }

    orgRoleRep = orgRoleRep.copy(isDefault = false)
    test(expectResult = orgRoleRep) {
      orgRoleClient(OrgRoleApi.Patch(
          orgGuid = orgGuid,
          orgRoleGuid = orgRoleRep.guid,
          rep = OrgRoleRep.Update(isDefault = false),
      ))
    }

    test(expectResult = setOf(orgRoleRep)) {
      orgRoleClient(OrgRoleApi.GetByOrgGuid(orgGuid))
    }
  }
}
