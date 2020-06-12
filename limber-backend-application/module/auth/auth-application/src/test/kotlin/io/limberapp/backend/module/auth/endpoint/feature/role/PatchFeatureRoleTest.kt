package io.limberapp.backend.module.auth.endpoint.feature.role

import io.limberapp.backend.authorization.permissions.featurePermissions.feature.forms.FormsFeaturePermissions
import io.limberapp.backend.module.auth.api.feature.role.FeatureRoleApi
import io.limberapp.backend.module.auth.api.org.role.OrgRoleApi
import io.limberapp.backend.module.auth.exception.feature.FeatureRoleNotFound
import io.limberapp.backend.module.auth.rep.feature.FeatureRoleRep
import io.limberapp.backend.module.auth.testing.ResourceTest
import io.limberapp.backend.module.auth.testing.fixtures.feature.FeatureRoleRepFixtures
import io.limberapp.backend.module.auth.testing.fixtures.org.OrgRoleRepFixtures
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

internal class PatchFeatureRoleTest : ResourceTest() {
  @Test
  fun featureRoleDoesNotExist() {
    val featureGuid = UUID.randomUUID()
    val featureRoleGuid = UUID.randomUUID()

    val featureRoleUpdateRep = FeatureRoleRep.Update(permissions = FormsFeaturePermissions.fromBitString("110"))
    piperTest.test(
      endpoint = FeatureRoleApi.Patch(featureGuid, featureRoleGuid, featureRoleUpdateRep),
      expectedException = FeatureRoleNotFound()
    )
  }

  @Test
  fun happyPath() {
    val orgGuid = UUID.randomUUID()
    val featureGuid = UUID.randomUUID()

    val orgRoleRep = OrgRoleRepFixtures.adminFixture.complete(this, 0)
    piperTest.setup(OrgRoleApi.Post(orgGuid, OrgRoleRepFixtures.adminFixture.creation()))

    var featureRoleRep = FeatureRoleRepFixtures.fixture.complete(this, orgRoleRep.guid, 1)
    piperTest.setup(FeatureRoleApi.Post(featureGuid, FeatureRoleRepFixtures.fixture.creation(orgRoleRep.guid)))

    val featureRoleUpdateRep = FeatureRoleRep.Update(permissions = FormsFeaturePermissions.fromBitString("110"))
    featureRoleRep = featureRoleRep.copy(permissions = featureRoleUpdateRep.permissions!!)
    piperTest.test(FeatureRoleApi.Patch(featureGuid, featureRoleRep.guid, featureRoleUpdateRep)) {
      val actual = json.parse<FeatureRoleRep.Complete>(response.content!!)
      assertEquals(featureRoleRep, actual)
    }

    piperTest.test(FeatureRoleApi.GetByFeatureGuid(featureGuid)) {
      val actual = json.parseSet<FeatureRoleRep.Complete>(response.content!!)
      assertEquals(setOf(featureRoleRep), actual)
    }
  }
}
