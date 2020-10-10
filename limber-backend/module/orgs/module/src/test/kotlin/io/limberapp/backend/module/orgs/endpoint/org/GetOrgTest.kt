package io.limberapp.backend.module.orgs.endpoint.org

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.orgs.api.org.OrgApi
import io.limberapp.backend.module.orgs.testing.IntegrationTest
import io.limberapp.backend.module.orgs.testing.fixtures.org.OrgRepFixtures
import io.limberapp.common.LimberApplication
import org.junit.jupiter.api.Test
import java.util.*

internal class GetOrgTest(
  engine: TestApplicationEngine,
  limberServer: LimberApplication<*>,
) : IntegrationTest(engine, limberServer) {
  @Test
  fun doesNotExist() {
    val orgGuid = UUID.randomUUID()

    test(expectResult = null) {
      orgClient(OrgApi.Get(orgGuid))
    }
  }

  @Test
  fun happyPath() {
    val orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
    setup { orgClient(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation())) }

    test(expectResult = orgRep) {
      orgClient(OrgApi.Get(orgRep.guid))
    }
  }
}
