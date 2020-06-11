package io.limberapp.backend.module.orgs.endpoint.org.feature

import io.limberapp.backend.module.orgs.api.org.OrgApi
import io.limberapp.backend.module.orgs.api.org.feature.OrgFeatureApi
import io.limberapp.backend.module.orgs.exception.feature.FeatureNotFound
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import io.limberapp.backend.module.orgs.testing.fixtures.org.FeatureRepFixtures
import io.limberapp.backend.module.orgs.testing.fixtures.org.OrgRepFixtures
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

internal class DeleteFeatureTest : ResourceTest() {
  @Test
  fun orgDoesNotExist() {
    val orgGuid = UUID.randomUUID()
    val featureGuid = UUID.randomUUID()

    piperTest.test(
      endpoint = OrgFeatureApi.Delete(orgGuid, featureGuid),
      expectedException = FeatureNotFound()
    )
  }

  @Test
  fun featureDoesNotExist() {
    val orgOwnerAccountGuid = UUID.randomUUID()
    val featureGuid = UUID.randomUUID()

    val orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, orgOwnerAccountGuid, 0)
    piperTest.setup(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation(orgOwnerAccountGuid)))

    piperTest.test(
      endpoint = OrgFeatureApi.Delete(orgRep.guid, featureGuid),
      expectedException = FeatureNotFound()
    )

    piperTest.test(OrgApi.Get(orgRep.guid)) {
      val actual = json.parse<OrgRep.Complete>(response.content!!)
      assertEquals(orgRep, actual)
    }
  }

  @Test
  fun happyPath() {
    val orgOwnerAccountGuid = UUID.randomUUID()

    var orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, orgOwnerAccountGuid, 0)
    piperTest.setup(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation(orgOwnerAccountGuid)))

    val featureRep = FeatureRepFixtures.formsFixture.complete(this, 2)
    orgRep = orgRep.copy(features = orgRep.features.plus(featureRep))
    piperTest.setup(OrgFeatureApi.Post(orgRep.guid, FeatureRepFixtures.formsFixture.creation()))

    orgRep = orgRep.copy(features = orgRep.features.filter { it.guid != featureRep.guid })
    piperTest.test(OrgFeatureApi.Delete(orgRep.guid, featureRep.guid)) {}

    piperTest.test(OrgApi.Get(orgRep.guid)) {
      val actual = json.parse<OrgRep.Complete>(response.content!!)
      assertEquals(orgRep, actual)
    }
  }
}
