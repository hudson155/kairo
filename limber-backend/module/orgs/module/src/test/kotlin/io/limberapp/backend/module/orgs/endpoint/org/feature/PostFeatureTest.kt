package io.limberapp.backend.module.orgs.endpoint.org.feature

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.orgs.api.feature.FeatureApi
import io.limberapp.backend.module.orgs.api.org.OrgApi
import io.limberapp.backend.module.orgs.exception.feature.FeaturePathIsNotUnique
import io.limberapp.backend.module.orgs.exception.feature.FeatureRankIsNotUnique
import io.limberapp.backend.module.orgs.exception.org.OrgNotFound
import io.limberapp.backend.module.orgs.testing.IntegrationTest
import io.limberapp.backend.module.orgs.testing.fixtures.feature.FeatureRepFixtures
import io.limberapp.backend.module.orgs.testing.fixtures.org.OrgRepFixtures
import io.limberapp.common.LimberApplication
import io.limberapp.common.exception.unprocessableEntity.unprocessable
import org.junit.jupiter.api.Test
import java.util.*

internal class PostFeatureTest(
    engine: TestApplicationEngine,
    limberServer: LimberApplication<*>,
) : IntegrationTest(engine, limberServer) {
  @Test
  fun orgDoesNotExist() {
    val orgGuid = UUID.randomUUID()

    test(expectError = OrgNotFound().unprocessable()) {
      featureClient(FeatureApi.Post(orgGuid, FeatureRepFixtures.formsFixture.creation()))
    }
  }

  @Test
  fun duplicateRank() {
    var orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
    setup {
      orgClient(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation()))
    }

    orgRep = orgRep.copy(features = orgRep.features + FeatureRepFixtures.homeFixture.complete(this, orgRep.guid, 1))
    setup {
      featureClient(FeatureApi.Post(orgGuid = orgRep.guid, rep = FeatureRepFixtures.homeFixture.creation()))
    }

    test(expectError = FeatureRankIsNotUnique()) {
      featureClient(FeatureApi.Post(
          orgGuid = orgRep.guid,
          rep = FeatureRepFixtures.formsFixture.creation().copy(rank = FeatureRepFixtures.homeFixture.creation().rank),
      ))
    }

    test(expectResult = orgRep) {
      orgClient(OrgApi.Get(orgRep.guid))
    }
  }

  @Test
  fun duplicatePath() {
    var orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
    setup {
      orgClient(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation()))
    }

    orgRep = orgRep.copy(features = orgRep.features + FeatureRepFixtures.homeFixture.complete(this, orgRep.guid, 1))
    setup {
      featureClient(FeatureApi.Post(
          orgGuid = orgRep.guid,
          rep = FeatureRepFixtures.homeFixture.creation(),
      ))
    }

    test(expectError = FeaturePathIsNotUnique()) {
      featureClient(FeatureApi.Post(
          orgGuid = orgRep.guid,
          rep = FeatureRepFixtures.formsFixture.creation().copy(path = FeatureRepFixtures.homeFixture.creation().path),
      ))
    }

    test(expectResult = orgRep) {
      orgClient(OrgApi.Get(orgRep.guid))
    }
  }

  @Test
  fun happyPath() {
    var orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
    setup {
      orgClient(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation()))
    }

    val featureRep = FeatureRepFixtures.formsFixture.complete(this, orgRep.guid, 1)
    orgRep = orgRep.copy(features = orgRep.features + featureRep)
    test(expectResult = featureRep) {
      featureClient(FeatureApi.Post(orgRep.guid, FeatureRepFixtures.formsFixture.creation()))
    }

    test(expectResult = orgRep) {
      orgClient(OrgApi.Get(orgRep.guid))
    }
  }
}
