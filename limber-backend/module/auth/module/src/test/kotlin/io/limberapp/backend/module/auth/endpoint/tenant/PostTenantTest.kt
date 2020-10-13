package io.limberapp.backend.module.auth.endpoint.tenant

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.auth.api.tenant.TenantApi
import io.limberapp.backend.module.auth.exception.tenant.Auth0ClientIdAlreadyRegistered
import io.limberapp.backend.module.auth.exception.tenant.OrgAlreadyHasTenant
import io.limberapp.backend.module.auth.exception.tenant.TenantDomainAlreadyRegistered
import io.limberapp.backend.module.auth.rep.tenant.TenantRep
import io.limberapp.backend.module.auth.testing.IntegrationTest
import io.limberapp.backend.module.auth.testing.fixtures.tenant.TenantRepFixtures
import io.limberapp.common.LimberApplication
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

internal class PostTenantTest(
    engine: TestApplicationEngine,
    limberServer: LimberApplication<*>,
) : IntegrationTest(engine, limberServer) {
  @Test
  fun duplicateOrgGuid() {
    val limberappOrgGuid = UUID.randomUUID()
    val someclientOrgGuid = UUID.randomUUID()

    val limberappTenantRep = TenantRepFixtures.limberappFixture.complete(this, limberappOrgGuid)
    setup(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(limberappOrgGuid)))

    test(
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
    setup(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(limberappOrgGuid)))

    test(
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

    setup(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(limberappOrgGuid)))

    val duplicateDomain = TenantRepFixtures.limberappFixture.creation(limberappOrgGuid).domain
    test(
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
    test(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(limberappOrgGuid))) {
      val actual = json.parse<TenantRep.Complete>(responseContent)
      assertEquals(limberappTenantRep, actual)
    }

    val someclientTenantRep = TenantRepFixtures.someclientFixture.complete(this, someclientOrgGuid)
    test(TenantApi.Post(TenantRepFixtures.someclientFixture.creation(someclientOrgGuid))) {
      val actual = json.parse<TenantRep.Complete>(responseContent)
      assertEquals(someclientTenantRep, actual)
    }

    test(TenantApi.Get(limberappOrgGuid)) {
      val actual = json.parse<TenantRep.Complete>(responseContent)
      assertEquals(limberappTenantRep, actual)
    }

    test(TenantApi.Get(someclientOrgGuid)) {
      val actual = json.parse<TenantRep.Complete>(responseContent)
      assertEquals(someclientTenantRep, actual)
    }
  }
}
