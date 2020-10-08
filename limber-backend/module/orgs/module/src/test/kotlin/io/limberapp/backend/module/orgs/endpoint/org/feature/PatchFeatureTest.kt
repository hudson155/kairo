package io.limberapp.backend.module.orgs.endpoint.org.feature

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.orgs.api.feature.FeatureApi
import io.limberapp.backend.module.orgs.api.org.OrgApi
import io.limberapp.backend.module.orgs.exception.feature.FeatureNotFound
import io.limberapp.backend.module.orgs.exception.feature.FeaturePathIsNotUnique
import io.limberapp.backend.module.orgs.exception.feature.FeatureRankIsNotUnique
import io.limberapp.backend.module.orgs.rep.feature.FeatureRep
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.testing.IntegrationTest
import io.limberapp.backend.module.orgs.testing.fixtures.feature.FeatureRepFixtures
import io.limberapp.backend.module.orgs.testing.fixtures.org.OrgRepFixtures
import io.limberapp.common.LimberApplication
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

internal class PatchFeatureTest(
  engine: TestApplicationEngine,
  limberServer: LimberApplication<*>,
) : IntegrationTest(engine, limberServer) {
  @Test
  fun orgDoesNotExist() {
    val orgGuid = UUID.randomUUID()
    val featureGuid = UUID.randomUUID()

    test(
      endpoint = FeatureApi.Patch(orgGuid, featureGuid, FeatureRep.Update(name = "Renamed Feature")),
      expectedException = FeatureNotFound(),
    )
  }

  @Test
  fun featureDoesNotExist() {
    val featureGuid = UUID.randomUUID()

    val orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
    setup(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation()))

    test(
      endpoint = FeatureApi.Patch(orgRep.guid, featureGuid, FeatureRep.Update(name = "Renamed Feature")),
      expectedException = FeatureNotFound(),
    )

    test(OrgApi.Get(orgRep.guid)) {
      val actual = json.parse<OrgRep.Complete>(responseContent)
      assertEquals(orgRep, actual)
    }
  }

  @Test
  fun rankConflict() {
    var orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
    setup(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation()))

    val homeFeatureRep = FeatureRepFixtures.homeFixture.complete(this, 1)
    orgRep = orgRep.copy(features = orgRep.features + homeFeatureRep)
    setup(FeatureApi.Post(orgRep.guid, FeatureRepFixtures.homeFixture.creation()))

    val formsFeatureRep = FeatureRepFixtures.formsFixture.complete(this, 2)
    orgRep = orgRep.copy(features = orgRep.features + formsFeatureRep)
    setup(FeatureApi.Post(orgRep.guid, FeatureRepFixtures.formsFixture.creation()))

    test(
      endpoint = FeatureApi.Patch(orgRep.guid, formsFeatureRep.guid, FeatureRep.Update(rank = homeFeatureRep.rank)),
      expectedException = FeatureRankIsNotUnique(),
    )

    test(OrgApi.Get(orgRep.guid)) {
      val actual = json.parse<OrgRep.Complete>(responseContent)
      assertEquals(orgRep, actual)
    }
  }

  @Test
  fun pathConflict() {
    var orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
    setup(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation()))

    val homeFeatureRep = FeatureRepFixtures.homeFixture.complete(this, 1)
    orgRep = orgRep.copy(features = orgRep.features + homeFeatureRep)
    setup(FeatureApi.Post(orgRep.guid, FeatureRepFixtures.homeFixture.creation()))

    val formsFeatureRep = FeatureRepFixtures.formsFixture.complete(this, 2)
    orgRep = orgRep.copy(features = orgRep.features + formsFeatureRep)
    setup(FeatureApi.Post(orgRep.guid, FeatureRepFixtures.formsFixture.creation()))

    test(
      endpoint = FeatureApi.Patch(orgRep.guid, formsFeatureRep.guid, FeatureRep.Update(path = homeFeatureRep.path)),
      expectedException = FeaturePathIsNotUnique(),
    )

    test(OrgApi.Get(orgRep.guid)) {
      val actual = json.parse<OrgRep.Complete>(responseContent)
      assertEquals(orgRep, actual)
    }
  }

  @Test
  fun happyPathRank() {
    var orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
    setup(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation()))

    val homeFeatureRep = FeatureRepFixtures.homeFixture.complete(this, 1)
    orgRep = orgRep.copy(features = orgRep.features + homeFeatureRep)
    setup(FeatureApi.Post(orgRep.guid, FeatureRepFixtures.homeFixture.creation()))

    var formsFeatureRep = FeatureRepFixtures.formsFixture.complete(this, 2)
    orgRep = orgRep.copy(features = orgRep.features + formsFeatureRep)
    setup(FeatureApi.Post(orgRep.guid, FeatureRepFixtures.formsFixture.creation()))

    formsFeatureRep = formsFeatureRep.copy(rank = 3)
    orgRep = orgRep.copy(
      features = orgRep.features.map { if (it.guid == formsFeatureRep.guid) formsFeatureRep else it }
    )
    test(FeatureApi.Patch(orgRep.guid, formsFeatureRep.guid, FeatureRep.Update(rank = 3))) {
      val actual = json.parse<FeatureRep.Complete>(responseContent)
      assertEquals(formsFeatureRep, actual)
    }

    test(OrgApi.Get(orgRep.guid)) {
      val actual = json.parse<OrgRep.Complete>(responseContent)
      assertEquals(orgRep, actual)
    }
  }

  @Test
  fun happyPathName() {
    var orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
    setup(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation()))

    val homeFeatureRep = FeatureRepFixtures.homeFixture.complete(this, 1)
    orgRep = orgRep.copy(features = orgRep.features + homeFeatureRep)
    setup(FeatureApi.Post(orgRep.guid, FeatureRepFixtures.homeFixture.creation()))

    var formsFeatureRep = FeatureRepFixtures.formsFixture.complete(this, 2)
    orgRep = orgRep.copy(features = orgRep.features + formsFeatureRep)
    setup(FeatureApi.Post(orgRep.guid, FeatureRepFixtures.formsFixture.creation()))

    formsFeatureRep = formsFeatureRep.copy(name = "Renamed Feature")
    orgRep = orgRep.copy(
      features = orgRep.features.map { if (it.guid == formsFeatureRep.guid) formsFeatureRep else it }
    )
    test(FeatureApi.Patch(orgRep.guid,
      formsFeatureRep.guid,
      FeatureRep.Update(name = "Renamed Feature"))) {
      val actual = json.parse<FeatureRep.Complete>(responseContent)
      assertEquals(formsFeatureRep, actual)
    }

    test(OrgApi.Get(orgRep.guid)) {
      val actual = json.parse<OrgRep.Complete>(responseContent)
      assertEquals(orgRep, actual)
    }
  }

  @Test
  fun happyPathPath() {
    var orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
    setup(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation()))

    val homeFeatureRep = FeatureRepFixtures.homeFixture.complete(this, 1)
    orgRep = orgRep.copy(features = orgRep.features + homeFeatureRep)
    setup(FeatureApi.Post(orgRep.guid, FeatureRepFixtures.homeFixture.creation()))

    var formsFeatureRep = FeatureRepFixtures.formsFixture.complete(this, 2)
    orgRep = orgRep.copy(features = orgRep.features + formsFeatureRep)
    setup(FeatureApi.Post(orgRep.guid, FeatureRepFixtures.formsFixture.creation()))

    formsFeatureRep = formsFeatureRep.copy(path = "/renamed")
    orgRep = orgRep.copy(
      features = orgRep.features.map { if (it.guid == formsFeatureRep.guid) formsFeatureRep else it }
    )
    test(FeatureApi.Patch(orgRep.guid, formsFeatureRep.guid, FeatureRep.Update(path = "/renamed"))) {
      val actual = json.parse<FeatureRep.Complete>(responseContent)
      assertEquals(formsFeatureRep, actual)
    }

    test(OrgApi.Get(orgRep.guid)) {
      val actual = json.parse<OrgRep.Complete>(responseContent)
      assertEquals(orgRep, actual)
    }
  }

  @Test
  fun happyPathSetAndRemoveDefault() {
    var orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
    setup(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation()))

    val homeFeatureRep = FeatureRepFixtures.homeFixture.complete(this, 1)
    orgRep = orgRep.copy(features = orgRep.features + homeFeatureRep)
    setup(FeatureApi.Post(orgRep.guid, FeatureRepFixtures.homeFixture.creation()))

    var formsFeatureRep = FeatureRepFixtures.formsFixture.complete(this, 2)
    orgRep = orgRep.copy(features = orgRep.features + formsFeatureRep)
    setup(FeatureApi.Post(orgRep.guid, FeatureRepFixtures.formsFixture.creation()))

    formsFeatureRep = formsFeatureRep.copy(isDefaultFeature = true)
    orgRep = orgRep.copy(
      features = orgRep.features.map {
        if (it.guid == formsFeatureRep.guid) formsFeatureRep else it.copy(isDefaultFeature = false)
      }
    )
    test(
      endpoint = FeatureApi.Patch(orgRep.guid, formsFeatureRep.guid, FeatureRep.Update(isDefaultFeature = true))
    ) {
      val actual = json.parse<FeatureRep.Complete>(responseContent)
      assertEquals(formsFeatureRep, actual)
    }

    formsFeatureRep = formsFeatureRep.copy(isDefaultFeature = false)
    orgRep = orgRep.copy(
      features = orgRep.features.map { if (it.guid == formsFeatureRep.guid) formsFeatureRep else it }
    )
    test(
      endpoint = FeatureApi.Patch(orgRep.guid, formsFeatureRep.guid, FeatureRep.Update(isDefaultFeature = false))
    ) {
      val actual = json.parse<FeatureRep.Complete>(responseContent)
      assertEquals(formsFeatureRep, actual)
    }

    test(OrgApi.Get(orgRep.guid)) {
      val actual = json.parse<OrgRep.Complete>(responseContent)
      assertEquals(orgRep, actual)
    }
  }
}
