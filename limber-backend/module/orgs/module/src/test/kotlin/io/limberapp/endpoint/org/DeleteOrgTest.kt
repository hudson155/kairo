package io.limberapp.endpoint.org

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.api.org.OrgApi
import io.limberapp.rep.org.OrgRepFixtures
import io.limberapp.server.Server
import io.limberapp.testing.integration.IntegrationTest
import org.junit.jupiter.api.Test
import java.util.UUID

internal class DeleteOrgTest(
    engine: TestApplicationEngine,
    server: Server<*>,
) : IntegrationTest(engine, server) {
  @Test
  fun `org does not exist`() {
    val orgGuid = UUID.randomUUID()

    test(expectResult = null) {
      orgClient(OrgApi.Delete(orgGuid))
    }
  }

  @Test
  fun `org exists`() {
    val orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
    setup {
      orgClient(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation()))
    }

    test(expectResult = Unit) {
      orgClient(OrgApi.Delete(orgRep.guid))
    }

    test(expectResult = null) {
      orgClient(OrgApi.Get(orgRep.guid))
    }
  }
}
