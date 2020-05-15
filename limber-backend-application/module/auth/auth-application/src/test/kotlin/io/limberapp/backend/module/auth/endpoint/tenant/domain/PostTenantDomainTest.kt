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
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

internal class PostTenantDomainTest : ResourceTest() {
  @Test
  fun orgDoesNotExist() {
    val orgGuid = UUID.randomUUID()

    piperTest.test(
      endpoint = TenantDomainApi.Post(orgGuid, TenantDomainRepFixtures.limberappFixture.creation()),
      expectedException = TenantNotFound()
    )
  }

  @Test
  fun duplicateDomain() {
    val limberappOrgGuid = UUID.randomUUID()
    val someclientOrgGuid = UUID.randomUUID()

    val limberappTenantRep = TenantRepFixtures.limberappFixture.complete(this, limberappOrgGuid)
    piperTest.setup(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(limberappOrgGuid)))

    piperTest.setup(TenantApi.Post(TenantRepFixtures.someclientFixture.creation(someclientOrgGuid)))

    piperTest.test(
      endpoint = TenantDomainApi.Post(limberappOrgGuid, TenantDomainRepFixtures.someclientFixture.creation()),
      expectedException = TenantDomainAlreadyRegistered()
    )

    piperTest.test(TenantApi.Get(limberappOrgGuid)) {
      val actual = json.parse<TenantRep.Complete>(response.content!!)
      assertEquals(limberappTenantRep, actual)
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

    piperTest.test(TenantApi.Get(orgGuid)) {
      val actual = json.parse<TenantRep.Complete>(response.content!!)
      assertEquals(tenantRep, actual)
    }
  }
}
