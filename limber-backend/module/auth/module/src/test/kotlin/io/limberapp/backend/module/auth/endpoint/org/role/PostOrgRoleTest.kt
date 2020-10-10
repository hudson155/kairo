package io.limberapp.backend.module.auth.endpoint.org.role

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.auth.api.org.role.OrgRoleApi
import io.limberapp.backend.module.auth.exception.org.OrgRoleNameIsNotUnique
import io.limberapp.backend.module.auth.testing.IntegrationTest
import io.limberapp.backend.module.auth.testing.fixtures.org.OrgRoleRepFixtures
import io.limberapp.common.LimberApplication
import org.junit.jupiter.api.Test
import java.util.*

internal class PostOrgRoleTest(
  engine: TestApplicationEngine,
  limberServer: LimberApplication<*>,
) : IntegrationTest(engine, limberServer) {
  @Test
  fun duplicateName() {
    val orgGuid = UUID.randomUUID()

    val adminOrgRoleRep = OrgRoleRepFixtures.adminFixture.complete(this, 0)
    setup {
      orgRoleClient(OrgRoleApi.Post(orgGuid, OrgRoleRepFixtures.adminFixture.creation()))
    }

    test(expectError = OrgRoleNameIsNotUnique()) {
      orgRoleClient(OrgRoleApi.Post(
        orgGuid = orgGuid,
        rep = OrgRoleRepFixtures.memberFixture.creation().copy(name = adminOrgRoleRep.name)
      ))
    }

    test(expectResult = setOf(adminOrgRoleRep)) {
      orgRoleClient(OrgRoleApi.GetByOrgGuid(orgGuid))
    }
  }

  @Test
  fun happyPath() {
    val orgGuid = UUID.randomUUID()

    val orgRoleRep = OrgRoleRepFixtures.adminFixture.complete(this, 0)
    test(expectResult = orgRoleRep) {
      orgRoleClient(OrgRoleApi.Post(orgGuid, OrgRoleRepFixtures.adminFixture.creation()))
    }

    test(expectResult = setOf(orgRoleRep)) {
      orgRoleClient(OrgRoleApi.GetByOrgGuid(orgGuid))
    }
  }
}
