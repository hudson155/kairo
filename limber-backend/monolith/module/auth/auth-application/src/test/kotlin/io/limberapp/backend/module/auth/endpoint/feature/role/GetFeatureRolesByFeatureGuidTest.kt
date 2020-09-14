package io.limberapp.backend.module.auth.endpoint.feature.role

import io.limberapp.backend.module.auth.api.feature.role.FeatureRoleApi
import io.limberapp.backend.module.auth.api.org.role.OrgRoleApi
import io.limberapp.backend.module.auth.rep.feature.FeatureRoleRep
import io.limberapp.backend.module.auth.testing.ResourceTest
import io.limberapp.backend.module.auth.testing.fixtures.feature.FeatureRoleRepFixtures
import io.limberapp.backend.module.auth.testing.fixtures.org.OrgRoleRepFixtures
import io.limberapp.common.testing.responseContent
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class GetFeatureRolesByFeatureGuidTest : ResourceTest() {
  @Test
  fun happyPathNoneExist() {
    val featureGuid = UUID.randomUUID()

    limberTest.test(FeatureRoleApi.GetByFeatureGuid(featureGuid)) {
      val actual = json.parseSet<FeatureRoleRep.Complete>(responseContent)
      assertTrue(actual.isEmpty())
    }
  }

  @Test
  fun happyPathMultipleExist() {
    val orgGuid = UUID.randomUUID()
    val featureGuid = UUID.randomUUID()

    val adminOrgRoleRep = OrgRoleRepFixtures.adminFixture.complete(this, 0)
    limberTest.setup(OrgRoleApi.Post(orgGuid, OrgRoleRepFixtures.adminFixture.creation()))

    val memberOrgRoleRep = OrgRoleRepFixtures.memberFixture.complete(this, 1)
    limberTest.setup(OrgRoleApi.Post(orgGuid, OrgRoleRepFixtures.memberFixture.creation()))

    val featureAdminRoleRep = FeatureRoleRepFixtures.fixture.complete(this, adminOrgRoleRep.guid, 2)
    limberTest.setup(FeatureRoleApi.Post(featureGuid, FeatureRoleRepFixtures.fixture.creation(adminOrgRoleRep.guid)))

    val featureMemberRoleRep = FeatureRoleRepFixtures.fixture.complete(this, memberOrgRoleRep.guid, 3)
    limberTest.setup(FeatureRoleApi.Post(featureGuid, FeatureRoleRepFixtures.fixture.creation(memberOrgRoleRep.guid)))

    limberTest.test(FeatureRoleApi.GetByFeatureGuid(featureGuid)) {
      val actual = json.parseSet<FeatureRoleRep.Complete>(responseContent)
      assertEquals(setOf(featureAdminRoleRep, featureMemberRoleRep), actual)
    }
  }
}
