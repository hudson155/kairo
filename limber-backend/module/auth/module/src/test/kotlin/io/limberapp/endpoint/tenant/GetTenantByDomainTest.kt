package io.limberapp.endpoint.tenant

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.api.tenant.TenantApi
import io.limberapp.api.tenant.TenantDomainApi
import io.limberapp.rep.tenant.TenantDomainRepFixtures
import io.limberapp.rep.tenant.TenantRepFixtures
import io.limberapp.server.Server
import io.limberapp.testing.integration.IntegrationTest
import org.junit.jupiter.api.Test
import java.util.UUID

internal class GetTenantByDomainTest(
    engine: TestApplicationEngine,
    server: Server<*>,
) : IntegrationTest(engine, server) {
  @Test
  fun `tenant and domain don't exist`() {
    val tenantDomain = "fakedomain.com"

    test(expectResult = null) { tenantClient(TenantApi.GetByDomain(tenantDomain)) }
  }

  @Test
  fun `tenant and domain exist`() {
    val orgGuid = UUID.randomUUID()

    var tenantRep = TenantRepFixtures.limberappFixture.complete(this, orgGuid)
    setup { tenantClient(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(orgGuid))) }

    val tenantDomainRep = TenantDomainRepFixtures.limberappFixture.complete(this)
    tenantRep = tenantRep.copy(domains = setOf(tenantDomainRep))
    setup {
      tenantDomainClient(TenantDomainApi.Post(
          orgGuid = orgGuid,
          rep = TenantDomainRepFixtures.limberappFixture.creation(),
      ))
    }

    test(expectResult = tenantRep) { tenantClient(TenantApi.GetByDomain(tenantDomainRep.domain)) }
  }
}
