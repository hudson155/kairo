package io.limberapp.backend.module.auth.endpoint.tenant

import com.piperframework.testing.responseContent
import io.limberapp.backend.module.auth.api.tenant.TenantApi
import io.limberapp.backend.module.auth.exception.tenant.Auth0ClientIdAlreadyRegistered
import io.limberapp.backend.module.auth.exception.tenant.OrgAlreadyHasTenant
import io.limberapp.backend.module.auth.exception.tenant.TenantDomainAlreadyRegistered
import io.limberapp.backend.module.auth.rep.tenant.TenantRep
import io.limberapp.backend.module.auth.testing.ResourceTest
import io.limberapp.backend.module.auth.testing.fixtures.tenant.TenantRepFixtures
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

internal class PostTenantTest : ResourceTest() {
  @Test
  fun duplicateOrgGuid() {
    val limberappOrgGuid = UUID.randomUUID()
    val someclientOrgGuid = UUID.randomUUID()

    val limberappTenantRep = TenantRepFixtures.limberappFixture.complete(this, limberappOrgGuid)
    piperTest.setup(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(limberappOrgGuid)))

    piperTest.test(
      endpoint = TenantApi.Post(
        TenantRepFixtures.someclientFixture.creation(someclientOrgGuid)
          .copy(orgGuid = limberappTenantRep.orgGuid)
      ),
      expectedException = OrgAlreadyHasTenant()
    )
  }

  @Test
  fun duplicateAuth0ClientId() {
    val limberappOrgGuid = UUID.randomUUID()
    val someclientOrgGuid = UUID.randomUUID()

    val limberappTenantRep = TenantRepFixtures.limberappFixture.complete(this, limberappOrgGuid)
    piperTest.setup(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(limberappOrgGuid)))

    piperTest.test(
      endpoint = TenantApi.Post(
        TenantRepFixtures.someclientFixture.creation(someclientOrgGuid)
          .copy(auth0ClientId = limberappTenantRep.auth0ClientId)
      ),
      expectedException = Auth0ClientIdAlreadyRegistered()
    )
  }

  @Test
  fun duplicateDomain() {
    val limberappOrgGuid = UUID.randomUUID()
    val someclientOrgGuid = UUID.randomUUID()

    piperTest.setup(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(limberappOrgGuid)))

    val duplicateDomain = TenantRepFixtures.limberappFixture.creation(limberappOrgGuid).domain
    piperTest.test(
      endpoint = TenantApi.Post(
        TenantRepFixtures.someclientFixture.creation(someclientOrgGuid)
          .copy(domain = duplicateDomain)
      ),
      expectedException = TenantDomainAlreadyRegistered()
    )
  }

  @Test
  fun happyPath() {
    val limberappOrgGuid = UUID.randomUUID()
    val someclientOrgGuid = UUID.randomUUID()

    val limberappTenantRep = TenantRepFixtures.limberappFixture.complete(this, limberappOrgGuid)
    piperTest.test(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(limberappOrgGuid))) {
      val actual = json.parse<TenantRep.Complete>(responseContent)
      assertEquals(limberappTenantRep, actual)
    }

    val someclientTenantRep = TenantRepFixtures.someclientFixture.complete(this, someclientOrgGuid)
    piperTest.test(TenantApi.Post(TenantRepFixtures.someclientFixture.creation(someclientOrgGuid))) {
      val actual = json.parse<TenantRep.Complete>(responseContent)
      assertEquals(someclientTenantRep, actual)
    }

    piperTest.test(TenantApi.Get(limberappOrgGuid)) {
      val actual = json.parse<TenantRep.Complete>(responseContent)
      assertEquals(limberappTenantRep, actual)
    }

    piperTest.test(TenantApi.Get(someclientOrgGuid)) {
      val actual = json.parse<TenantRep.Complete>(responseContent)
      assertEquals(someclientTenantRep, actual)
    }
  }
}
