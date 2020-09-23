package io.limberapp.backend.module.orgs.endpoint.org

import io.limberapp.backend.module.orgs.api.org.OrgApi
import io.limberapp.backend.module.orgs.exception.org.OrgNotFound
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import io.limberapp.backend.module.orgs.testing.fixtures.org.OrgRepFixtures
import io.limberapp.common.testing.responseContent
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

internal class GetByOwnerUserGuidTest : ResourceTest() {
  @Test
  fun noOrg() {
    val ownerUserGuid = UUID.randomUUID()

    limberTest.test(
      endpoint = OrgApi.GetByOwnerUserGuid(ownerUserGuid),
      expectedException = OrgNotFound()
    )
  }

  @Test
  fun happyPath() {
    val ownerUserGuid = UUID.randomUUID()

    var crankyPastaOrgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
    limberTest.setup(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation()))

    crankyPastaOrgRep = crankyPastaOrgRep.copy(ownerUserGuid = ownerUserGuid)
    limberTest.setup(OrgApi.Patch(crankyPastaOrgRep.guid, OrgRep.Update(ownerUserGuid = ownerUserGuid)))

    limberTest.test(OrgApi.GetByOwnerUserGuid(ownerUserGuid)) {
      val actual = json.parse<OrgRep.Complete>(responseContent)
      assertEquals(crankyPastaOrgRep, actual)
    }
  }
}
