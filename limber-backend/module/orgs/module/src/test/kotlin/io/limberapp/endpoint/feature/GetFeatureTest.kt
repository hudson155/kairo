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

internal class GetFeatureTest(
    engine: TestApplicationEngine,
    server: Server<*>,
) : IntegrationTest(engine, server) {
  @Test
  fun `feature does not exist`() {
    val featureGuid = UUID.randomUUID()

    test(expectResult = null) {
      featureClient(FeatureApi.Get(featureGuid))
    }
  }

  @Test
  fun `feature exists`() {
    val orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
    setup {
      orgClient(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation()))
    }

    val homeFeatureRep = FeatureRepFixtures.homeFixture.complete(this, orgRep.guid, 1)
    setup {
      featureClient(FeatureApi.Post(orgRep.guid, FeatureRepFixtures.homeFixture.creation()))
    }

    test(expectResult = homeFeatureRep) {
      featureClient(FeatureApi.Get(homeFeatureRep.guid))
    }
  }
}
