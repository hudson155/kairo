package io.limberapp.backend.module.auth.endpoint.tenant.domain

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.auth.api.tenant.TenantApi
import io.limberapp.backend.module.auth.api.tenant.domain.TenantDomainApi
import io.limberapp.backend.module.auth.exception.tenant.TenantDomainAlreadyRegistered
import io.limberapp.backend.module.auth.exception.tenant.TenantNotFound
import io.limberapp.backend.module.auth.rep.tenant.TenantDomainRep
import io.limberapp.backend.module.auth.rep.tenant.TenantRep
import io.limberapp.backend.module.auth.testing.IntegrationTest
import io.limberapp.backend.module.auth.testing.fixtures.tenant.TenantDomainRepFixtures
import io.limberapp.backend.module.auth.testing.fixtures.tenant.TenantRepFixtures
import io.limberapp.common.LimberApplication
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

internal class PostTenantDomainTest(
  engine: TestApplicationEngine,
  limberServer: LimberApplication<*>,
) : IntegrationTest(engine, limberServer) {
  @Test
  fun orgDoesNotExist() {
    val orgGuid = UUID.randomUUID()

    test(
      endpoint = TenantDomainApi.Post(orgGuid, TenantDomainRepFixtures.limberappFixture.creation()),
      expectedException = TenantNotFound()
    )
  }

  @Test
  fun duplicateDomain() {
    val limberappOrgGuid = UUID.randomUUID()
    val someclientOrgGuid = UUID.randomUUID()

    val limberappTenantRep = TenantRepFixtures.limberappFixture.complete(this, limberappOrgGuid)
    setup(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(limberappOrgGuid)))

    setup(TenantApi.Post(TenantRepFixtures.someclientFixture.creation(someclientOrgGuid)))

    test(
      endpoint = TenantDomainApi.Post(limberappOrgGuid, TenantDomainRepFixtures.someclientFixture.creation()),
      expectedException = TenantDomainAlreadyRegistered()
    )

    test(TenantApi.Get(limberappOrgGuid)) {
      val actual = json.parse<TenantRep.Complete>(responseContent)
      assertEquals(limberappTenantRep, actual)
    }
  }

  @Test
  fun happyPath() {
    val orgGuid = UUID.randomUUID()

    var tenantRep = TenantRepFixtures.limberappFixture.complete(this, orgGuid)
    setup(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(orgGuid)))

    val tenantDomainRep = TenantDomainRepFixtures.someclientFixture.complete(this)
    tenantRep = tenantRep.copy(domains = tenantRep.domains + tenantDomainRep)
    test(TenantDomainApi.Post(orgGuid, TenantDomainRepFixtures.someclientFixture.creation())) {
      val actual = json.parse<TenantDomainRep.Complete>(responseContent)
      assertEquals(tenantDomainRep, actual)
    }

    test(TenantApi.Get(orgGuid)) {
      val actual = json.parse<TenantRep.Complete>(responseContent)
      assertEquals(tenantRep, actual)
    }
  }
}
