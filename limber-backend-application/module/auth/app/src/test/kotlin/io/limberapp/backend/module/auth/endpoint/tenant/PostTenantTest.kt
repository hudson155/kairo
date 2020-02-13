package io.limberapp.backend.module.auth.endpoint.tenant

import com.fasterxml.jackson.module.kotlin.readValue
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
    fun duplicateDomain() {

        // Setup
        val org0Id = UUID.randomUUID()
        val org1Id = UUID.randomUUID()

        // PostTenant
        val limberappTenantRep = TenantRepFixtures.limberappFixture.complete(this, org0Id)
        piperTest.setup(
            endpointConfig = PostTenant.endpointConfig,
            body = TenantRepFixtures.limberappFixture.creation(org0Id)
        )

        // PostTenant
        piperTest.test(
            endpointConfig = PostTenant.endpointConfig,
            body = TenantRepFixtures.someclientFixture.creation(org1Id).copy(domain = limberappTenantRep.domain),
            expectedException = TenantDomainAlreadyRegistered(limberappTenantRep.domain)
        )
    }

    @Test
    fun duplicateOrgId() {

        // Setup
        val orgId = UUID.randomUUID()

        // PostTenant
        val limberappTenantRep = TenantRepFixtures.limberappFixture.complete(this, orgId)
        piperTest.setup(
            endpointConfig = PostTenant.endpointConfig,
            body = TenantRepFixtures.limberappFixture.creation(orgId)
        )

        // PostTenant
        piperTest.test(
            endpointConfig = PostTenant.endpointConfig,
            body = TenantRepFixtures.someclientFixture.creation(orgId),
            expectedException = OrgAlreadyHasTenant(limberappTenantRep.orgId)
        )
    }

    @Test
    fun duplicateAuth0ClientId() {

        // Setup
        val org0Id = UUID.randomUUID()
        val org1Id = UUID.randomUUID()

        // PostTenant
        val limberappTenantRep = TenantRepFixtures.limberappFixture.complete(this, org0Id)
        piperTest.setup(
            endpointConfig = PostTenant.endpointConfig,
            body = TenantRepFixtures.limberappFixture.creation(org0Id)
        )

        // PostTenant
        piperTest.test(
            endpointConfig = PostTenant.endpointConfig,
            body = TenantRepFixtures.someclientFixture.creation(org1Id).copy(
                auth0ClientId = limberappTenantRep.auth0ClientId
            ),
            expectedException = Auth0ClientIdAlreadyRegistered(limberappTenantRep.auth0ClientId)
        )
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
            body = TenantRepFixtures.limberappFixture.creation(org0Id)
        ) {
            val actual = objectMapper.readValue<TenantRep.Complete>(response.content!!)
            assertEquals(tenant0Rep, actual)
        }

        // PostTenant
        val tenant1Rep = TenantRepFixtures.someclientFixture.complete(this, org1Id)
        piperTest.test(
            endpointConfig = PostTenant.endpointConfig,
            body = TenantRepFixtures.someclientFixture.creation(org1Id)
        ) {
            val actual = objectMapper.readValue<TenantRep.Complete>(response.content!!)
            assertEquals(tenant1Rep, actual)
        }

        // GetTenant
        piperTest.test(
            endpointConfig = GetTenant.endpointConfig,
            pathParams = mapOf(GetTenant.tenantDomain to tenant0Rep.domain)
        ) {
            val actual = objectMapper.readValue<TenantRep.Complete>(response.content!!)
            assertEquals(tenant0Rep, actual)
        }

        // GetTenant
        piperTest.test(
            endpointConfig = GetTenant.endpointConfig,
            pathParams = mapOf(GetTenant.tenantDomain to tenant1Rep.domain)
        ) {
            val actual = objectMapper.readValue<TenantRep.Complete>(response.content!!)
            assertEquals(tenant1Rep, actual)
        }
    }
}
