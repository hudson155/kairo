package io.limberapp.backend.module.orgs.endpoint.feature

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.orgs.api.feature.FeatureApi
import io.limberapp.backend.module.orgs.api.org.OrgApi
import io.limberapp.backend.module.orgs.testing.IntegrationTest
import io.limberapp.backend.module.orgs.testing.fixtures.feature.FeatureRepFixtures
import io.limberapp.backend.module.orgs.testing.fixtures.org.OrgRepFixtures
import io.limberapp.common.LimberApplication
import org.junit.jupiter.api.Test
import java.util.*

internal class GetFeatureTest(
    engine: TestApplicationEngine,
    limberServer: LimberApplication<*>,
) : IntegrationTest(engine, limberServer) {
  @Test
  fun featureDoesNotExist() {
    val featureGuid = UUID.randomUUID()

    test(expectResult = null) {
      featureClient(FeatureApi.Get(featureGuid))
    }
  }

  @Test
  fun happyPath() {
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
