package io.limberapp.backend.module.auth.endpoint.tenant.domain

import io.limberapp.backend.module.auth.api.tenant.TenantApi
import io.limberapp.backend.module.auth.api.tenant.domain.TenantDomainApi
import io.limberapp.backend.module.auth.exception.tenant.TenantDomainNotFound
import io.limberapp.backend.module.auth.rep.tenant.TenantDomainRep
import io.limberapp.backend.module.auth.rep.tenant.TenantRep
import io.limberapp.backend.module.auth.testing.ResourceTest
import io.limberapp.backend.module.auth.testing.fixtures.tenant.TenantDomainRepFixtures
import io.limberapp.backend.module.auth.testing.fixtures.tenant.TenantRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class DeleteTenantDomainTest : ResourceTest() {
  @Test
  fun orgDoesNotExist() {
    val orgGuid = UUID.randomUUID()
    val tenantDomain = "fakedomain.com"

    piperTest.test(
      endpoint = TenantDomainApi.Delete(orgGuid, tenantDomain),
      expectedException = TenantDomainNotFound()
    )
  }

  @Test
  fun domainDoesNotExist() {
    val orgGuid = UUID.randomUUID()
    val tenantDomain = "fakedomain.com"

    val tenantRep = TenantRepFixtures.limberappFixture.complete(this, orgGuid)
    piperTest.setup(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(orgGuid)))

    piperTest.test(
      endpoint = TenantDomainApi.Delete(orgGuid, tenantDomain),
      expectedException = TenantDomainNotFound()
    )

    piperTest.test(TenantApi.Get(orgGuid)) {
      val actual = json.parse<TenantRep.Complete>(response.content!!)
      assertEquals(tenantRep, actual)
    }
  }

  @Test
  fun happyPath() {
    val orgGuid = UUID.randomUUID()

    var tenantRep = TenantRepFixtures.limberappFixture.complete(this, orgGuid)
    piperTest.setup(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(orgGuid)))

    val tenantDomainRep = TenantDomainRepFixtures.someclientFixture.complete(this)
    tenantRep = tenantRep.copy(domains = tenantRep.domains.plus(tenantDomainRep))
    piperTest.test(TenantDomainApi.Post(orgGuid, TenantDomainRepFixtures.someclientFixture.creation())) {
      val actual = json.parse<TenantDomainRep.Complete>(response.content!!)
      assertEquals(tenantDomainRep, actual)
    }

    tenantRep = tenantRep.copy(domains = tenantRep.domains.minus(tenantDomainRep))
    piperTest.test(TenantDomainApi.Delete(orgGuid, tenantDomainRep.domain)) {}

    piperTest.test(TenantApi.Get(orgGuid)) {
      val actual = json.parse<TenantRep.Complete>(response.content!!)
      assertEquals(tenantRep, actual)
    }
  }
}
