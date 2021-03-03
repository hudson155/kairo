package io.limberapp.endpoint.feature

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.api.feature.FeatureRoleApi
import io.limberapp.api.org.OrgRoleApi
import io.limberapp.rep.feature.FeatureRoleRepFixtures
import io.limberapp.rep.org.OrgRoleRepFixtures
import io.limberapp.server.Server
import io.limberapp.testing.integration.IntegrationTest
import org.junit.jupiter.api.Test
import java.util.UUID

internal class GetFeatureRolesByFeatureGuidTest(
    engine: TestApplicationEngine,
    server: Server<*>,
) : IntegrationTest(engine, server) {
  @Test
  fun `happy path, none exist`() {
    val featureGuid = UUID.randomUUID()

    test(expectResult = emptySet()) { featureRoleClient(FeatureRoleApi.GetByFeatureGuid(featureGuid)) }
  }

  @Test
  fun `happy path, multiple exist`() {
    val orgGuid = UUID.randomUUID()
    val featureGuid = UUID.randomUUID()

    val adminOrgRoleRep = OrgRoleRepFixtures.adminFixture.complete(this, orgGuid, 0)
    setup { orgRoleClient(OrgRoleApi.Post(OrgRoleRepFixtures.adminFixture.creation(orgGuid))) }

    val memberOrgRoleRep = OrgRoleRepFixtures.memberFixture.complete(this, orgGuid, 1)
    setup { orgRoleClient(OrgRoleApi.Post(OrgRoleRepFixtures.memberFixture.creation(orgGuid))) }

    val featureAdminRoleRep =
        FeatureRoleRepFixtures.fixture.complete(this, featureGuid, adminOrgRoleRep.guid, 2)
    setup {
      featureRoleClient(FeatureRoleApi.Post(
          rep = FeatureRoleRepFixtures.fixture.creation(featureGuid, adminOrgRoleRep.guid),
      ))
    }

    val featureMemberRoleRep =
        FeatureRoleRepFixtures.fixture.complete(this, featureGuid, memberOrgRoleRep.guid, 3)
    setup {
      featureRoleClient(FeatureRoleApi.Post(
          rep = FeatureRoleRepFixtures.fixture.creation(featureGuid, memberOrgRoleRep.guid),
      ))
    }

    test(expectResult = setOf(featureAdminRoleRep, featureMemberRoleRep)) {
      featureRoleClient(FeatureRoleApi.GetByFeatureGuid(featureGuid))
    }
  }
}
