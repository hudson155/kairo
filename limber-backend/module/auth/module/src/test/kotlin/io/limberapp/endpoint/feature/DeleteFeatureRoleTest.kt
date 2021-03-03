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

internal class DeleteFeatureRoleTest(
    engine: TestApplicationEngine,
    server: Server<*>,
) : IntegrationTest(engine, server) {
  @Test
  fun `role does not exist`() {
    val featureRoleGuid = UUID.randomUUID()

    test(expectResult = null) { featureRoleClient(FeatureRoleApi.Delete(featureRoleGuid)) }
  }

  @Test
  fun `happy path`() {
    val orgGuid = UUID.randomUUID()
    val feature0Guid = UUID.randomUUID()
    val feature1Guid = UUID.randomUUID()

    val adminOrgRoleRep = OrgRoleRepFixtures.adminFixture.complete(this, orgGuid, 0)
    setup { orgRoleClient(OrgRoleApi.Post(OrgRoleRepFixtures.adminFixture.creation(orgGuid))) }

    val memberOrgRoleRep = OrgRoleRepFixtures.memberFixture.complete(this, orgGuid, 1)
    setup { orgRoleClient(OrgRoleApi.Post(OrgRoleRepFixtures.memberFixture.creation(orgGuid))) }

    val feature0AdminRoleRep = FeatureRoleRepFixtures.fixture.complete(this,
        feature0Guid, adminOrgRoleRep.guid, 2)
    setup {
      featureRoleClient(FeatureRoleApi.Post(
          rep = FeatureRoleRepFixtures.fixture.creation(feature0Guid, adminOrgRoleRep.guid),
      ))
    }

    val feature0MemberRoleRep = FeatureRoleRepFixtures.fixture.complete(this,
        feature0Guid, memberOrgRoleRep.guid, 3)
    setup {
      featureRoleClient(FeatureRoleApi.Post(
          rep = FeatureRoleRepFixtures.fixture.creation(feature0Guid, memberOrgRoleRep.guid),
      ))
    }

    val feature1AdminRoleRep = FeatureRoleRepFixtures.fixture.complete(this,
        feature1Guid, adminOrgRoleRep.guid, 4)
    setup {
      featureRoleClient(FeatureRoleApi.Post(
          rep = FeatureRoleRepFixtures.fixture.creation(feature1Guid, adminOrgRoleRep.guid),
      ))
    }

    test(expectResult = Unit) {
      featureRoleClient(FeatureRoleApi.Delete(feature0AdminRoleRep.guid))
    }

    test(expectResult = setOf(feature0MemberRoleRep)) {
      featureRoleClient(FeatureRoleApi.GetByFeatureGuid(feature0Guid))
    }

    test(expectResult = setOf(feature1AdminRoleRep)) {
      featureRoleClient(FeatureRoleApi.GetByFeatureGuid(feature1Guid))
    }
  }
}
