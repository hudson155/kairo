package io.limberapp.backend.module.auth.endpoint.tenant.domain

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.auth.api.tenant.TenantApi
import io.limberapp.backend.module.auth.api.tenant.TenantDomainApi
import io.limberapp.backend.module.auth.testing.IntegrationTest
import io.limberapp.backend.module.auth.testing.fixtures.tenant.TenantDomainRepFixtures
import io.limberapp.backend.module.auth.testing.fixtures.tenant.TenantRepFixtures
import io.limberapp.common.LimberApplication
import org.junit.jupiter.api.Test
import java.util.*

internal class DeleteTenantDomainTest(
    engine: TestApplicationEngine,
    limberServer: LimberApplication<*>,
) : IntegrationTest(engine, limberServer) {
  @Test
  fun orgDoesNotExist() {
    val orgGuid = UUID.randomUUID()
    val tenantDomain = "fakedomain.com"

    test(expectResult = null) {
      tenantDomainClient(TenantDomainApi.Delete(orgGuid, tenantDomain))
    }
  }

  @Test
  fun domainDoesNotExist() {
    val orgGuid = UUID.randomUUID()
    val tenantDomain = "fakedomain.com"

    val tenantRep = TenantRepFixtures.limberappFixture.complete(this, orgGuid)
    setup {
      tenantClient(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(orgGuid)))
    }

    test(expectResult = null) {
      tenantDomainClient(TenantDomainApi.Delete(orgGuid, tenantDomain))
    }

    test(expectResult = tenantRep) {
      tenantClient(TenantApi.Get(orgGuid))
    }
  }

  @Test
  fun happyPath() {
    val orgGuid = UUID.randomUUID()

    var tenantRep = TenantRepFixtures.limberappFixture.complete(this, orgGuid)
    setup {
      tenantClient(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(orgGuid)))
    }

    val tenantDomainRep = TenantDomainRepFixtures.someclientFixture.complete(this)
    tenantRep = tenantRep.copy(domains = tenantRep.domains + tenantDomainRep)
    test(expectResult = tenantDomainRep) {
      tenantDomainClient(TenantDomainApi.Post(orgGuid, TenantDomainRepFixtures.someclientFixture.creation()))
    }

    tenantRep = tenantRep.copy(domains = tenantRep.domains - tenantDomainRep)
    test(expectResult = Unit) {
      tenantDomainClient(TenantDomainApi.Delete(orgGuid, tenantDomainRep.domain))
    }

    test(expectResult = tenantRep) {
      tenantClient(TenantApi.Get(orgGuid))
    }
  }
}
