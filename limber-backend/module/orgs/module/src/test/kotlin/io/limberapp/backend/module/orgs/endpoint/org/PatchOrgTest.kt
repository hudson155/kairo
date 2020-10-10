package io.limberapp.backend.module.orgs.endpoint.org

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.orgs.api.org.OrgApi
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.testing.IntegrationTest
import io.limberapp.backend.module.orgs.testing.fixtures.org.OrgRepFixtures
import io.limberapp.common.LimberApplication
import org.junit.jupiter.api.Test
import java.util.*

internal class PatchOrgTest(
  engine: TestApplicationEngine,
  limberServer: LimberApplication<*>,
) : IntegrationTest(engine, limberServer) {
  @Test
  fun doesNotExist() {
    val orgGuid = UUID.randomUUID()

    test(expectResult = null) {
      orgClient(OrgApi.Patch(orgGuid, OrgRep.Update("Standing Teeth")))
    }
  }

  @Test
  fun happyPathName() {
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
  fun happyPathOwnerUserGuid() {
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
