package io.limberapp.endpoint.feature

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.api.feature.FeatureApi
import io.limberapp.api.org.OrgApi
import io.limberapp.rep.feature.FeatureRepFixtures
import io.limberapp.rep.org.OrgRepFixtures
import io.limberapp.server.Server
import io.limberapp.testing.integration.IntegrationTest
import org.junit.jupiter.api.Test
import java.util.UUID

internal class DeleteFeatureTest(
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
      featureClient(FeatureApi.Delete(featureGuid))
    }

    test(expectResult = orgRep) {
      orgClient(OrgApi.Get(orgRep.guid))
    }
  }

  @Test
  fun `feature exists`() {
    var orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
    setup {
      orgClient(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation()))
    }

    val homeFeatureRep = FeatureRepFixtures.homeFixture.complete(this, orgRep.guid, 1)
    orgRep = orgRep.copy(features = listOf(homeFeatureRep))
    setup {
      featureClient(FeatureApi.Post(orgRep.guid, FeatureRepFixtures.homeFixture.creation()))
    }

    val formsFeatureRep = FeatureRepFixtures.formsFixture.complete(this, orgRep.guid, 2)
    orgRep = orgRep.copy(features = listOf(homeFeatureRep, formsFeatureRep))
    setup {
      featureClient(FeatureApi.Post(orgRep.guid, FeatureRepFixtures.formsFixture.creation()))
    }

    orgRep = orgRep.copy(features = listOf(homeFeatureRep))
    test(expectResult = Unit) {
      featureClient(FeatureApi.Delete(formsFeatureRep.guid))
    }

    test(expectResult = orgRep) {
      orgClient(OrgApi.Get(orgRep.guid))
    }
  }
}
