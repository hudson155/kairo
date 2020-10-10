package io.limberapp.backend.module.auth.endpoint.feature.role

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.authorization.permissions.featurePermissions.feature.forms.FormsFeaturePermissions
import io.limberapp.backend.module.auth.api.feature.role.FeatureRoleApi
import io.limberapp.backend.module.auth.api.org.role.OrgRoleApi
import io.limberapp.backend.module.auth.exception.feature.FeatureRoleNotFound
import io.limberapp.backend.module.auth.rep.feature.FeatureRoleRep
import io.limberapp.backend.module.auth.testing.IntegrationTest
import io.limberapp.backend.module.auth.testing.fixtures.feature.FeatureRoleRepFixtures
import io.limberapp.backend.module.auth.testing.fixtures.org.OrgRoleRepFixtures
import io.limberapp.common.LimberApplication
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

internal class PatchFeatureRoleTest(
  engine: TestApplicationEngine,
  limberServer: LimberApplication<*>,
) : IntegrationTest(engine, limberServer) {
  @Test
  fun featureRoleDoesNotExist() {
    val featureGuid = UUID.randomUUID()
    val featureRoleGuid = UUID.randomUUID()

    test(
      endpoint = FeatureRoleApi.Patch(
        featureGuid = featureGuid,
        featureRoleGuid = featureRoleGuid,
        rep = FeatureRoleRep.Update(permissions = FormsFeaturePermissions.fromBitString("0110")),
      ),
      expectedException = FeatureRoleNotFound()
    )
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
    setup(FeatureRoleApi.Post(featureGuid, FeatureRoleRepFixtures.fixture.creation(orgRoleRep.guid)))

    featureRoleRep = featureRoleRep.copy(permissions = FormsFeaturePermissions.fromBitString("0110"))
    test(
      endpoint = FeatureRoleApi.Patch(
        featureGuid = featureGuid,
        featureRoleGuid = featureRoleRep.guid,
        rep = FeatureRoleRep.Update(permissions = FormsFeaturePermissions.fromBitString("0110")),
      )
    ) {
      val actual = json.parse<FeatureRoleRep.Complete>(responseContent)
      assertEquals(featureRoleRep, actual)
    }

    test(FeatureRoleApi.GetByFeatureGuid(featureGuid)) {
      val actual = json.parseSet<FeatureRoleRep.Complete>(responseContent)
      assertEquals(setOf(featureRoleRep), actual)
    }
  }
}
