package io.limberapp.backend.module.orgs.endpoint.org

import io.limberapp.backend.module.orgs.api.org.OrgApi
import io.limberapp.backend.module.orgs.exception.org.OrgNotFound
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import io.limberapp.backend.module.orgs.testing.fixtures.org.OrgRepFixtures
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

internal class GetByOwnerUserGuidTest : ResourceTest() {
  @Test
  fun noOrg() {
    val ownerUserGuid = UUID.randomUUID()

    piperTest.test(
      endpoint = OrgApi.GetByOwnerUserGuid(ownerUserGuid),
      expectedException = OrgNotFound()
    )
  }

  @Test
  fun happyPath() {
    val ownerUserGuid = UUID.randomUUID()

    val crankyPastaOrgRep = OrgRepFixtures.crankyPastaFixture.complete(this, ownerUserGuid, 0)
    piperTest.setup(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation(ownerUserGuid)))

    piperTest.test(OrgApi.GetByOwnerUserGuid(ownerUserGuid)) {
      val actual = json.parse<OrgRep.Complete>(response.content!!)
      assertEquals(crankyPastaOrgRep, actual)
    }
  }
}
