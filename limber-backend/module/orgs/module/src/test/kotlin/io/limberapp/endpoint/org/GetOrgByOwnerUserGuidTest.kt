package io.limberapp.endpoint.org

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.api.org.OrgApi
import io.limberapp.rep.org.OrgRep
import io.limberapp.rep.org.OrgRepFixtures
import io.limberapp.server.Server
import io.limberapp.testing.integration.IntegrationTest
import org.junit.jupiter.api.Test
import java.util.UUID

internal class GetOrgByOwnerUserGuidTest(
    engine: TestApplicationEngine,
    server: Server<*>,
) : IntegrationTest(engine, server) {
  @Test
  fun `org does not exist`() {
    val ownerUserGuid = UUID.randomUUID()

    test(expectResult = null) {
      orgClient(OrgApi.GetByOwnerUserGuid(ownerUserGuid))
    }
  }

  @Test
  fun `org exists`() {
    val ownerUserGuid = UUID.randomUUID()

    var crankyPastaOrgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
    setup {
      orgClient(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation()))
    }

    crankyPastaOrgRep = crankyPastaOrgRep.copy(ownerUserGuid = ownerUserGuid)
    setup {
      orgClient(OrgApi.Patch(crankyPastaOrgRep.guid, OrgRep.Update(ownerUserGuid = ownerUserGuid)))
    }

    test(expectResult = crankyPastaOrgRep) {
      orgClient(OrgApi.GetByOwnerUserGuid(ownerUserGuid))
    }
  }
}
