package io.limberapp.endpoint.org

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.api.org.OrgRoleApi
import io.limberapp.exception.org.OrgRoleNameIsNotUnique
import io.limberapp.permissions.org.OrgPermissions
import io.limberapp.rep.org.OrgRoleRep
import io.limberapp.rep.org.OrgRoleRepFixtures
import io.limberapp.server.Server
import io.limberapp.testing.integration.IntegrationTest
import org.junit.jupiter.api.Test
import java.util.UUID

internal class PatchOrgRoleTest(
    engine: TestApplicationEngine,
    limberServer: Server<*>,
) : IntegrationTest(engine, limberServer) {
  @Test
  fun `role does not exist`() {
    val orgRoleGuid = UUID.randomUUID()

    test(expectResult = null) {
      orgRoleClient(OrgRoleApi.Patch(
          orgRoleGuid = orgRoleGuid,
          rep = OrgRoleRep.Update(permissions = OrgPermissions.fromBitString("0110")),
      ))
    }
  }

  @Test
  fun `name - duplicate`() {
    val orgGuid = UUID.randomUUID()

    val adminOrgRoleRep = OrgRoleRepFixtures.adminFixture.complete(this, orgGuid, 0)
    setup { orgRoleClient(OrgRoleApi.Post(OrgRoleRepFixtures.adminFixture.creation(orgGuid))) }

    val memberOrgRoleRep = OrgRoleRepFixtures.memberFixture.complete(this, orgGuid, 1)
    setup { orgRoleClient(OrgRoleApi.Post(OrgRoleRepFixtures.memberFixture.creation(orgGuid))) }

    test(expectError = OrgRoleNameIsNotUnique()) {
      orgRoleClient(OrgRoleApi.Patch(
          orgRoleGuid = memberOrgRoleRep.guid,
          rep = OrgRoleRep.Update(name = adminOrgRoleRep.name),
      ))
    }

    test(expectResult = setOf(adminOrgRoleRep, memberOrgRoleRep)) {
      orgRoleClient(OrgRoleApi.GetByOrgGuid(orgGuid))
    }
  }

  @Test
  fun `name - happy path`() {
    val orgGuid = UUID.randomUUID()

    var adminOrgRoleRep = OrgRoleRepFixtures.adminFixture.complete(this, orgGuid, 0)
    setup { orgRoleClient(OrgRoleApi.Post(OrgRoleRepFixtures.adminFixture.creation(orgGuid))) }

    adminOrgRoleRep = adminOrgRoleRep.copy(name = "Renamed", slug = "renamed")
    test(expectResult = adminOrgRoleRep) {
      orgRoleClient(OrgRoleApi.Patch(
          orgRoleGuid = adminOrgRoleRep.guid,
          rep = OrgRoleRep.Update(name = "Renamed"),
      ))
    }

    test(expectResult = setOf(adminOrgRoleRep)) {
      orgRoleClient(OrgRoleApi.GetByOrgGuid(orgGuid))
    }
  }

  @Test
  fun `permissions - happy path`() {
    val orgGuid = UUID.randomUUID()

    var orgRoleRep = OrgRoleRepFixtures.adminFixture.complete(this, orgGuid, 0)
    setup { orgRoleClient(OrgRoleApi.Post(OrgRoleRepFixtures.adminFixture.creation(orgGuid))) }

    orgRoleRep = orgRoleRep.copy(permissions = OrgPermissions.fromBitString("0110"))
    test(expectResult = orgRoleRep) {
      orgRoleClient(OrgRoleApi.Patch(
          orgRoleGuid = orgRoleRep.guid,
          rep = OrgRoleRep.Update(permissions = OrgPermissions.fromBitString("0110")),
      ))
    }

    test(expectResult = setOf(orgRoleRep)) { orgRoleClient(OrgRoleApi.GetByOrgGuid(orgGuid)) }
  }

  @Test
  fun `is default - happy path`() {
    val orgGuid = UUID.randomUUID()

    var orgRoleRep = OrgRoleRepFixtures.adminFixture.complete(this, orgGuid, 0)
    setup { orgRoleClient(OrgRoleApi.Post(OrgRoleRepFixtures.adminFixture.creation(orgGuid))) }

    orgRoleRep = orgRoleRep.copy(isDefault = true)
    test(expectResult = orgRoleRep) {
      orgRoleClient(OrgRoleApi.Patch(
          orgRoleGuid = orgRoleRep.guid,
          rep = OrgRoleRep.Update(isDefault = true),
      ))
    }

    test(expectResult = setOf(orgRoleRep)) { orgRoleClient(OrgRoleApi.GetByOrgGuid(orgGuid)) }

    orgRoleRep = orgRoleRep.copy(isDefault = false)
    test(expectResult = orgRoleRep) {
      orgRoleClient(OrgRoleApi.Patch(
          orgRoleGuid = orgRoleRep.guid,
          rep = OrgRoleRep.Update(isDefault = false),
      ))
    }

    test(expectResult = setOf(orgRoleRep)) { orgRoleClient(OrgRoleApi.GetByOrgGuid(orgGuid)) }
  }
}
