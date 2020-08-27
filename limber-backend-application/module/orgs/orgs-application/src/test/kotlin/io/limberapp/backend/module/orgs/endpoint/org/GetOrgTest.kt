package io.limberapp.backend.module.orgs.endpoint.org

import io.limberapp.backend.module.orgs.api.org.OrgApi
import io.limberapp.backend.module.orgs.exception.org.OrgNotFound
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import io.limberapp.backend.module.orgs.testing.fixtures.org.OrgRepFixtures
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

internal class GetOrgTest : ResourceTest() {
  @Test
  fun doesNotExist() {
    val orgGuid = UUID.randomUUID()

    piperTest.test(
      endpoint = OrgApi.Get(orgGuid),
      expectedException = OrgNotFound()
    )
  }

  @Test
  fun happyPath() {
    val ownerUserGuid = UUID.randomUUID()

    val orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, ownerUserGuid, 0)
    piperTest.setup(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation(ownerUserGuid)))

    piperTest.test(OrgApi.Get(orgRep.guid)) {
      val actual = json.parse<OrgRep.Complete>(response.content!!)
      assertEquals(orgRep, actual)
    }
  }
}
