package io.limberapp.backend.module.auth.endpoint.tenant.domain

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.auth.api.tenant.TenantApi
import io.limberapp.backend.module.auth.api.tenant.TenantDomainApi
import io.limberapp.backend.module.auth.exception.tenant.TenantDomainAlreadyRegistered
import io.limberapp.backend.module.auth.exception.tenant.TenantNotFound
import io.limberapp.backend.module.auth.testing.IntegrationTest
import io.limberapp.backend.module.auth.testing.fixtures.tenant.TenantDomainRepFixtures
import io.limberapp.backend.module.auth.testing.fixtures.tenant.TenantRepFixtures
import io.limberapp.common.LimberApplication
import io.limberapp.common.exception.unprocessableEntity.unprocessable
import org.junit.jupiter.api.Test
import java.util.*

internal class PostTenantDomainTest(
    engine: TestApplicationEngine,
    limberServer: LimberApplication<*>,
) : IntegrationTest(engine, limberServer) {
  @Test
  fun orgDoesNotExist() {
    val orgGuid = UUID.randomUUID()

    test(expectError = TenantNotFound().unprocessable()) {
      tenantDomainClient(TenantDomainApi.Post(orgGuid, TenantDomainRepFixtures.limberappFixture.creation()))
    }
  }

  @Test
  fun duplicateDomain() {
    val limberappOrgGuid = UUID.randomUUID()
    val someclientOrgGuid = UUID.randomUUID()

    val limberappTenantRep = TenantRepFixtures.limberappFixture.complete(this, limberappOrgGuid)
    setup {
      tenantClient(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(limberappOrgGuid)))
    }

    setup {
      tenantClient(TenantApi.Post(TenantRepFixtures.someclientFixture.creation(someclientOrgGuid)))
    }

    test(expectError = TenantDomainAlreadyRegistered()) {
      tenantDomainClient(TenantDomainApi.Post(limberappOrgGuid, TenantDomainRepFixtures.someclientFixture.creation()))
    }

    test(expectResult = limberappTenantRep) {
      tenantClient(TenantApi.Get(limberappOrgGuid))
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

    test(expectResult = tenantRep) {
      tenantClient(TenantApi.Get(orgGuid))
    }
  }
}
