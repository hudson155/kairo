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

internal class DeleteTenantDomainTest(
    engine: TestApplicationEngine,
    server: Server<*>,
) : IntegrationTest(engine, server) {
  @Test
  fun `org does not exist`() {
    val orgGuid = UUID.randomUUID()
    val tenantDomain = "fakedomain.com"

    test(expectResult = null) {
      tenantDomainClient(TenantDomainApi.Delete(orgGuid, tenantDomain))
    }
  }

  @Test
  fun `domain does not exist`() {
    val orgGuid = UUID.randomUUID()

    val tenantRep = TenantRepFixtures.limberappFixture.complete(this, orgGuid)
    setup {
      tenantClient(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(orgGuid)))
    }

    test(expectResult = null) {
      tenantDomainClient(TenantDomainApi.Delete(
          orgGuid = orgGuid,
          domain = TenantDomainRepFixtures.limberappFixture.complete(this).domain,
      ))
    }

    test(expectResult = tenantRep) {
      tenantClient(TenantApi.Get(orgGuid))
    }
  }

  @Test
  fun `domain exists`() {
    val orgGuid = UUID.randomUUID()

    var tenantRep = TenantRepFixtures.limberappFixture.complete(this, orgGuid)
    setup {
      tenantClient(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(orgGuid)))
    }

    val limberappTenantDomainRep = TenantDomainRepFixtures.limberappFixture.complete(this)
    tenantRep = tenantRep.copy(domains = setOf(limberappTenantDomainRep))
    setup {
      tenantDomainClient(TenantDomainApi.Post(
          orgGuid = orgGuid,
          rep = TenantDomainRepFixtures.limberappFixture.creation(),
      ))
    }

    val someclientTenantDomainRep = TenantDomainRepFixtures.someclientFixture.complete(this)
    tenantRep = tenantRep.copy(domains = setOf(limberappTenantDomainRep, someclientTenantDomainRep))
    setup {
      tenantDomainClient(TenantDomainApi.Post(
          orgGuid = orgGuid,
          rep = TenantDomainRepFixtures.someclientFixture.creation(),
      ))
    }

    tenantRep = tenantRep.copy(domains = setOf(limberappTenantDomainRep))
    test(expectResult = Unit) {
      tenantDomainClient(TenantDomainApi.Delete(orgGuid, someclientTenantDomainRep.domain))
    }

    test(expectResult = tenantRep) {
      tenantClient(TenantApi.Get(orgGuid))
    }
  }
}
