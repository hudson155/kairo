package io.limberapp.endpoint.tenant

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.api.tenant.TenantApi
import io.limberapp.rep.tenant.TenantRepFixtures
import io.limberapp.server.Server
import io.limberapp.testing.integration.IntegrationTest
import org.junit.jupiter.api.Test
import java.util.UUID

internal class DeleteTenantTest(
    engine: TestApplicationEngine,
    server: Server<*>,
) : IntegrationTest(engine, server) {
  @Test
  fun `tenant does not exist`() {
    val orgGuid = UUID.randomUUID()

    test(expectResult = null) {
      tenantClient(TenantApi.Delete(orgGuid))
    }
  }

  @Test
  fun `tenant exists`() {
    val orgGuid = UUID.randomUUID()

    val tenantRep = TenantRepFixtures.limberappFixture.complete(this, orgGuid)
    setup {
      tenantClient(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(orgGuid)))
    }

    test(expectResult = Unit) {
      tenantClient(TenantApi.Delete(tenantRep.orgGuid))
    }

    test(expectResult = null) {
      tenantClient(TenantApi.Get(orgGuid))
    }
  }
}
