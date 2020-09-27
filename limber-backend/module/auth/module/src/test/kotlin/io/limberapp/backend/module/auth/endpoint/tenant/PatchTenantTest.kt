package io.limberapp.backend.module.auth.endpoint.tenant

import io.limberapp.backend.module.auth.api.tenant.TenantApi
import io.limberapp.backend.module.auth.exception.tenant.Auth0ClientIdAlreadyRegistered
import io.limberapp.backend.module.auth.exception.tenant.TenantNotFound
import io.limberapp.backend.module.auth.rep.tenant.TenantRep
import io.limberapp.backend.module.auth.testing.ResourceTest
import io.limberapp.backend.module.auth.testing.fixtures.tenant.TenantRepFixtures
import io.limberapp.common.testing.responseContent
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

internal class PatchTenantTest : ResourceTest() {
  @Test
  fun doesNotExist() {
    val orgGuid = UUID.randomUUID()

    limberTest.test(
      endpoint = TenantApi.Patch(orgGuid, TenantRep.Update(auth0ClientId = "zyxwvutsrqponmlkjihgfedcbazyxwvu")),
      expectedException = TenantNotFound()
    )
  }

  @Test
  fun duplicateAuth0ClientId() {
    val limberappOrgGuid = UUID.randomUUID()
    val someclientOrgGuid = UUID.randomUUID()

    val limberappTenantRep = TenantRepFixtures.limberappFixture.complete(this, limberappOrgGuid)
    limberTest.setup(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(limberappOrgGuid)))

    limberTest.setup(TenantApi.Post(TenantRepFixtures.someclientFixture.creation(someclientOrgGuid)))

    limberTest.test(
      endpoint = TenantApi.Patch(someclientOrgGuid, TenantRep.Update(auth0ClientId = limberappTenantRep.auth0ClientId)),
      expectedException = Auth0ClientIdAlreadyRegistered()
    )
  }

  @Test
  fun happyPathName() {
    val orgGuid = UUID.randomUUID()

    val originalTenantRep = TenantRepFixtures.limberappFixture.complete(this, orgGuid)
    var tenantRep = TenantRepFixtures.limberappFixture.complete(this, orgGuid)
    limberTest.setup(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(orgGuid)))

    tenantRep = tenantRep.copy(name = "new tenant (display) name")
    limberTest.test(TenantApi.Patch(originalTenantRep.orgGuid,
      TenantRep.Update(name = "new tenant (display) name"))) {
      val actual = json.parse<TenantRep.Complete>(responseContent)
      assertEquals(tenantRep, actual)
    }

    limberTest.test(TenantApi.Get(orgGuid)) {
      val actual = json.parse<TenantRep.Complete>(responseContent)
      assertEquals(tenantRep, actual)
    }
  }

  @Test
  fun happyPathAuth0ClientId() {
    val orgGuid = UUID.randomUUID()

    val originalTenantRep = TenantRepFixtures.limberappFixture.complete(this, orgGuid)
    var tenantRep = TenantRepFixtures.limberappFixture.complete(this, orgGuid)
    limberTest.setup(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(orgGuid)))

    tenantRep = tenantRep.copy(auth0ClientId = "zyxwvutsrqponmlkjihgfedcbazyxwvu")
    limberTest.test(TenantApi.Patch(originalTenantRep.orgGuid,
      TenantRep.Update(auth0ClientId = "zyxwvutsrqponmlkjihgfedcbazyxwvu"))) {
      val actual = json.parse<TenantRep.Complete>(responseContent)
      assertEquals(tenantRep, actual)
    }

    limberTest.test(TenantApi.Get(orgGuid)) {
      val actual = json.parse<TenantRep.Complete>(responseContent)
      assertEquals(tenantRep, actual)
    }
  }
}
