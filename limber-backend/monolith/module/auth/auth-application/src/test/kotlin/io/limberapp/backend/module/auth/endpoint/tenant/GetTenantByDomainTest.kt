package io.limberapp.backend.module.auth.endpoint.tenant

import com.piperframework.testing.responseContent
import io.limberapp.backend.module.auth.api.tenant.TenantApi
import io.limberapp.backend.module.auth.exception.tenant.TenantNotFound
import io.limberapp.backend.module.auth.rep.tenant.TenantRep
import io.limberapp.backend.module.auth.testing.ResourceTest
import io.limberapp.backend.module.auth.testing.fixtures.tenant.TenantRepFixtures
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

internal class GetTenantByDomainTest : ResourceTest() {
  @Test
  fun doesNotExist() {
    val tenantDomain = "fakedomain.com"

    piperTest.test(
      endpoint = TenantApi.GetByDomain(tenantDomain),
      expectedException = TenantNotFound()
    )
  }

  @Test
  fun happyPath() {
    val orgGuid = UUID.randomUUID()

    val tenantRep = TenantRepFixtures.limberappFixture.complete(this, orgGuid)
    piperTest.setup(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(orgGuid)))

    piperTest.test(TenantApi.GetByDomain(tenantRep.domains.single().domain)) {
      val actual = json.parse<TenantRep.Complete>(responseContent)
      assertEquals(tenantRep, actual)
    }
  }
}
