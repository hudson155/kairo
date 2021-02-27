package io.limberapp.endpoint.feature

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.api.feature.FeatureApi
import io.limberapp.api.org.OrgApi
import io.limberapp.exception.feature.FeatureNameIsNotUnique
import io.limberapp.exception.feature.FeaturePathIsNotUnique
import io.limberapp.exception.feature.FeatureRankIsNotUnique
import io.limberapp.rep.feature.FeatureRep
import io.limberapp.rep.feature.FeatureRepFixtures
import io.limberapp.rep.org.OrgRepFixtures
import io.limberapp.server.Server
import io.limberapp.testing.integration.IntegrationTest
import org.junit.jupiter.api.Test
import java.util.UUID

internal class PatchFeatureTest(
    engine: TestApplicationEngine,
    server: Server<*>,
) : IntegrationTest(engine, server) {
  @Test
  fun `feature does not exist`() {
    val featureGuid = UUID.randomUUID()

    val orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
    setup {
      orgClient(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation()))
    }

    test(expectResult = null) {
      featureClient(FeatureApi.Patch(featureGuid, FeatureRep.Update(name = "Renamed Feature")))
    }

    test(expectResult = orgRep) {
      orgClient(OrgApi.Get(orgRep.guid))
    }
  }

  @Test
  fun `update name - conflict`() {
    var orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
    setup {
      orgClient(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation()))
    }

    val homeFeatureRep = FeatureRepFixtures.homeFixture.complete(this, orgRep.guid, 1)
    orgRep = orgRep.copy(features = orgRep.features + homeFeatureRep)
    setup { featureClient(FeatureApi.Post(orgRep.guid, FeatureRepFixtures.homeFixture.creation())) }

    val formsFeatureRep = FeatureRepFixtures.formsFixture.complete(this, orgRep.guid, 2)
    orgRep = orgRep.copy(features = orgRep.features + formsFeatureRep)
    setup {
      featureClient(FeatureApi.Post(orgRep.guid, FeatureRepFixtures.formsFixture.creation()))
    }

    test(expectError = FeatureNameIsNotUnique()) {
      featureClient(FeatureApi.Patch(
          featureGuid = formsFeatureRep.guid,
          rep = FeatureRep.Update(name = homeFeatureRep.name),
      ))
    }

    test(expectResult = orgRep) {
      orgClient(OrgApi.Get(orgRep.guid))
    }
  }

  @Test
  fun `update name - success`() {
    var orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
    setup {
      orgClient(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation()))
    }

    val homeFeatureRep = FeatureRepFixtures.homeFixture.complete(this, orgRep.guid, 1)
    orgRep = orgRep.copy(features = orgRep.features + homeFeatureRep)
    setup { featureClient(FeatureApi.Post(orgRep.guid, FeatureRepFixtures.homeFixture.creation())) }

    var formsFeatureRep = FeatureRepFixtures.formsFixture.complete(this, orgRep.guid, 2)
    orgRep = orgRep.copy(features = orgRep.features + formsFeatureRep)
    setup {
      featureClient(FeatureApi.Post(orgRep.guid, FeatureRepFixtures.formsFixture.creation()))
    }

    formsFeatureRep = formsFeatureRep.copy(name = "Renamed").also {
      orgRep = orgRep.copy(features = orgRep.features - formsFeatureRep + it)
    }
    test(expectResult = formsFeatureRep) {
      featureClient(FeatureApi.Patch(
          featureGuid = formsFeatureRep.guid,
          rep = FeatureRep.Update(name = "Renamed"),
      ))
    }

    test(expectResult = orgRep) {
      orgClient(OrgApi.Get(orgRep.guid))
    }
  }

  @Test
  fun `update path - conflict`() {
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
      featureClient(FeatureApi.Patch(
          featureGuid = formsFeatureRep.guid,
          rep = FeatureRep.Update(path = homeFeatureRep.path),
      ))
    }

    test(expectResult = orgRep) {
      orgClient(OrgApi.Get(orgRep.guid))
    }
  }

  @Test
  fun `update path - success`() {
    var orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
    setup {
      orgClient(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation()))
    }

    val homeFeatureRep = FeatureRepFixtures.homeFixture.complete(this, orgRep.guid, 1)
    orgRep = orgRep.copy(features = orgRep.features + homeFeatureRep)
    setup { featureClient(FeatureApi.Post(orgRep.guid, FeatureRepFixtures.homeFixture.creation())) }

    var formsFeatureRep = FeatureRepFixtures.formsFixture.complete(this, orgRep.guid, 2)
    orgRep = orgRep.copy(features = orgRep.features + formsFeatureRep)
    setup {
      featureClient(FeatureApi.Post(orgRep.guid, FeatureRepFixtures.formsFixture.creation()))
    }

    formsFeatureRep = formsFeatureRep.copy(path = "/renamed").also {
      orgRep = orgRep.copy(features = orgRep.features - formsFeatureRep + it)
    }
    test(expectResult = formsFeatureRep) {
      featureClient(FeatureApi.Patch(formsFeatureRep.guid, FeatureRep.Update(path = "/renamed")))
    }

    test(expectResult = orgRep) {
      orgClient(OrgApi.Get(orgRep.guid))
    }
  }

  @Test
  fun `update rank - conflict`() {
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
      featureClient(FeatureApi.Patch(
          featureGuid = formsFeatureRep.guid,
          rep = FeatureRep.Update(rank = homeFeatureRep.rank),
      ))
    }

    test(expectResult = orgRep) {
      orgClient(OrgApi.Get(orgRep.guid))
    }
  }

  @Test
  fun `update rank - success`() {
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

    formsFeatureRep = formsFeatureRep.copy(rank = 3).also {
      orgRep = orgRep.copy(features = orgRep.features - formsFeatureRep + it)
    }
    test(expectResult = formsFeatureRep) {
      featureClient(FeatureApi.Patch(formsFeatureRep.guid, FeatureRep.Update(rank = 3)))
    }

    test(expectResult = orgRep) {
      orgClient(OrgApi.Get(orgRep.guid))
    }
  }

  @Test
  fun `update default feature`() {
    var orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
    setup {
      orgClient(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation()))
    }

    var homeFeatureRep = FeatureRepFixtures.homeFixture.complete(this, orgRep.guid, 1)
    orgRep = orgRep.copy(features = orgRep.features + homeFeatureRep)
    setup {
      featureClient(FeatureApi.Post(orgRep.guid, FeatureRepFixtures.homeFixture.creation()))
    }

    var formsFeatureRep = FeatureRepFixtures.formsFixture.complete(this, orgRep.guid, 2)
    orgRep = orgRep.copy(features = orgRep.features + formsFeatureRep)
    setup {
      featureClient(FeatureApi.Post(orgRep.guid, FeatureRepFixtures.formsFixture.creation()))
    }

    // The first update only needs to set isDefaultFeature to true on formsFeatureRep.
    formsFeatureRep = formsFeatureRep.copy(isDefaultFeature = true)
    orgRep = orgRep.copy(features = listOf(homeFeatureRep, formsFeatureRep))
    test(expectResult = formsFeatureRep) {
      featureClient(FeatureApi.Patch(
          featureGuid = formsFeatureRep.guid,
          rep = FeatureRep.Update(isDefaultFeature = true),
      ))
    }

    test(expectResult = orgRep) {
      orgClient(OrgApi.Get(orgRep.guid))
    }

    // The second update needs to set isDefaultFeature to true on homeFeatureRep,
    // and also set isDefaultFeature to false on formsFeatureRep.
    homeFeatureRep = homeFeatureRep.copy(isDefaultFeature = true)
    formsFeatureRep = formsFeatureRep.copy(isDefaultFeature = false)
    orgRep = orgRep.copy(features = listOf(homeFeatureRep, formsFeatureRep))
    test(expectResult = homeFeatureRep) {
      featureClient(FeatureApi.Patch(
          featureGuid = homeFeatureRep.guid,
          rep = FeatureRep.Update(isDefaultFeature = true),
      ))
    }

    test(expectResult = orgRep) {
      orgClient(OrgApi.Get(orgRep.guid))
    }

    // The third update only needs to set isDefaultFeature to false on homeFeatureRep.
    homeFeatureRep = homeFeatureRep.copy(isDefaultFeature = false)
    orgRep = orgRep.copy(features = listOf(homeFeatureRep, formsFeatureRep))
    test(expectResult = homeFeatureRep) {
      featureClient(FeatureApi.Patch(
          featureGuid = homeFeatureRep.guid,
          rep = FeatureRep.Update(isDefaultFeature = false),
      ))
    }

    test(expectResult = orgRep) {
      orgClient(OrgApi.Get(orgRep.guid))
    }
  }
}
