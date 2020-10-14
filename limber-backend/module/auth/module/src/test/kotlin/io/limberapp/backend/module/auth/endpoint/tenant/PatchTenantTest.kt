package io.limberapp.backend.module.auth.endpoint.tenant

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.auth.api.tenant.TenantApi
import io.limberapp.backend.module.auth.exception.tenant.Auth0ClientIdAlreadyRegistered
import io.limberapp.backend.module.auth.rep.tenant.TenantRep
import io.limberapp.backend.module.auth.testing.IntegrationTest
import io.limberapp.backend.module.auth.testing.fixtures.tenant.TenantRepFixtures
import io.limberapp.common.LimberApplication
import org.junit.jupiter.api.Test
import java.util.*

internal class PatchTenantTest(
    engine: TestApplicationEngine,
    limberServer: LimberApplication<*>,
) : IntegrationTest(engine, limberServer) {
  @Test
  fun doesNotExist() {
    val orgGuid = UUID.randomUUID()

    test(expectResult = null) {
      tenantClient(TenantApi.Patch(orgGuid, TenantRep.Update(auth0ClientId = "zyxwvutsrqponmlkjihgfedcbazyxwvu")))
    }
  }

  @Test
  fun duplicateAuth0ClientId() {
    val limberappOrgGuid = UUID.randomUUID()
    val someclientOrgGuid = UUID.randomUUID()

    val limberappTenantRep = TenantRepFixtures.limberappFixture.complete(this, limberappOrgGuid)
    setup {
      tenantClient(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(limberappOrgGuid)))
    }

    setup {
      tenantClient(TenantApi.Post(TenantRepFixtures.someclientFixture.creation(someclientOrgGuid)))
    }

    test(expectError = Auth0ClientIdAlreadyRegistered()) {
      tenantClient(TenantApi.Patch(
          orgGuid = someclientOrgGuid,
          rep = TenantRep.Update(auth0ClientId = limberappTenantRep.auth0ClientId)
      ))
    }
  }

  @Test
  fun happyPathName() {
    val orgGuid = UUID.randomUUID()

    val originalTenantRep = TenantRepFixtures.limberappFixture.complete(this, orgGuid)
    var tenantRep = TenantRepFixtures.limberappFixture.complete(this, orgGuid)
    setup {
      tenantClient(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(orgGuid)))
    }

    tenantRep = tenantRep.copy(name = "new tenant (display) name")
    test(expectResult = tenantRep) {
      tenantClient(TenantApi.Patch(
          orgGuid = originalTenantRep.orgGuid,
          rep = TenantRep.Update(name = "new tenant (display) name")
      ))
    }

    test(expectResult = tenantRep) {
      tenantClient(TenantApi.Get(orgGuid))
    }
  }

  @Test
  fun happyPathAuth0ClientId() {
    val orgGuid = UUID.randomUUID()

    val originalTenantRep = TenantRepFixtures.limberappFixture.complete(this, orgGuid)
    var tenantRep = TenantRepFixtures.limberappFixture.complete(this, orgGuid)
    setup {
      tenantClient(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(orgGuid)))
    }

    tenantRep = tenantRep.copy(auth0ClientId = "zyxwvutsrqponmlkjihgfedcbazyxwvu")
    test(expectResult = tenantRep) {
      tenantClient(TenantApi.Patch(
          orgGuid = originalTenantRep.orgGuid,
          rep = TenantRep.Update(auth0ClientId = "zyxwvutsrqponmlkjihgfedcbazyxwvu")
      ))
    }

    test(expectResult = tenantRep) {
      tenantClient(TenantApi.Get(orgGuid))
    }
  }
}
