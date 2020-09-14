package io.limberapp.backend.module.auth.endpoint.tenant

import io.limberapp.backend.module.auth.api.tenant.TenantApi
import io.limberapp.backend.module.auth.exception.tenant.TenantNotFound
import io.limberapp.backend.module.auth.testing.ResourceTest
import io.limberapp.backend.module.auth.testing.fixtures.tenant.TenantRepFixtures
import org.junit.jupiter.api.Test
import java.util.*

internal class DeleteTenantTest : ResourceTest() {
  @Test
  fun doesNotExist() {
    val orgGuid = UUID.randomUUID()

    limberTest.test(
      endpoint = TenantApi.Delete(orgGuid),
      expectedException = TenantNotFound()
    )
  }

  @Test
  fun happyPath() {
    val orgGuid = UUID.randomUUID()

    val tenantRep = TenantRepFixtures.limberappFixture.complete(this, orgGuid)
    limberTest.setup(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(orgGuid)))

    limberTest.test(TenantApi.Delete(tenantRep.orgGuid)) {}

    limberTest.test(
      endpoint = TenantApi.Get(orgGuid),
      expectedException = TenantNotFound()
    )
  }
}
