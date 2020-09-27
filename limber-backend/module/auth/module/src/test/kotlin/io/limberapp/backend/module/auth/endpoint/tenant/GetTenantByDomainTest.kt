package io.limberapp.backend.module.auth.endpoint.tenant

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.auth.api.tenant.TenantApi
import io.limberapp.backend.module.auth.exception.tenant.TenantNotFound
import io.limberapp.backend.module.auth.rep.tenant.TenantRep
import io.limberapp.backend.module.auth.testing.IntegrationTest
import io.limberapp.backend.module.auth.testing.fixtures.tenant.TenantRepFixtures
import io.limberapp.common.LimberApplication
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

internal class GetTenantByDomainTest(
  engine: TestApplicationEngine,
  limberServer: LimberApplication<*>,
) : IntegrationTest(engine, limberServer) {
  @Test
  fun doesNotExist() {
    val tenantDomain = "fakedomain.com"

    test(
      endpoint = TenantApi.GetByDomain(tenantDomain),
      expectedException = TenantNotFound()
    )
  }

  @Test
  fun happyPath() {
    val orgGuid = UUID.randomUUID()

    val tenantRep = TenantRepFixtures.limberappFixture.complete(this, orgGuid)
    setup(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(orgGuid)))

    test(TenantApi.GetByDomain(tenantRep.domains.single().domain)) {
      val actual = json.parse<TenantRep.Complete>(responseContent)
      assertEquals(tenantRep, actual)
    }
  }
}
