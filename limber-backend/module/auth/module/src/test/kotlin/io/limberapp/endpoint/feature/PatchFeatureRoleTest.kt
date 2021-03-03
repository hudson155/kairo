package io.limberapp.endpoint.feature

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.api.feature.FeatureRoleApi
import io.limberapp.api.org.OrgRoleApi
import io.limberapp.permissions.feature.TestFeaturePermissions
import io.limberapp.rep.feature.FeatureRoleRep
import io.limberapp.rep.feature.FeatureRoleRepFixtures
import io.limberapp.rep.org.OrgRoleRepFixtures
import io.limberapp.server.Server
import io.limberapp.testing.integration.IntegrationTest
import org.junit.jupiter.api.Test
import java.util.UUID

internal class PatchFeatureRoleTest(
    engine: TestApplicationEngine,
    server: Server<*>,
) : IntegrationTest(engine, server) {
  @Test
  fun `role does not exist`() {
    val featureRoleGuid = UUID.randomUUID()

    test(expectResult = null) {
      featureRoleClient(FeatureRoleApi.Patch(
          featureRoleGuid = featureRoleGuid,
          rep = FeatureRoleRep.Update(permissions = TestFeaturePermissions.fromBitString("10")),
      ))
    }
  }

  @Test
  fun `permissions - happy path`() {
    val orgGuid = UUID.randomUUID()
    val featureGuid = UUID.randomUUID()

    val orgRoleRep = OrgRoleRepFixtures.adminFixture.complete(this, orgGuid, 0)
    setup { orgRoleClient(OrgRoleApi.Post(OrgRoleRepFixtures.adminFixture.creation(orgGuid))) }

    var featureRoleRep =
        FeatureRoleRepFixtures.fixture.complete(this, featureGuid, orgRoleRep.guid, 1)
    setup {
      featureRoleClient(FeatureRoleApi.Post(
          rep = FeatureRoleRepFixtures.fixture.creation(featureGuid, orgRoleRep.guid),
      ))
    }

    featureRoleRep = featureRoleRep.copy(permissions = TestFeaturePermissions.fromBitString("11"))
    test(expectResult = featureRoleRep) {
      featureRoleClient(FeatureRoleApi.Patch(
          featureRoleGuid = featureRoleRep.guid,
          rep = FeatureRoleRep.Update(permissions = TestFeaturePermissions.fromBitString("11")),
      ))
    }

    test(expectResult = setOf(featureRoleRep)) {
      featureRoleClient(FeatureRoleApi.GetByFeatureGuid(featureGuid))
    }
  }
}
