package io.limberapp.backend.module.orgs.endpoint.org.feature

import io.limberapp.backend.module.orgs.api.org.OrgApi
import io.limberapp.backend.module.orgs.api.org.feature.OrgFeatureApi
import io.limberapp.backend.module.orgs.exception.feature.FeaturePathIsNotUnique
import io.limberapp.backend.module.orgs.exception.feature.FeatureRankIsNotUnique
import io.limberapp.backend.module.orgs.exception.org.OrgNotFound
import io.limberapp.backend.module.orgs.rep.org.FeatureRep
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import io.limberapp.backend.module.orgs.testing.fixtures.org.FeatureRepFixtures
import io.limberapp.backend.module.orgs.testing.fixtures.org.OrgRepFixtures
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

internal class PostFeatureTest : ResourceTest() {
  @Test
  fun orgDoesNotExist() {
    val orgGuid = UUID.randomUUID()

    piperTest.test(
      endpoint = OrgFeatureApi.Post(orgGuid, FeatureRepFixtures.formsFixture.creation()),
      expectedException = OrgNotFound()
    )
  }

  @Test
  fun duplicateRank() {
    val ownerUserGuid = UUID.randomUUID()

    val orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, ownerUserGuid, 0)
    piperTest.setup(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation(ownerUserGuid)))

    piperTest.test(
      endpoint = OrgFeatureApi.Post(
        orgGuid = orgRep.guid,
        rep = FeatureRepFixtures.formsFixture.creation().copy(rank = FeatureRepFixtures.default.creation().rank)
      ),
      expectedException = FeatureRankIsNotUnique()
    )

    piperTest.test(OrgApi.Get(orgRep.guid)) {
      val actual = json.parse<OrgRep.Complete>(response.content!!)
      assertEquals(orgRep, actual)
    }
  }

  @Test
  fun duplicatePath() {
    val ownerUserGuid = UUID.randomUUID()

    val orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, ownerUserGuid, 0)
    piperTest.setup(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation(ownerUserGuid)))

    piperTest.test(
      endpoint = OrgFeatureApi.Post(
        orgGuid = orgRep.guid,
        rep = FeatureRepFixtures.formsFixture.creation().copy(path = FeatureRepFixtures.default.creation().path)
      ),
      expectedException = FeaturePathIsNotUnique()
    )

    piperTest.test(OrgApi.Get(orgRep.guid)) {
      val actual = json.parse<OrgRep.Complete>(response.content!!)
      assertEquals(orgRep, actual)
    }
  }

  @Test
  fun happyPath() {
    val ownerUserGuid = UUID.randomUUID()

    var orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, ownerUserGuid, 0)
    piperTest.setup(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation(ownerUserGuid)))

    val featureRep = FeatureRepFixtures.formsFixture.complete(this, 2)
    orgRep = orgRep.copy(features = orgRep.features.plus(featureRep))
    piperTest.test(OrgFeatureApi.Post(orgRep.guid, FeatureRepFixtures.formsFixture.creation())) {
      val actual = json.parse<FeatureRep.Complete>(response.content!!)
      assertEquals(featureRep, actual)
    }

    piperTest.test(OrgApi.Get(orgRep.guid)) {
      val actual = json.parse<OrgRep.Complete>(response.content!!)
      assertEquals(orgRep, actual)
    }
  }
}
