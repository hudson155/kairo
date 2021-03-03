package io.limberapp.endpoint.feature

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.api.feature.FeatureRoleApi
import io.limberapp.api.org.OrgRoleApi
import io.limberapp.exception.feature.FeatureRoleOrgRoleIsNotUnique
import io.limberapp.rep.feature.FeatureRoleRepFixtures
import io.limberapp.rep.org.OrgRoleRepFixtures
import io.limberapp.server.Server
import io.limberapp.testing.integration.IntegrationTest
import org.junit.jupiter.api.Test
import java.util.UUID

internal class PostFeatureRoleTest(
    engine: TestApplicationEngine,
    server: Server<*>,
) : IntegrationTest(engine, server) {
  @Test
  fun duplicate() {
    val orgGuid = UUID.randomUUID()
    val featureGuid = UUID.randomUUID()

    val adminOrgRoleRep = OrgRoleRepFixtures.adminFixture.complete(this, orgGuid, 0)
    setup {
      orgRoleClient(OrgRoleApi.Post(OrgRoleRepFixtures.adminFixture.creation(adminOrgRoleRep.guid)))
    }

    val adminFeatureRoleRep = FeatureRoleRepFixtures.fixture.complete(this,
        featureGuid, adminOrgRoleRep.guid, 1)
    setup {
      featureRoleClient(FeatureRoleApi.Post(
          rep = FeatureRoleRepFixtures.fixture.creation(featureGuid, adminOrgRoleRep.guid),
      ))
    }

    test(expectError = FeatureRoleOrgRoleIsNotUnique()) {
      featureRoleClient(FeatureRoleApi.Post(
          rep = FeatureRoleRepFixtures.fixture.creation(featureGuid, adminOrgRoleRep.guid),
      ))
    }

    test(expectResult = setOf(adminFeatureRoleRep)) {
      featureRoleClient(FeatureRoleApi.GetByFeatureGuid(featureGuid))
    }
  }

  @Test
  fun `happy path`() {
    val orgGuid = UUID.randomUUID()
    val featureGuid = UUID.randomUUID()

    val adminOrgRoleRep = OrgRoleRepFixtures.adminFixture.complete(this, orgGuid, 0)
    setup {
      orgRoleClient(OrgRoleApi.Post(OrgRoleRepFixtures.adminFixture.creation(orgGuid)))
    }

    val adminFeatureRoleRep = FeatureRoleRepFixtures.fixture.complete(this,
        featureGuid, adminOrgRoleRep.guid, 1)
    test(expectResult = adminFeatureRoleRep) {
      featureRoleClient(FeatureRoleApi.Post(
          rep = FeatureRoleRepFixtures.fixture.creation(featureGuid, adminOrgRoleRep.guid),
      ))
    }

    test(expectResult = setOf(adminFeatureRoleRep)) {
      featureRoleClient(FeatureRoleApi.GetByFeatureGuid(featureGuid))
    }
  }
}
