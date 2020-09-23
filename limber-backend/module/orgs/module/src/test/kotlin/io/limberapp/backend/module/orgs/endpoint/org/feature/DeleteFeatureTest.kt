package io.limberapp.backend.module.orgs.endpoint.org.feature

import io.limberapp.backend.module.orgs.api.org.OrgApi
import io.limberapp.backend.module.orgs.api.org.feature.OrgFeatureApi
import io.limberapp.backend.module.orgs.exception.feature.FeatureNotFound
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import io.limberapp.backend.module.orgs.testing.fixtures.org.FeatureRepFixtures
import io.limberapp.backend.module.orgs.testing.fixtures.org.OrgRepFixtures
import io.limberapp.common.testing.responseContent
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

internal class DeleteFeatureTest : ResourceTest() {
  @Test
  fun orgDoesNotExist() {
    val orgGuid = UUID.randomUUID()
    val featureGuid = UUID.randomUUID()

    limberTest.test(
      endpoint = OrgFeatureApi.Delete(orgGuid, featureGuid),
      expectedException = FeatureNotFound(),
    )
  }

  @Test
  fun featureDoesNotExist() {
    val featureGuid = UUID.randomUUID()

    val orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
    limberTest.setup(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation()))

    limberTest.test(
      endpoint = OrgFeatureApi.Delete(orgRep.guid, featureGuid),
      expectedException = FeatureNotFound(),
    )

    limberTest.test(OrgApi.Get(orgRep.guid)) {
      val actual = json.parse<OrgRep.Complete>(responseContent)
      assertEquals(orgRep, actual)
    }
  }

  @Test
  fun happyPath() {
    var orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
    limberTest.setup(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation()))

    val homeFeatureRep = FeatureRepFixtures.homeFixture.complete(this, 1)
    orgRep = orgRep.copy(features = orgRep.features + homeFeatureRep)
    limberTest.setup(OrgFeatureApi.Post(orgRep.guid, FeatureRepFixtures.homeFixture.creation()))

    val formsFeatureRep = FeatureRepFixtures.formsFixture.complete(this, 2)
    orgRep = orgRep.copy(features = orgRep.features + formsFeatureRep)
    limberTest.setup(OrgFeatureApi.Post(orgRep.guid, FeatureRepFixtures.formsFixture.creation()))

    orgRep = orgRep.copy(features = orgRep.features.filter { it.guid != formsFeatureRep.guid })
    limberTest.test(OrgFeatureApi.Delete(orgRep.guid, formsFeatureRep.guid)) {}

    limberTest.test(OrgApi.Get(orgRep.guid)) {
      val actual = json.parse<OrgRep.Complete>(responseContent)
      assertEquals(orgRep, actual)
    }
  }
}
