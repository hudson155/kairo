package io.limberapp.backend.module.auth.endpoint.tenant

import io.limberapp.backend.module.auth.exception.tenant.TenantDomainAlreadyRegistered
import io.limberapp.backend.module.auth.rep.tenant.TenantRep
import io.limberapp.backend.module.auth.testing.ResourceTest
import io.limberapp.backend.module.auth.testing.fixtures.tenant.TenantRepFixtures
import kotlinx.serialization.parse
import kotlinx.serialization.stringify
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class PostTenantTest : ResourceTest() {

    @Test
    fun duplicateDomain() {

        // Setup
        val org0Id = UUID.randomUUID()
        val org1Id = UUID.randomUUID()

        // PostTenant
        val limberappTenantRep = TenantRepFixtures.limberappFixture.complete(this, org0Id)
        piperTest.setup(
            endpointConfig = PostTenant.endpointConfig,
            body = json.stringify(TenantRepFixtures.limberappFixture.creation(org0Id))
        )

        // PostTenant
        piperTest.test(
            endpointConfig = PostTenant.endpointConfig,
            body = json.stringify(
                TenantRepFixtures.someclientFixture.creation(org1Id).copy(domain = limberappTenantRep.domain)
            ),
            expectedException = TenantDomainAlreadyRegistered(limberappTenantRep.domain)
        )
    }

    @Test
    fun duplicateOrgId() {

        // Setup
        val org0Id = UUID.randomUUID()
        val org1Id = UUID.randomUUID()

        // PostTenant
        val tenant0Rep = TenantRepFixtures.limberappFixture.complete(this, org0Id)
        piperTest.setup(
            endpointConfig = PostTenant.endpointConfig,
            body = json.stringify(TenantRepFixtures.limberappFixture.creation(org0Id))
        )

        // PostTenant
        val tenant1Rep = TenantRepFixtures.someclientFixture.complete(this, org1Id).copy(orgId = tenant0Rep.orgId)
        piperTest.test(
            endpointConfig = PostTenant.endpointConfig,
            body = json.stringify(TenantRepFixtures.someclientFixture.creation(org1Id).copy(orgId = tenant0Rep.orgId))
        ) {
            val actual = json.parse<TenantRep.Complete>(response.content!!)
            assertEquals(tenant1Rep, actual)
        }

        // GetTenant
        piperTest.test(
            endpointConfig = GetTenant.endpointConfig,
            pathParams = mapOf(GetTenant.tenantDomain to tenant0Rep.domain)
        ) {
            val actual = json.parse<TenantRep.Complete>(response.content!!)
            assertEquals(tenant0Rep, actual)
        }

        // GetTenant
        piperTest.test(
            endpointConfig = GetTenant.endpointConfig,
            pathParams = mapOf(GetTenant.tenantDomain to tenant1Rep.domain)
        ) {
            val actual = json.parse<TenantRep.Complete>(response.content!!)
            assertEquals(tenant1Rep, actual)
        }
    }

    @Test
    fun duplicateAuth0ClientId() {

        // Setup
        val org0Id = UUID.randomUUID()
        val org1Id = UUID.randomUUID()

        // PostTenant
        val tenant0Rep = TenantRepFixtures.limberappFixture.complete(this, org0Id)
        piperTest.setup(
            endpointConfig = PostTenant.endpointConfig,
            body = json.stringify(TenantRepFixtures.limberappFixture.creation(org0Id))
        )

        // PostTenant
        val tenant1Rep = TenantRepFixtures.someclientFixture.complete(this, org1Id)
            .copy(auth0ClientId = tenant0Rep.auth0ClientId)
        piperTest.test(
            endpointConfig = PostTenant.endpointConfig,
            body = json.stringify(
                TenantRepFixtures.someclientFixture.creation(org1Id).copy(auth0ClientId = tenant0Rep.auth0ClientId)
            )
        ) {
            val actual = json.parse<TenantRep.Complete>(response.content!!)
            assertEquals(tenant1Rep, actual)
        }

        // GetTenant
        piperTest.test(
            endpointConfig = GetTenant.endpointConfig,
            pathParams = mapOf(GetTenant.tenantDomain to tenant0Rep.domain)
        ) {
            val actual = json.parse<TenantRep.Complete>(response.content!!)
            assertEquals(tenant0Rep, actual)
        }

        // GetTenant
        piperTest.test(
            endpointConfig = GetTenant.endpointConfig,
            pathParams = mapOf(GetTenant.tenantDomain to tenant1Rep.domain)
        ) {
            val actual = json.parse<TenantRep.Complete>(response.content!!)
            assertEquals(tenant1Rep, actual)
        }
    }

    @Test
    fun happyPath() {

        // Setup
        val org0Id = UUID.randomUUID()
        val org1Id = UUID.randomUUID()

        // PostTenant
        val tenant0Rep = TenantRepFixtures.limberappFixture.complete(this, org0Id)
        piperTest.test(
            endpointConfig = PostTenant.endpointConfig,
            body = json.stringify(TenantRepFixtures.limberappFixture.creation(org0Id))
        ) {
            val actual = json.parse<TenantRep.Complete>(response.content!!)
            assertEquals(tenant0Rep, actual)
        }

        // PostTenant
        val tenant1Rep = TenantRepFixtures.someclientFixture.complete(this, org1Id)
        piperTest.test(
            endpointConfig = PostTenant.endpointConfig,
            body = json.stringify(TenantRepFixtures.someclientFixture.creation(org1Id))
        ) {
            val actual = json.parse<TenantRep.Complete>(response.content!!)
            assertEquals(tenant1Rep, actual)
        }

        // GetTenant
        piperTest.test(
            endpointConfig = GetTenant.endpointConfig,
            pathParams = mapOf(GetTenant.tenantDomain to tenant0Rep.domain)
        ) {
            val actual = json.parse<TenantRep.Complete>(response.content!!)
            assertEquals(tenant0Rep, actual)
        }

        // GetTenant
        piperTest.test(
            endpointConfig = GetTenant.endpointConfig,
            pathParams = mapOf(GetTenant.tenantDomain to tenant1Rep.domain)
        ) {
            val actual = json.parse<TenantRep.Complete>(response.content!!)
            assertEquals(tenant1Rep, actual)
        }
    }
}
