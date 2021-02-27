package io.limberapp.endpoint.org

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.api.org.OrgApi
import io.limberapp.rep.org.OrgRepFixtures
import io.limberapp.server.Server
import io.limberapp.testing.integration.IntegrationTest
import org.junit.jupiter.api.Test

internal class PostOrgTest(
    engine: TestApplicationEngine,
    server: Server<*>,
) : IntegrationTest(engine, server) {
  @Test
  fun `happy path`() {
    val orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
    test(expectResult = orgRep) {
      orgClient(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation()))
    }

    test(expectResult = orgRep) {
      orgClient(OrgApi.Get(orgRep.guid))
    }
  }
}
