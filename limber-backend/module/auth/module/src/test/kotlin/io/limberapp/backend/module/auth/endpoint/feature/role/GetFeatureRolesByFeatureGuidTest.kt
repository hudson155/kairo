package io.limberapp.backend.module.auth.endpoint.feature.role

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.auth.api.feature.FeatureRoleApi
import io.limberapp.backend.module.auth.api.org.OrgRoleApi
import io.limberapp.backend.module.auth.testing.IntegrationTest
import io.limberapp.backend.module.auth.testing.fixtures.feature.FeatureRoleRepFixtures
import io.limberapp.backend.module.auth.testing.fixtures.org.OrgRoleRepFixtures
import io.limberapp.common.LimberApplication
import org.junit.jupiter.api.Test
import java.util.*

internal class GetFeatureRolesByFeatureGuidTest(
    engine: TestApplicationEngine,
    limberServer: LimberApplication<*>,
) : IntegrationTest(engine, limberServer) {
  @Test
  fun happyPathNoneExist() {
    val featureGuid = UUID.randomUUID()

    test(expectResult = emptySet()) {
      featureRoleClient(FeatureRoleApi.GetByFeatureGuid(featureGuid))
    }
  }

  @Test
  fun happyPathMultipleExist() {
    val orgGuid = UUID.randomUUID()
    val featureGuid = UUID.randomUUID()

    val adminOrgRoleRep = OrgRoleRepFixtures.adminFixture.complete(this, 0)
    setup {
      orgRoleClient(OrgRoleApi.Post(orgGuid, OrgRoleRepFixtures.adminFixture.creation()))
    }

    val memberOrgRoleRep = OrgRoleRepFixtures.memberFixture.complete(this, 1)
    setup {
      orgRoleClient(OrgRoleApi.Post(orgGuid, OrgRoleRepFixtures.memberFixture.creation()))
    }

    val featureAdminRoleRep = FeatureRoleRepFixtures.fixture.complete(this, adminOrgRoleRep.guid, 2)
    setup {
      featureRoleClient(FeatureRoleApi.Post(
          featureGuid = featureGuid,
          rep = FeatureRoleRepFixtures.fixture.creation(adminOrgRoleRep.guid)
      ))
    }

    val featureMemberRoleRep = FeatureRoleRepFixtures.fixture.complete(this, memberOrgRoleRep.guid, 3)
    setup {
      featureRoleClient(FeatureRoleApi.Post(
          featureGuid = featureGuid,
          rep = FeatureRoleRepFixtures.fixture.creation(memberOrgRoleRep.guid)
      ))
    }

    test(expectResult = setOf(featureAdminRoleRep, featureMemberRoleRep)) {
      featureRoleClient(FeatureRoleApi.GetByFeatureGuid(featureGuid))
    }
  }
}
