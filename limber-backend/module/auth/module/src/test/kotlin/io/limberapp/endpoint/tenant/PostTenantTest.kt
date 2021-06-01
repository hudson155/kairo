package io.limberapp.endpoint.tenant

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.api.tenant.TenantApi
import io.limberapp.exception.tenant.Auth0OrgIdAlreadyRegistered
import io.limberapp.exception.tenant.OrgAlreadyHasTenant
import io.limberapp.rep.tenant.TenantRepFixtures
import io.limberapp.server.Server
import io.limberapp.testing.integration.IntegrationTest
import org.junit.jupiter.api.Test
import java.util.UUID

internal class PostTenantTest(
    engine: TestApplicationEngine,
    server: Server<*>,
) : IntegrationTest(engine, server) {
  @Test
  fun `duplicate org guid`() {
    val limberappOrgGuid = UUID.randomUUID()
    val someclientOrgGuid = UUID.randomUUID()

    val limberappTenantRep = TenantRepFixtures.limberappFixture.complete(this, limberappOrgGuid)
    setup {
      tenantClient(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(limberappOrgGuid)))
    }

    test(expectError = OrgAlreadyHasTenant()) {
      tenantClient(TenantApi.Post(
          rep = TenantRepFixtures.someclientFixture.creation(someclientOrgGuid)
              .copy(orgGuid = limberappTenantRep.orgGuid),
      ))
    }
  }

  @Test
  fun `duplicate auth0 org id`() {
    val limberappOrgGuid = UUID.randomUUID()
    val someclientOrgGuid = UUID.randomUUID()

    val limberappTenantRep = TenantRepFixtures.limberappFixture.complete(this, limberappOrgGuid)
    setup {
      tenantClient(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(limberappOrgGuid)))
    }

    test(expectError = Auth0OrgIdAlreadyRegistered()) {
      tenantClient(TenantApi.Post(
          rep = TenantRepFixtures.someclientFixture.creation(someclientOrgGuid)
              .copy(auth0OrgId = limberappTenantRep.auth0OrgId),
      ))
    }
  }

  @Test
  fun `happy path`() {
    val limberappOrgGuid = UUID.randomUUID()
    val someclientOrgGuid = UUID.randomUUID()

    val limberappTenantRep = TenantRepFixtures.limberappFixture.complete(this, limberappOrgGuid)
    test(expectResult = limberappTenantRep) {
      tenantClient(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(limberappOrgGuid)))
    }

    val someclientTenantRep = TenantRepFixtures.someclientFixture.complete(this, someclientOrgGuid)
    test(expectResult = someclientTenantRep) {
      tenantClient(TenantApi.Post(TenantRepFixtures.someclientFixture.creation(someclientOrgGuid)))
    }

    test(expectResult = limberappTenantRep) {
      tenantClient(TenantApi.Get(limberappOrgGuid))
    }

    test(expectResult = someclientTenantRep) {
      tenantClient(TenantApi.Get(someclientOrgGuid))
    }
  }
}
