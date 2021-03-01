package io.limberapp.endpoint.tenant

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.api.tenant.TenantApi
import io.limberapp.api.tenant.TenantDomainApi
import io.limberapp.exception.tenant.TenantDomainAlreadyRegistered
import io.limberapp.exception.tenant.TenantNotFound
import io.limberapp.exception.unprocessable
import io.limberapp.rep.tenant.TenantDomainRepFixtures
import io.limberapp.rep.tenant.TenantRepFixtures
import io.limberapp.server.Server
import io.limberapp.testing.integration.IntegrationTest
import org.junit.jupiter.api.Test
import java.util.UUID

internal class PostTenantDomainTest(
    engine: TestApplicationEngine,
    server: Server<*>,
) : IntegrationTest(engine, server) {
  @Test
  fun `tenant does not exist`() {
    val orgGuid = UUID.randomUUID()

    test(expectError = TenantNotFound().unprocessable()) {
      tenantDomainClient(TenantDomainApi.Post(
          orgGuid = orgGuid,
          rep = TenantDomainRepFixtures.limberappFixture.creation(),
      ))
    }
  }

  @Test
  fun `duplicate domain`() {
    val limberappOrgGuid = UUID.randomUUID()
    val someclientOrgGuid = UUID.randomUUID()

    setup {
      tenantClient(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(limberappOrgGuid)))
    }

    setup {
      tenantDomainClient(TenantDomainApi.Post(
          orgGuid = limberappOrgGuid,
          rep = TenantDomainRepFixtures.limberappFixture.creation(),
      ))
    }

    val someclientTenantRep = TenantRepFixtures.someclientFixture.complete(this, someclientOrgGuid)
    setup {
      tenantClient(TenantApi.Post(TenantRepFixtures.someclientFixture.creation(someclientOrgGuid)))
    }

    test(expectError = TenantDomainAlreadyRegistered()) {
      tenantDomainClient(TenantDomainApi.Post(
          orgGuid = limberappOrgGuid,
          rep = TenantDomainRepFixtures.limberappFixture.creation(),
      ))
    }

    test(expectResult = someclientTenantRep) { tenantClient(TenantApi.Get(someclientOrgGuid)) }
  }

  @Test
  fun `happy path`() {
    val orgGuid = UUID.randomUUID()

    var tenantRep = TenantRepFixtures.limberappFixture.complete(this, orgGuid)
    setup { tenantClient(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(orgGuid))) }

    val limberappTenantDomainRep = TenantDomainRepFixtures.limberappFixture.complete(this)
    tenantRep = tenantRep.copy(domains = setOf(limberappTenantDomainRep))
    test(expectResult = limberappTenantDomainRep) {
      tenantDomainClient(TenantDomainApi.Post(
          orgGuid = orgGuid,
          rep = TenantDomainRepFixtures.limberappFixture.creation(),
      ))
    }

    val someclientTenantDomainRep = TenantDomainRepFixtures.someclientFixture.complete(this)
    tenantRep = tenantRep.copy(domains = setOf(limberappTenantDomainRep, someclientTenantDomainRep))
    test(expectResult = someclientTenantDomainRep) {
      tenantDomainClient(TenantDomainApi.Post(
          orgGuid = orgGuid,
          rep = TenantDomainRepFixtures.someclientFixture.creation(),
      ))
    }

    test(expectResult = tenantRep) { tenantClient(TenantApi.Get(orgGuid)) }
  }
}
