package io.limberapp.backend.module.auth.endpoint.tenant

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

        // Setup
        val limberappOrgId = UUID.randomUUID()
        val someclientOrgId = UUID.randomUUID()

        // PostTenant
        val limberappTenantRep = TenantRepFixtures.limberappFixture.complete(this, limberappOrgId)
        piperTest.setup(
            endpointConfig = PostTenant.endpointConfig,
            body = TenantRepFixtures.limberappFixture.creation(limberappOrgId)
        )

        // PostTenant
        piperTest.test(
            endpointConfig = PostTenant.endpointConfig,
            body = TenantRepFixtures.someclientFixture.creation(someclientOrgId).copy(orgId = limberappTenantRep.orgId),
            expectedException = OrgAlreadyHasTenant(limberappTenantRep.orgId)
        )
    }

    @Test
    fun duplicateAuth0ClientId() {

        // Setup
        val limberappOrgId = UUID.randomUUID()
        val someclientOrgId = UUID.randomUUID()

        // PostTenant
        val limberappTenantRep = TenantRepFixtures.limberappFixture.complete(this, limberappOrgId)
        piperTest.setup(
            endpointConfig = PostTenant.endpointConfig,
            body = TenantRepFixtures.limberappFixture.creation(limberappOrgId)
        )

        // PostTenant
        piperTest.test(
            endpointConfig = PostTenant.endpointConfig,
            body = TenantRepFixtures.someclientFixture.creation(someclientOrgId).copy(
                auth0ClientId = limberappTenantRep.auth0ClientId
            ),
            expectedException = Auth0ClientIdAlreadyRegistered(limberappTenantRep.auth0ClientId)
        )
    }

    @Test
    fun duplicateDomain() {

        // Setup
        val limberappOrgId = UUID.randomUUID()
        val someclientOrgId = UUID.randomUUID()

        // PostTenant
        piperTest.setup(
            endpointConfig = PostTenant.endpointConfig,
            body = TenantRepFixtures.limberappFixture.creation(limberappOrgId)
        )

        // PostTenant
        val duplicateDomain = TenantRepFixtures.limberappFixture.creation(limberappOrgId).domain
        piperTest.test(
            endpointConfig = PostTenant.endpointConfig,
            body = TenantRepFixtures.someclientFixture.creation(someclientOrgId).copy(domain = duplicateDomain),
            expectedException = TenantDomainAlreadyRegistered(duplicateDomain.domain)
        )
    }

    @Test
    fun happyPath() {

        // Setup
        val limberappOrgId = UUID.randomUUID()
        val someclientOrgId = UUID.randomUUID()

        // PostTenant
        val limberappTenantRep = TenantRepFixtures.limberappFixture.complete(this, limberappOrgId)
        piperTest.test(
            endpointConfig = PostTenant.endpointConfig,
            body = TenantRepFixtures.limberappFixture.creation(limberappOrgId)
        ) {
            val actual = json.parse<TenantRep.Complete>(response.content!!)
            assertEquals(limberappTenantRep, actual)
        }

        // PostTenant
        val someclientTenantRep = TenantRepFixtures.someclientFixture.complete(this, someclientOrgId)
        piperTest.test(
            endpointConfig = PostTenant.endpointConfig,
            body = TenantRepFixtures.someclientFixture.creation(someclientOrgId)
        ) {
            val actual = json.parse<TenantRep.Complete>(response.content!!)
            assertEquals(someclientTenantRep, actual)
        }

        // GetTenant
        piperTest.test(
            endpointConfig = GetTenant.endpointConfig,
            pathParams = mapOf(GetTenant.orgId to limberappOrgId)
        ) {
            val actual = json.parse<TenantRep.Complete>(response.content!!)
            assertEquals(limberappTenantRep, actual)
        }

        // GetTenant
        piperTest.test(
            endpointConfig = GetTenant.endpointConfig,
            pathParams = mapOf(GetTenant.orgId to someclientOrgId)
        ) {
            val actual = json.parse<TenantRep.Complete>(response.content!!)
            assertEquals(someclientTenantRep, actual)
        }
    }
}
