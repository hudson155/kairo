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
      expectedException = OrgNotFound(),
    )
  }

  @Test
  fun duplicateRank() {
    var orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
    piperTest.setup(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation()))

    orgRep = orgRep.copy(features = orgRep.features + FeatureRepFixtures.homeFixture.complete(this, 1))
    piperTest.setup(
      endpoint = OrgFeatureApi.Post(
        orgGuid = orgRep.guid,
        rep = FeatureRepFixtures.homeFixture.creation(),
      ),
    )

    piperTest.test(
      endpoint = OrgFeatureApi.Post(
        orgGuid = orgRep.guid,
        rep = FeatureRepFixtures.formsFixture.creation().copy(rank = FeatureRepFixtures.homeFixture.creation().rank),
      ),
      expectedException = FeatureRankIsNotUnique(),
    )

    piperTest.test(OrgApi.Get(orgRep.guid)) {
      val actual = json.parse<OrgRep.Complete>(response.content!!)
      assertEquals(orgRep, actual)
    }
  }

  @Test
  fun duplicatePath() {
    var orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
    piperTest.setup(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation()))

    orgRep = orgRep.copy(features = orgRep.features + FeatureRepFixtures.homeFixture.complete(this, 1))
    piperTest.setup(
      endpoint = OrgFeatureApi.Post(
        orgGuid = orgRep.guid,
        rep = FeatureRepFixtures.homeFixture.creation(),
      ),
    )

    piperTest.test(
      endpoint = OrgFeatureApi.Post(
        orgGuid = orgRep.guid,
        rep = FeatureRepFixtures.formsFixture.creation().copy(path = FeatureRepFixtures.homeFixture.creation().path),
      ),
      expectedException = FeaturePathIsNotUnique(),
    )

    piperTest.test(OrgApi.Get(orgRep.guid)) {
      val actual = json.parse<OrgRep.Complete>(response.content!!)
      assertEquals(orgRep, actual)
    }
  }

  @Test
  fun happyPath() {
    var orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
    piperTest.setup(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation()))

    val featureRep = FeatureRepFixtures.formsFixture.complete(this, 1)
    orgRep = orgRep.copy(features = orgRep.features + featureRep)
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
