package io.limberapp.backend.module.auth.endpoint.tenant

import io.limberapp.backend.module.auth.api.tenant.TenantApi
import io.limberapp.backend.module.auth.exception.tenant.Auth0ClientIdAlreadyRegistered
import io.limberapp.backend.module.auth.exception.tenant.OrgAlreadyHasTenant
import io.limberapp.backend.module.auth.exception.tenant.TenantDomainAlreadyRegistered
import io.limberapp.backend.module.auth.rep.tenant.TenantRep
import io.limberapp.backend.module.auth.testing.ResourceTest
import io.limberapp.backend.module.auth.testing.fixtures.tenant.TenantRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class PostTenantTest : ResourceTest() {
    @Test
    fun duplicateOrgId() {
        val limberappOrgId = UUID.randomUUID()
        val someclientOrgId = UUID.randomUUID()

        val limberappTenantRep = TenantRepFixtures.limberappFixture.complete(this, limberappOrgId)
        piperTest.setup(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(limberappOrgId)))

        piperTest.test(
            endpoint = TenantApi.Post(
                TenantRepFixtures.someclientFixture.creation(someclientOrgId)
                    .copy(orgId = limberappTenantRep.orgId)
            ),
            expectedException = OrgAlreadyHasTenant(limberappTenantRep.orgId)
        )
    }

    @Test
    fun duplicateAuth0ClientId() {
        val limberappOrgId = UUID.randomUUID()
        val someclientOrgId = UUID.randomUUID()

        val limberappTenantRep = TenantRepFixtures.limberappFixture.complete(this, limberappOrgId)
        piperTest.setup(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(limberappOrgId)))

        piperTest.test(
            endpoint = TenantApi.Post(
                TenantRepFixtures.someclientFixture.creation(someclientOrgId)
                    .copy(auth0ClientId = limberappTenantRep.auth0ClientId)
            ),
            expectedException = Auth0ClientIdAlreadyRegistered(limberappTenantRep.auth0ClientId)
        )
    }

    @Test
    fun duplicateDomain() {
        val limberappOrgId = UUID.randomUUID()
        val someclientOrgId = UUID.randomUUID()

        piperTest.setup(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(limberappOrgId)))

        val duplicateDomain = TenantRepFixtures.limberappFixture.creation(limberappOrgId).domain
        piperTest.test(
            endpoint = TenantApi.Post(
                TenantRepFixtures.someclientFixture.creation(someclientOrgId)
                    .copy(domain = duplicateDomain)
            ),
            expectedException = TenantDomainAlreadyRegistered(duplicateDomain.domain)
        )
    }

    @Test
    fun happyPath() {
        val limberappOrgId = UUID.randomUUID()
        val someclientOrgId = UUID.randomUUID()

        val limberappTenantRep = TenantRepFixtures.limberappFixture.complete(this, limberappOrgId)
        piperTest.test(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(limberappOrgId))) {
            val actual = json.parse<TenantRep.Complete>(response.content!!)
            assertEquals(limberappTenantRep, actual)
        }

        val someclientTenantRep = TenantRepFixtures.someclientFixture.complete(this, someclientOrgId)
        piperTest.test(TenantApi.Post(TenantRepFixtures.someclientFixture.creation(someclientOrgId))) {
            val actual = json.parse<TenantRep.Complete>(response.content!!)
            assertEquals(someclientTenantRep, actual)
        }

        piperTest.test(TenantApi.Get(limberappOrgId)) {
            val actual = json.parse<TenantRep.Complete>(response.content!!)
            assertEquals(limberappTenantRep, actual)
        }

        piperTest.test(TenantApi.Get(someclientOrgId)) {
            val actual = json.parse<TenantRep.Complete>(response.content!!)
            assertEquals(someclientTenantRep, actual)
        }
    }
}
