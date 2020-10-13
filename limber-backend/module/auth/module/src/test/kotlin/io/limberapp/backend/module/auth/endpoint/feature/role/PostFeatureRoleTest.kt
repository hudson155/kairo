package io.limberapp.backend.module.auth.endpoint.feature.role

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.auth.api.feature.FeatureRoleApi
import io.limberapp.backend.module.auth.api.org.OrgRoleApi
import io.limberapp.backend.module.auth.exception.feature.FeatureRoleOrgRoleIsNotUnique
import io.limberapp.backend.module.auth.testing.IntegrationTest
import io.limberapp.backend.module.auth.testing.fixtures.feature.FeatureRoleRepFixtures
import io.limberapp.backend.module.auth.testing.fixtures.org.OrgRoleRepFixtures
import io.limberapp.common.LimberApplication
import org.junit.jupiter.api.Test
import java.util.*

internal class PostFeatureRoleTest(
    engine: TestApplicationEngine,
    limberServer: LimberApplication<*>,
) : IntegrationTest(engine, limberServer) {
  @Test
  fun duplicateFeatureRoleGuid() {
    val featureGuid = UUID.randomUUID()

    val adminOrgRoleRep = OrgRoleRepFixtures.adminFixture.complete(this, 0)
    setup {
      orgRoleClient(OrgRoleApi.Post(adminOrgRoleRep.guid, OrgRoleRepFixtures.adminFixture.creation()))
    }

    val featureAdminOrgRoleRep = FeatureRoleRepFixtures.fixture.complete(this, adminOrgRoleRep.guid, 1)
    setup {
      featureRoleClient(FeatureRoleApi.Post(featureGuid, FeatureRoleRepFixtures.fixture.creation(adminOrgRoleRep.guid)))
    }

    test(expectError = FeatureRoleOrgRoleIsNotUnique()) {
      featureRoleClient(FeatureRoleApi.Post(
          featureGuid = featureGuid,
          rep = FeatureRoleRepFixtures.fixture.creation(adminOrgRoleRep.guid)
      ))
    }

    test(expectResult = setOf(featureAdminOrgRoleRep)) {
      featureRoleClient(FeatureRoleApi.GetByFeatureGuid(featureGuid))
    }
  }

  @Test
  fun happyPath() {
    val orgGuid = UUID.randomUUID()
    val featureGuid = UUID.randomUUID()

    val adminOrgRoleRep = OrgRoleRepFixtures.adminFixture.complete(this, 0)
    setup {
      orgRoleClient(OrgRoleApi.Post(orgGuid, OrgRoleRepFixtures.adminFixture.creation()))
    }

    val featureAdminRoleRep = FeatureRoleRepFixtures.fixture.complete(this, adminOrgRoleRep.guid, 1)
    test(expectResult = featureAdminRoleRep) {
      featureRoleClient(FeatureRoleApi.Post(featureGuid, FeatureRoleRepFixtures.fixture.creation(adminOrgRoleRep.guid)))
    }

    test(expectResult = setOf(featureAdminRoleRep)) {
      featureRoleClient(FeatureRoleApi.GetByFeatureGuid(featureGuid))
    }
  }
}
