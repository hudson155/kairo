package io.limberapp.backend.module.auth.endpoint.feature.role

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.auth.api.feature.FeatureRoleApi
import io.limberapp.backend.module.auth.api.org.OrgRoleApi
import io.limberapp.backend.module.auth.rep.feature.FeatureRoleRep
import io.limberapp.backend.module.auth.testing.IntegrationTest
import io.limberapp.backend.module.auth.testing.fixtures.feature.FeatureRoleRepFixtures
import io.limberapp.backend.module.auth.testing.fixtures.org.OrgRoleRepFixtures
import io.limberapp.common.LimberApplication
import io.limberapp.common.permissions.featurePermissions.feature.forms.FormsFeaturePermissions
import org.junit.jupiter.api.Test
import java.util.*

internal class PatchFeatureRoleTest(
    engine: TestApplicationEngine,
    limberServer: LimberApplication<*>,
) : IntegrationTest(engine, limberServer) {
  @Test
  fun featureRoleDoesNotExist() {
    val featureGuid = UUID.randomUUID()
    val featureRoleGuid = UUID.randomUUID()

    test(expectResult = null) {
      featureRoleClient(FeatureRoleApi.Patch(
          featureGuid = featureGuid,
          featureRoleGuid = featureRoleGuid,
          rep = FeatureRoleRep.Update(permissions = FormsFeaturePermissions.fromBitString("0110")),
      ))
    }
  }

  @Test
  fun happyPathPermissions() {
    val orgGuid = UUID.randomUUID()
    val featureGuid = UUID.randomUUID()

    val orgRoleRep = OrgRoleRepFixtures.adminFixture.complete(this, 0)
    setup {
      orgRoleClient(OrgRoleApi.Post(orgGuid, OrgRoleRepFixtures.adminFixture.creation()))
    }

    var featureRoleRep = FeatureRoleRepFixtures.fixture.complete(this, orgRoleRep.guid, 1)
    setup {
      featureRoleClient(FeatureRoleApi.Post(featureGuid, FeatureRoleRepFixtures.fixture.creation(orgRoleRep.guid)))
    }

    featureRoleRep = featureRoleRep.copy(permissions = FormsFeaturePermissions.fromBitString("0110"))
    test(expectResult = featureRoleRep) {
      featureRoleClient(FeatureRoleApi.Patch(
          featureGuid = featureGuid,
          featureRoleGuid = featureRoleRep.guid,
          rep = FeatureRoleRep.Update(permissions = FormsFeaturePermissions.fromBitString("0110")),
      ))
    }

    test(expectResult = setOf(featureRoleRep)) {
      featureRoleClient(FeatureRoleApi.GetByFeatureGuid(featureGuid))
    }
  }
}
