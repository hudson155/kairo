package io.limberapp.endpoint.tenant

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.api.tenant.TenantApi
import io.limberapp.exception.tenant.Auth0ClientIdAlreadyRegistered
import io.limberapp.rep.tenant.TenantRep
import io.limberapp.rep.tenant.TenantRepFixtures
import io.limberapp.server.Server
import io.limberapp.testing.integration.IntegrationTest
import org.junit.jupiter.api.Test
import java.util.UUID

internal class PatchTenantTest(
    engine: TestApplicationEngine,
    server: Server<*>,
) : IntegrationTest(engine, server) {
  @Test
  fun `tenant does not exist`() {
    val orgGuid = UUID.randomUUID()

    test(expectResult = null) {
      tenantClient(TenantApi.Patch(
          orgGuid = orgGuid,
          rep = TenantRep.Update(auth0ClientId = "org_zyxwvutsrqponmlk"),
      ))
    }
  }

  @Test
  fun `auth0 client id - duplicate`() {
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
          rep = TenantRep.Update(auth0ClientId = limberappTenantRep.auth0ClientId),
      ))
    }
  }

  @Test
  fun `auth0 client id - happy path`() {
    val orgGuid = UUID.randomUUID()

    val originalTenantRep = TenantRepFixtures.limberappFixture.complete(this, orgGuid)
    var tenantRep = TenantRepFixtures.limberappFixture.complete(this, orgGuid)
    setup { tenantClient(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(orgGuid))) }

    tenantRep = tenantRep.copy(auth0ClientId = "org_zyxwvutsrqponmlk")
    test(expectResult = tenantRep) {
      tenantClient(TenantApi.Patch(
          orgGuid = originalTenantRep.orgGuid,
          rep = TenantRep.Update(auth0ClientId = "org_zyxwvutsrqponmlk"),
      ))
    }

    test(expectResult = tenantRep) { tenantClient(TenantApi.Get(orgGuid)) }
  }
}
