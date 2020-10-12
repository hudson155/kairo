package io.limberapp.backend.module.orgs.endpoint.org.feature

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.orgs.api.feature.FeatureApi
import io.limberapp.backend.module.orgs.api.org.OrgApi
import io.limberapp.backend.module.orgs.testing.IntegrationTest
import io.limberapp.backend.module.orgs.testing.fixtures.feature.FeatureRepFixtures
import io.limberapp.backend.module.orgs.testing.fixtures.org.OrgRepFixtures
import io.limberapp.common.LimberApplication
import org.junit.jupiter.api.Test
import java.util.*

internal class DeleteFeatureTest(
  engine: TestApplicationEngine,
  limberServer: LimberApplication<*>,
) : IntegrationTest(engine, limberServer) {
  @Test
  fun orgDoesNotExist() {
    val orgGuid = UUID.randomUUID()
    val featureGuid = UUID.randomUUID()

    test(expectResult = null) {
      featureClient(FeatureApi.Delete(orgGuid, featureGuid))
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
      featureClient(FeatureApi.Delete(orgRep.guid, featureGuid))
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

    val homeFeatureRep = FeatureRepFixtures.homeFixture.complete(this, 1)
    orgRep = orgRep.copy(features = orgRep.features + homeFeatureRep)
    setup {
      featureClient(FeatureApi.Post(orgRep.guid, FeatureRepFixtures.homeFixture.creation()))
    }

    val formsFeatureRep = FeatureRepFixtures.formsFixture.complete(this, 2)
    orgRep = orgRep.copy(features = orgRep.features + formsFeatureRep)
    setup {
      featureClient(FeatureApi.Post(orgRep.guid, FeatureRepFixtures.formsFixture.creation()))
    }

    orgRep = orgRep.copy(features = orgRep.features.filter { it.guid != formsFeatureRep.guid })
    test(expectResult = Unit) {
      featureClient(FeatureApi.Delete(orgRep.guid, formsFeatureRep.guid))
    }

    test(expectResult = orgRep) {
      orgClient(OrgApi.Get(orgRep.guid))
    }
  }
}
