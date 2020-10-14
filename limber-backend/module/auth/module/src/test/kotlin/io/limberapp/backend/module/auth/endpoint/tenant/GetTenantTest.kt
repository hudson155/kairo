package io.limberapp.backend.module.auth.endpoint.tenant

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.auth.api.tenant.TenantApi
import io.limberapp.backend.module.auth.testing.IntegrationTest
import io.limberapp.backend.module.auth.testing.fixtures.tenant.TenantRepFixtures
import io.limberapp.common.LimberApplication
import org.junit.jupiter.api.Test
import java.util.*

internal class GetTenantTest(
    engine: TestApplicationEngine,
    limberServer: LimberApplication<*>,
) : IntegrationTest(engine, limberServer) {
  @Test
  fun doesNotExist() {
    val orgGuid = UUID.randomUUID()

    test(expectResult = null) {
      tenantClient(TenantApi.Get(orgGuid))
    }
  }

  @Test
  fun happyPath() {
    val orgGuid = UUID.randomUUID()

    val tenantRep = TenantRepFixtures.limberappFixture.complete(this, orgGuid)
    setup {
      tenantClient(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(orgGuid)))
    }

    test(expectResult = tenantRep) {
      tenantClient(TenantApi.Get(orgGuid))
    }
  }
}
