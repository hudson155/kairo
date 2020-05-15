package io.limberapp.backend.module.auth.endpoint.tenant

import io.limberapp.backend.module.auth.api.tenant.TenantApi
import io.limberapp.backend.module.auth.exception.tenant.TenantNotFound
import io.limberapp.backend.module.auth.rep.tenant.TenantRep
import io.limberapp.backend.module.auth.testing.ResourceTest
import io.limberapp.backend.module.auth.testing.fixtures.tenant.TenantRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class GetTenantTest : ResourceTest() {
  @Test
  fun doesNotExist() {
    val orgGuid = UUID.randomUUID()

    piperTest.test(
      endpoint = TenantApi.Get(orgGuid),
      expectedException = TenantNotFound()
    )
  }

  @Test
  fun happyPath() {
    val orgGuid = UUID.randomUUID()

    val tenantRep = TenantRepFixtures.limberappFixture.complete(this, orgGuid)
    piperTest.setup(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(orgGuid)))

    piperTest.test(TenantApi.Get(orgGuid)) {
      val actual = json.parse<TenantRep.Complete>(response.content!!)
      assertEquals(tenantRep, actual)
    }
  }
}
