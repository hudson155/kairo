package io.limberapp.backend.module.orgs.endpoint.org.feature

import io.limberapp.backend.module.orgs.api.org.OrgApi
import io.limberapp.backend.module.orgs.api.org.feature.OrgFeatureApi
import io.limberapp.backend.module.orgs.exception.feature.FeatureNotFound
import io.limberapp.backend.module.orgs.exception.feature.FeaturePathIsNotUnique
import io.limberapp.backend.module.orgs.exception.feature.FeatureRankIsNotUnique
import io.limberapp.backend.module.orgs.rep.org.FeatureRep
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import io.limberapp.backend.module.orgs.testing.fixtures.org.FeatureRepFixtures
import io.limberapp.backend.module.orgs.testing.fixtures.org.OrgRepFixtures
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

internal class PatchFeatureTest : ResourceTest() {
  @Test
  fun orgDoesNotExist() {
    val orgGuid = UUID.randomUUID()
    val featureGuid = UUID.randomUUID()

    piperTest.test(
      endpoint = OrgFeatureApi.Patch(orgGuid, featureGuid, FeatureRep.Update(name = "Renamed Feature")),
      expectedException = FeatureNotFound(),
    )
  }

  @Test
  fun featureDoesNotExist() {
    val featureGuid = UUID.randomUUID()

    val orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
    piperTest.setup(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation()))

    piperTest.test(
      endpoint = OrgFeatureApi.Patch(orgRep.guid, featureGuid, FeatureRep.Update(name = "Renamed Feature")),
      expectedException = FeatureNotFound(),
    )

    piperTest.test(OrgApi.Get(orgRep.guid)) {
      val actual = json.parse<OrgRep.Complete>(response.content!!)
      assertEquals(orgRep, actual)
    }
  }

  @Test
  fun rankConflict() {
    var orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
    piperTest.setup(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation()))

    val homeFeatureRep = FeatureRepFixtures.homeFixture.complete(this, 1)
    orgRep = orgRep.copy(features = orgRep.features + homeFeatureRep)
    piperTest.setup(OrgFeatureApi.Post(orgRep.guid, FeatureRepFixtures.homeFixture.creation()))

    val formsFeatureRep = FeatureRepFixtures.formsFixture.complete(this, 2)
    orgRep = orgRep.copy(features = orgRep.features + formsFeatureRep)
    piperTest.setup(OrgFeatureApi.Post(orgRep.guid, FeatureRepFixtures.formsFixture.creation()))

    piperTest.test(
      endpoint = OrgFeatureApi.Patch(orgRep.guid, formsFeatureRep.guid, FeatureRep.Update(rank = homeFeatureRep.rank)),
      expectedException = FeatureRankIsNotUnique(),
    )

    piperTest.test(OrgApi.Get(orgRep.guid)) {
      val actual = json.parse<OrgRep.Complete>(response.content!!)
      assertEquals(orgRep, actual)
    }
  }

  @Test
  fun pathConflict() {
    var orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
    piperTest.setup(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation()))

    val homeFeatureRep = FeatureRepFixtures.homeFixture.complete(this, 1)
    orgRep = orgRep.copy(features = orgRep.features + homeFeatureRep)
    piperTest.setup(OrgFeatureApi.Post(orgRep.guid, FeatureRepFixtures.homeFixture.creation()))

    val formsFeatureRep = FeatureRepFixtures.formsFixture.complete(this, 2)
    orgRep = orgRep.copy(features = orgRep.features + formsFeatureRep)
    piperTest.setup(OrgFeatureApi.Post(orgRep.guid, FeatureRepFixtures.formsFixture.creation()))

    piperTest.test(
      endpoint = OrgFeatureApi.Patch(orgRep.guid, formsFeatureRep.guid, FeatureRep.Update(path = homeFeatureRep.path)),
      expectedException = FeaturePathIsNotUnique(),
    )

    piperTest.test(OrgApi.Get(orgRep.guid)) {
      val actual = json.parse<OrgRep.Complete>(response.content!!)
      assertEquals(orgRep, actual)
    }
  }

  @Test
  fun happyPathRank() {
    var orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
    piperTest.setup(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation()))

    val homeFeatureRep = FeatureRepFixtures.homeFixture.complete(this, 1)
    orgRep = orgRep.copy(features = orgRep.features + homeFeatureRep)
    piperTest.setup(OrgFeatureApi.Post(orgRep.guid, FeatureRepFixtures.homeFixture.creation()))

    var formsFeatureRep = FeatureRepFixtures.formsFixture.complete(this, 2)
    orgRep = orgRep.copy(features = orgRep.features + formsFeatureRep)
    piperTest.setup(OrgFeatureApi.Post(orgRep.guid, FeatureRepFixtures.formsFixture.creation()))

    formsFeatureRep = formsFeatureRep.copy(rank = 3)
    orgRep = orgRep.copy(
      features = orgRep.features.map { if (it.guid == formsFeatureRep.guid) formsFeatureRep else it }
    )
    piperTest.test(OrgFeatureApi.Patch(orgRep.guid, formsFeatureRep.guid, FeatureRep.Update(rank = 3))) {
      val actual = json.parse<FeatureRep.Complete>(response.content!!)
      assertEquals(formsFeatureRep, actual)
    }

    piperTest.test(OrgApi.Get(orgRep.guid)) {
      val actual = json.parse<OrgRep.Complete>(response.content!!)
      assertEquals(orgRep, actual)
    }
  }

  @Test
  fun happyPathName() {
    var orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
    piperTest.setup(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation()))

    val homeFeatureRep = FeatureRepFixtures.homeFixture.complete(this, 1)
    orgRep = orgRep.copy(features = orgRep.features + homeFeatureRep)
    piperTest.setup(OrgFeatureApi.Post(orgRep.guid, FeatureRepFixtures.homeFixture.creation()))

    var formsFeatureRep = FeatureRepFixtures.formsFixture.complete(this, 2)
    orgRep = orgRep.copy(features = orgRep.features + formsFeatureRep)
    piperTest.setup(OrgFeatureApi.Post(orgRep.guid, FeatureRepFixtures.formsFixture.creation()))

    formsFeatureRep = formsFeatureRep.copy(name = "Renamed Feature")
    orgRep = orgRep.copy(
      features = orgRep.features.map { if (it.guid == formsFeatureRep.guid) formsFeatureRep else it }
    )
    piperTest.test(OrgFeatureApi.Patch(orgRep.guid,
      formsFeatureRep.guid,
      FeatureRep.Update(name = "Renamed Feature"))) {
      val actual = json.parse<FeatureRep.Complete>(response.content!!)
      assertEquals(formsFeatureRep, actual)
    }

    piperTest.test(OrgApi.Get(orgRep.guid)) {
      val actual = json.parse<OrgRep.Complete>(response.content!!)
      assertEquals(orgRep, actual)
    }
  }

  @Test
  fun happyPathPath() {
    var orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
    piperTest.setup(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation()))

    val homeFeatureRep = FeatureRepFixtures.homeFixture.complete(this, 1)
    orgRep = orgRep.copy(features = orgRep.features + homeFeatureRep)
    piperTest.setup(OrgFeatureApi.Post(orgRep.guid, FeatureRepFixtures.homeFixture.creation()))

    var formsFeatureRep = FeatureRepFixtures.formsFixture.complete(this, 2)
    orgRep = orgRep.copy(features = orgRep.features + formsFeatureRep)
    piperTest.setup(OrgFeatureApi.Post(orgRep.guid, FeatureRepFixtures.formsFixture.creation()))

    formsFeatureRep = formsFeatureRep.copy(path = "/renamed")
    orgRep = orgRep.copy(
      features = orgRep.features.map { if (it.guid == formsFeatureRep.guid) formsFeatureRep else it }
    )
    piperTest.test(OrgFeatureApi.Patch(orgRep.guid, formsFeatureRep.guid, FeatureRep.Update(path = "/renamed"))) {
      val actual = json.parse<FeatureRep.Complete>(response.content!!)
      assertEquals(formsFeatureRep, actual)
    }

    piperTest.test(OrgApi.Get(orgRep.guid)) {
      val actual = json.parse<OrgRep.Complete>(response.content!!)
      assertEquals(orgRep, actual)
    }
  }

  @Test
  fun happyPathSetAndRemoveDefault() {
    var orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
    piperTest.setup(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation()))

    val homeFeatureRep = FeatureRepFixtures.homeFixture.complete(this, 1)
    orgRep = orgRep.copy(features = orgRep.features + homeFeatureRep)
    piperTest.setup(OrgFeatureApi.Post(orgRep.guid, FeatureRepFixtures.homeFixture.creation()))

    var formsFeatureRep = FeatureRepFixtures.formsFixture.complete(this, 2)
    orgRep = orgRep.copy(features = orgRep.features + formsFeatureRep)
    piperTest.setup(OrgFeatureApi.Post(orgRep.guid, FeatureRepFixtures.formsFixture.creation()))

    formsFeatureRep = formsFeatureRep.copy(isDefaultFeature = true)
    orgRep = orgRep.copy(
      features = orgRep.features.map {
        if (it.guid == formsFeatureRep.guid) formsFeatureRep else it.copy(isDefaultFeature = false)
      }
    )
    piperTest.test(
      endpoint = OrgFeatureApi.Patch(orgRep.guid, formsFeatureRep.guid, FeatureRep.Update(isDefaultFeature = true))
    ) {
      val actual = json.parse<FeatureRep.Complete>(response.content!!)
      assertEquals(formsFeatureRep, actual)
    }

    formsFeatureRep = formsFeatureRep.copy(isDefaultFeature = false)
    orgRep = orgRep.copy(
      features = orgRep.features.map { if (it.guid == formsFeatureRep.guid) formsFeatureRep else it }
    )
    piperTest.test(
      endpoint = OrgFeatureApi.Patch(orgRep.guid, formsFeatureRep.guid, FeatureRep.Update(isDefaultFeature = false))
    ) {
      val actual = json.parse<FeatureRep.Complete>(response.content!!)
      assertEquals(formsFeatureRep, actual)
    }

    piperTest.test(OrgApi.Get(orgRep.guid)) {
      val actual = json.parse<OrgRep.Complete>(response.content!!)
      assertEquals(orgRep, actual)
    }
  }
}
