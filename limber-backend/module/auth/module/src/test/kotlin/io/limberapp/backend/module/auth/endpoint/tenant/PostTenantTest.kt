package io.limberapp.backend.module.auth.endpoint.tenant

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.auth.api.tenant.TenantApi
import io.limberapp.backend.module.auth.exception.tenant.Auth0ClientIdAlreadyRegistered
import io.limberapp.backend.module.auth.exception.tenant.OrgAlreadyHasTenant
import io.limberapp.backend.module.auth.exception.tenant.TenantDomainAlreadyRegistered
import io.limberapp.backend.module.auth.testing.IntegrationTest
import io.limberapp.backend.module.auth.testing.fixtures.tenant.TenantRepFixtures
import io.limberapp.common.LimberApplication
import org.junit.jupiter.api.Test
import java.util.*

internal class PostTenantTest(
    engine: TestApplicationEngine,
    limberServer: LimberApplication<*>,
) : IntegrationTest(engine, limberServer) {
  @Test
  fun duplicateOrgGuid() {
    val limberappOrgGuid = UUID.randomUUID()
    val someclientOrgGuid = UUID.randomUUID()

    val limberappTenantRep = TenantRepFixtures.limberappFixture.complete(this, limberappOrgGuid)
    setup {
      tenantClient(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(limberappOrgGuid)))
    }

    test(expectError = OrgAlreadyHasTenant()) {
      tenantClient(TenantApi.Post(
          rep = TenantRepFixtures.someclientFixture.creation(someclientOrgGuid)
              .copy(orgGuid = limberappTenantRep.orgGuid)
      ))
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

    test(expectError = Auth0ClientIdAlreadyRegistered()) {
      tenantClient(TenantApi.Post(
          rep = TenantRepFixtures.someclientFixture.creation(someclientOrgGuid)
              .copy(auth0ClientId = limberappTenantRep.auth0ClientId)
      ))
    }
  }

  @Test
  fun duplicateDomain() {
    val limberappOrgGuid = UUID.randomUUID()
    val someclientOrgGuid = UUID.randomUUID()

    setup {
      tenantClient(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(limberappOrgGuid)))
    }

    val duplicateDomain = TenantRepFixtures.limberappFixture.creation(limberappOrgGuid).domain
    test(expectError = TenantDomainAlreadyRegistered()) {
      tenantClient(TenantApi.Post(
          rep = TenantRepFixtures.someclientFixture.creation(someclientOrgGuid)
              .copy(domain = duplicateDomain)
      ))
    }
  }

  @Test
  fun happyPath() {
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
