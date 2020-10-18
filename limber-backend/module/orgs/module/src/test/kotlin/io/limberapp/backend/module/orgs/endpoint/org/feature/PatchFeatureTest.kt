package io.limberapp.backend.module.orgs.endpoint.org.feature

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.orgs.api.feature.FeatureApi
import io.limberapp.backend.module.orgs.api.org.OrgApi
import io.limberapp.backend.module.orgs.exception.feature.FeaturePathIsNotUnique
import io.limberapp.backend.module.orgs.exception.feature.FeatureRankIsNotUnique
import io.limberapp.backend.module.orgs.rep.feature.FeatureRep
import io.limberapp.backend.module.orgs.testing.IntegrationTest
import io.limberapp.backend.module.orgs.testing.fixtures.feature.FeatureRepFixtures
import io.limberapp.backend.module.orgs.testing.fixtures.org.OrgRepFixtures
import io.limberapp.common.LimberApplication
import org.junit.jupiter.api.Test
import java.util.*

internal class PatchFeatureTest(
    engine: TestApplicationEngine,
    limberServer: LimberApplication<*>,
) : IntegrationTest(engine, limberServer) {
  @Test
  fun orgDoesNotExist() {
    val orgGuid = UUID.randomUUID()
    val featureGuid = UUID.randomUUID()

    test(expectResult = null) {
      featureClient(FeatureApi.Patch(orgGuid, featureGuid, FeatureRep.Update(name = "Renamed Feature")))
    }
  }

  @Test
  fun featureDoesNotExist() {
    val featureGuid = UUID.randomUUID()

    val orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
    setup {
      orgClient(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation()))
    }

    test(expectResult = null) {
      featureClient(FeatureApi.Patch(orgRep.guid, featureGuid, FeatureRep.Update(name = "Renamed Feature")))
    }

    test(expectResult = orgRep) {
      orgClient(OrgApi.Get(orgRep.guid))
    }
  }

  @Test
  fun rankConflict() {
    var orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
    setup {
      orgClient(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation()))
    }

    val homeFeatureRep = FeatureRepFixtures.homeFixture.complete(this, orgRep.guid, 1)
    orgRep = orgRep.copy(features = orgRep.features + homeFeatureRep)
    setup {
      featureClient(FeatureApi.Post(orgRep.guid, FeatureRepFixtures.homeFixture.creation()))
    }

    val formsFeatureRep = FeatureRepFixtures.formsFixture.complete(this, orgRep.guid, 2)
    orgRep = orgRep.copy(features = orgRep.features + formsFeatureRep)
    setup {
      featureClient(FeatureApi.Post(orgRep.guid, FeatureRepFixtures.formsFixture.creation()))
    }

    test(expectError = FeatureRankIsNotUnique()) {
      featureClient(FeatureApi.Patch(orgRep.guid, formsFeatureRep.guid, FeatureRep.Update(rank = homeFeatureRep.rank)))
    }

    test(expectResult = orgRep) {
      orgClient(OrgApi.Get(orgRep.guid))
    }
  }

  @Test
  fun pathConflict() {
    var orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
    setup {
      orgClient(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation()))
    }

    val homeFeatureRep = FeatureRepFixtures.homeFixture.complete(this, orgRep.guid, 1)
    orgRep = orgRep.copy(features = orgRep.features + homeFeatureRep)
    setup {
      featureClient(FeatureApi.Post(orgRep.guid, FeatureRepFixtures.homeFixture.creation()))
    }

    val formsFeatureRep = FeatureRepFixtures.formsFixture.complete(this, orgRep.guid, 2)
    orgRep = orgRep.copy(features = orgRep.features + formsFeatureRep)
    setup {
      featureClient(FeatureApi.Post(orgRep.guid, FeatureRepFixtures.formsFixture.creation()))
    }

    test(expectError = FeaturePathIsNotUnique()) {
      featureClient(FeatureApi.Patch(orgRep.guid, formsFeatureRep.guid, FeatureRep.Update(path = homeFeatureRep.path)))
    }

    test(expectResult = orgRep) {
      orgClient(OrgApi.Get(orgRep.guid))
    }
  }

  @Test
  fun happyPathRank() {
    var orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
    setup {
      orgClient(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation()))
    }

    val homeFeatureRep = FeatureRepFixtures.homeFixture.complete(this, orgRep.guid, 1)
    orgRep = orgRep.copy(features = orgRep.features + homeFeatureRep)
    setup {
      featureClient(FeatureApi.Post(orgRep.guid, FeatureRepFixtures.homeFixture.creation()))
    }

    var formsFeatureRep = FeatureRepFixtures.formsFixture.complete(this, orgRep.guid, 2)
    orgRep = orgRep.copy(features = orgRep.features + formsFeatureRep)
    setup {
      featureClient(FeatureApi.Post(orgRep.guid, FeatureRepFixtures.formsFixture.creation()))
    }

    formsFeatureRep = formsFeatureRep.copy(rank = 3)
    orgRep = orgRep.copy(
        features = orgRep.features.map { if (it.guid == formsFeatureRep.guid) formsFeatureRep else it }
    )
    test(expectResult = formsFeatureRep) {
      featureClient(FeatureApi.Patch(orgRep.guid, formsFeatureRep.guid, FeatureRep.Update(rank = 3)))
    }

    test(expectResult = orgRep) {
      orgClient(OrgApi.Get(orgRep.guid))
    }
  }

  @Test
  fun happyPathName() {
    var orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
    setup {
      orgClient(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation()))
    }

    val homeFeatureRep = FeatureRepFixtures.homeFixture.complete(this, orgRep.guid, 1)
    orgRep = orgRep.copy(features = orgRep.features + homeFeatureRep)
    setup { featureClient(FeatureApi.Post(orgRep.guid, FeatureRepFixtures.homeFixture.creation())) }

    var formsFeatureRep = FeatureRepFixtures.formsFixture.complete(this, orgRep.guid, 2)
    orgRep = orgRep.copy(features = orgRep.features + formsFeatureRep)
    setup { featureClient(FeatureApi.Post(orgRep.guid, FeatureRepFixtures.formsFixture.creation())) }

    formsFeatureRep = formsFeatureRep.copy(name = "Renamed Feature")
    orgRep = orgRep.copy(
        features = orgRep.features.map { if (it.guid == formsFeatureRep.guid) formsFeatureRep else it }
    )
    test(expectResult = formsFeatureRep) {
      featureClient(FeatureApi.Patch(orgRep.guid, formsFeatureRep.guid, FeatureRep.Update(name = "Renamed Feature")))
    }

    test(expectResult = orgRep) {
      orgClient(OrgApi.Get(orgRep.guid))
    }
  }

  @Test
  fun happyPathPath() {
    var orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
    setup {
      orgClient(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation()))
    }

    val homeFeatureRep = FeatureRepFixtures.homeFixture.complete(this, orgRep.guid, 1)
    orgRep = orgRep.copy(features = orgRep.features + homeFeatureRep)
    setup { featureClient(FeatureApi.Post(orgRep.guid, FeatureRepFixtures.homeFixture.creation())) }

    var formsFeatureRep = FeatureRepFixtures.formsFixture.complete(this, orgRep.guid, 2)
    orgRep = orgRep.copy(features = orgRep.features + formsFeatureRep)
    setup { featureClient(FeatureApi.Post(orgRep.guid, FeatureRepFixtures.formsFixture.creation())) }

    formsFeatureRep = formsFeatureRep.copy(path = "/renamed")
    orgRep = orgRep.copy(
        features = orgRep.features.map { if (it.guid == formsFeatureRep.guid) formsFeatureRep else it }
    )
    test(expectResult = formsFeatureRep) {
      featureClient(FeatureApi.Patch(orgRep.guid, formsFeatureRep.guid, FeatureRep.Update(path = "/renamed")))
    }

    test(expectResult = orgRep) {
      orgClient(OrgApi.Get(orgRep.guid))
    }
  }

  @Test
  fun happyPathSetAndRemoveDefault() {
    var orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
    setup {
      orgClient(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation()))
    }

    val homeFeatureRep = FeatureRepFixtures.homeFixture.complete(this, orgRep.guid, 1)
    orgRep = orgRep.copy(features = orgRep.features + homeFeatureRep)
    setup {
      featureClient(FeatureApi.Post(orgRep.guid, FeatureRepFixtures.homeFixture.creation()))
    }

    var formsFeatureRep = FeatureRepFixtures.formsFixture.complete(this, orgRep.guid, 2)
    orgRep = orgRep.copy(features = orgRep.features + formsFeatureRep)
    setup {
      featureClient(FeatureApi.Post(orgRep.guid, FeatureRepFixtures.formsFixture.creation()))
    }

    formsFeatureRep = formsFeatureRep.copy(isDefaultFeature = true)
    orgRep = orgRep.copy(
        features = orgRep.features.map {
          if (it.guid == formsFeatureRep.guid) formsFeatureRep else it.copy(isDefaultFeature = false)
        }
    )
    test(expectResult = formsFeatureRep) {
      featureClient(FeatureApi.Patch(orgRep.guid, formsFeatureRep.guid, FeatureRep.Update(isDefaultFeature = true)))
    }

    formsFeatureRep = formsFeatureRep.copy(isDefaultFeature = false)
    orgRep = orgRep.copy(
        features = orgRep.features.map { if (it.guid == formsFeatureRep.guid) formsFeatureRep else it }
    )
    test(expectResult = formsFeatureRep) {
      featureClient(FeatureApi.Patch(orgRep.guid, formsFeatureRep.guid, FeatureRep.Update(isDefaultFeature = false)))
    }

    test(expectResult = orgRep) {
      orgClient(OrgApi.Get(orgRep.guid))
    }
  }
}
