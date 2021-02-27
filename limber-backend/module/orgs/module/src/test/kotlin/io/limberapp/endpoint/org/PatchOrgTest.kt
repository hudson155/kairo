package io.limberapp.endpoint.org

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.api.org.OrgApi
import io.limberapp.exception.org.UserAlreadyOwnsOrg
import io.limberapp.rep.org.OrgRep
import io.limberapp.rep.org.OrgRepFixtures
import io.limberapp.server.Server
import io.limberapp.testing.integration.IntegrationTest
import org.junit.jupiter.api.Test
import java.util.UUID

internal class PatchOrgTest(
    engine: TestApplicationEngine,
    server: Server<*>,
) : IntegrationTest(engine, server) {
  @Test
  fun `org does not exist`() {
    val orgGuid = UUID.randomUUID()

    test(expectResult = null) {
      orgClient(OrgApi.Patch(orgGuid, OrgRep.Update("Standing Teeth")))
    }
  }

  @Test
  fun `name changed`() {
    var orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
    setup {
      orgClient(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation()))
    }

    orgRep = orgRep.copy(name = "Standing Teeth")
    test(expectResult = orgRep) {
      orgClient(OrgApi.Patch(orgRep.guid, OrgRep.Update(name = "Standing Teeth")))
    }

    test(expectResult = orgRep) {
      orgClient(OrgApi.Get(orgRep.guid))
    }
  }

  @Test
  fun `user already owns org`() {
    val ownerUserGuid = UUID.randomUUID()

    var crankyPastaOrgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
    setup {
      orgClient(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation()))
    }

    val dynamicTennisOrgRep = OrgRepFixtures.dynamicTennisFixture.complete(this, 1)
    setup {
      orgClient(OrgApi.Post(OrgRepFixtures.dynamicTennisFixture.creation()))
    }

    crankyPastaOrgRep = crankyPastaOrgRep.copy(ownerUserGuid = ownerUserGuid)
    setup {
      orgClient(OrgApi.Patch(
          orgGuid = crankyPastaOrgRep.guid,
          rep = OrgRep.Update(ownerUserGuid = ownerUserGuid),
      ))
    }

    test(expectError = UserAlreadyOwnsOrg()) {
      orgClient(OrgApi.Patch(
          orgGuid = dynamicTennisOrgRep.guid,
          rep = OrgRep.Update(ownerUserGuid = ownerUserGuid),
      ))
    }
  }

  @Test
  fun `owner changed`() {
    val ownerUserGuid = UUID.randomUUID()

    var orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
    setup {
      orgClient(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation()))
    }

    orgRep = orgRep.copy(ownerUserGuid = ownerUserGuid)
    test(expectResult = orgRep) {
      orgClient(OrgApi.Patch(orgRep.guid, OrgRep.Update(ownerUserGuid = ownerUserGuid)))
    }

    test(expectResult = orgRep) {
      orgClient(OrgApi.Get(orgRep.guid))
    }
  }
}
