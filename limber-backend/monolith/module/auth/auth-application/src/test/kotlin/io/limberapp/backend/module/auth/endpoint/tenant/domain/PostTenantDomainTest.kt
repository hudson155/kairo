package io.limberapp.backend.module.auth.endpoint.tenant.domain

import io.limberapp.backend.module.auth.api.tenant.TenantApi
import io.limberapp.backend.module.auth.api.tenant.domain.TenantDomainApi
import io.limberapp.backend.module.auth.exception.tenant.TenantDomainAlreadyRegistered
import io.limberapp.backend.module.auth.exception.tenant.TenantNotFound
import io.limberapp.backend.module.auth.rep.tenant.TenantDomainRep
import io.limberapp.backend.module.auth.rep.tenant.TenantRep
import io.limberapp.backend.module.auth.testing.ResourceTest
import io.limberapp.backend.module.auth.testing.fixtures.tenant.TenantDomainRepFixtures
import io.limberapp.backend.module.auth.testing.fixtures.tenant.TenantRepFixtures
import io.limberapp.common.testing.responseContent
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

internal class PostTenantDomainTest : ResourceTest() {
  @Test
  fun orgDoesNotExist() {
    val orgGuid = UUID.randomUUID()

    limberTest.test(
      endpoint = TenantDomainApi.Post(orgGuid, TenantDomainRepFixtures.limberappFixture.creation()),
      expectedException = TenantNotFound()
    )
  }

  @Test
  fun duplicateDomain() {
    val limberappOrgGuid = UUID.randomUUID()
    val someclientOrgGuid = UUID.randomUUID()

    val limberappTenantRep = TenantRepFixtures.limberappFixture.complete(this, limberappOrgGuid)
    limberTest.setup(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(limberappOrgGuid)))

    limberTest.setup(TenantApi.Post(TenantRepFixtures.someclientFixture.creation(someclientOrgGuid)))

    limberTest.test(
      endpoint = TenantDomainApi.Post(limberappOrgGuid, TenantDomainRepFixtures.someclientFixture.creation()),
      expectedException = TenantDomainAlreadyRegistered()
    )

    limberTest.test(TenantApi.Get(limberappOrgGuid)) {
      val actual = json.parse<TenantRep.Complete>(responseContent)
      assertEquals(limberappTenantRep, actual)
    }
  }

  @Test
  fun happyPath() {
    val orgGuid = UUID.randomUUID()

    var tenantRep = TenantRepFixtures.limberappFixture.complete(this, orgGuid)
    limberTest.setup(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(orgGuid)))

    val tenantDomainRep = TenantDomainRepFixtures.someclientFixture.complete(this)
    tenantRep = tenantRep.copy(domains = tenantRep.domains + tenantDomainRep)
    limberTest.test(TenantDomainApi.Post(orgGuid, TenantDomainRepFixtures.someclientFixture.creation())) {
      val actual = json.parse<TenantDomainRep.Complete>(responseContent)
      assertEquals(tenantDomainRep, actual)
    }

    limberTest.test(TenantApi.Get(orgGuid)) {
      val actual = json.parse<TenantRep.Complete>(responseContent)
      assertEquals(tenantRep, actual)
    }
  }
}
