package io.limberapp.backend.module.auth.endpoint.tenant

import io.limberapp.backend.module.auth.exception.tenant.TenantDomainAlreadyRegistered
import io.limberapp.backend.module.auth.exception.tenant.TenantNotFound
import io.limberapp.backend.module.auth.rep.tenant.TenantRep
import io.limberapp.backend.module.auth.testing.ResourceTest
import io.limberapp.backend.module.auth.testing.fixtures.tenant.TenantRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class PatchTenantTest : ResourceTest() {

    @Test
    fun doesNotExist() {

        // Setup
        val tenantDomain = "fakedomain.com"

        // PatchTenant
        val tenantUpdateRep = TenantRep.Update(
            domain = "newdomain.com",
            auth0ClientId = "zyxwvutsrqponmlkjihgfedcbazyxwvu"
        )
        piperTest.test(
            endpointConfig = PatchTenant.endpointConfig,
            body = tenantUpdateRep,
            pathParams = mapOf(PatchTenant.tenantDomain to tenantDomain),
            expectedException = TenantNotFound()
        )
    }

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
        val someclientTenantRep = TenantRepFixtures.someclientFixture.complete(this, org1Id)
        piperTest.setup(
            endpointConfig = PostTenant.endpointConfig,
            body = TenantRepFixtures.someclientFixture.creation(org1Id)
        )

        // PatchTenant
        val tenantUpdateRep = TenantRep.Update(domain = limberappTenantRep.domain)
        piperTest.test(
            endpointConfig = PatchTenant.endpointConfig,
            body = tenantUpdateRep,
            pathParams = mapOf(PatchTenant.tenantDomain to someclientTenantRep.domain),
            expectedException = TenantDomainAlreadyRegistered(limberappTenantRep.domain)
        )

        // GetTenant
        piperTest.test(
            endpointConfig = GetTenant.endpointConfig,
            pathParams = mapOf(GetTenant.tenantDomain to someclientTenantRep.domain)
        ) {
            val actual = json.parse<TenantRep.Complete>(response.content!!)
            assertEquals(someclientTenantRep, actual)
        }
    }

    @Test
    fun duplicateOrgId() {

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
        var someclientTenantRep = TenantRepFixtures.someclientFixture.complete(this, org1Id)
        piperTest.setup(
            endpointConfig = PostTenant.endpointConfig,
            body = TenantRepFixtures.someclientFixture.creation(org1Id)
        )

        // PatchTenant
        val tenantUpdateRep = TenantRep.Update(orgId = limberappTenantRep.orgId)
        someclientTenantRep = someclientTenantRep.copy(orgId = tenantUpdateRep.orgId!!)
        piperTest.test(
            endpointConfig = PatchTenant.endpointConfig,
            body = tenantUpdateRep,
            pathParams = mapOf(PatchTenant.tenantDomain to someclientTenantRep.domain)
        ) {
            val actual = json.parse<TenantRep.Complete>(response.content!!)
            assertEquals(someclientTenantRep, actual)
        }

        // GetTenant
        piperTest.test(
            endpointConfig = GetTenant.endpointConfig,
            pathParams = mapOf(GetTenant.tenantDomain to someclientTenantRep.domain)
        ) {
            val actual = json.parse<TenantRep.Complete>(response.content!!)
            assertEquals(someclientTenantRep, actual)
        }
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
        var someclientTenantRep = TenantRepFixtures.someclientFixture.complete(this, org1Id)
        piperTest.setup(
            endpointConfig = PostTenant.endpointConfig,
            body = TenantRepFixtures.someclientFixture.creation(org1Id)
        )

        // PatchTenant
        val tenantUpdateRep = TenantRep.Update(auth0ClientId = limberappTenantRep.auth0ClientId)
        someclientTenantRep = someclientTenantRep.copy(auth0ClientId = tenantUpdateRep.auth0ClientId!!)
        piperTest.test(
            endpointConfig = PatchTenant.endpointConfig,
            body = tenantUpdateRep,
            pathParams = mapOf(PatchTenant.tenantDomain to someclientTenantRep.domain)
        ) {
            val actual = json.parse<TenantRep.Complete>(response.content!!)
            assertEquals(someclientTenantRep, actual)
        }

        // GetTenant
        piperTest.test(
            endpointConfig = GetTenant.endpointConfig,
            pathParams = mapOf(GetTenant.tenantDomain to someclientTenantRep.domain)
        ) {
            val actual = json.parse<TenantRep.Complete>(response.content!!)
            assertEquals(someclientTenantRep, actual)
        }
    }

    @Test
    fun happyPath() {

        // Setup
        val orgId = UUID.randomUUID()

        // PostTenant
        val originalTenantRep = TenantRepFixtures.limberappFixture.complete(this, orgId)
        var tenantRep = TenantRepFixtures.limberappFixture.complete(this, orgId)
        piperTest.setup(
            endpointConfig = PostTenant.endpointConfig,
            body = TenantRepFixtures.limberappFixture.creation(orgId)
        )

        // PatchTenant
        val tenantUpdateRep = TenantRep.Update(
            domain = "newdomain.com",
            auth0ClientId = "zyxwvutsrqponmlkjihgfedcbazyxwvu"
        )
        tenantRep = tenantRep.copy(domain = tenantUpdateRep.domain!!, auth0ClientId = tenantUpdateRep.auth0ClientId!!)
        piperTest.test(
            endpointConfig = PatchTenant.endpointConfig,
            body = tenantUpdateRep,
            pathParams = mapOf(PatchTenant.tenantDomain to originalTenantRep.domain)
        ) {
            val actual = json.parse<TenantRep.Complete>(response.content!!)
            assertEquals(tenantRep, actual)
        }

        // GetTenant
        piperTest.test(
            endpointConfig = GetTenant.endpointConfig,
            pathParams = mapOf(GetTenant.tenantDomain to originalTenantRep.domain),
            expectedException = TenantNotFound()
        )

        // GetTenant
        piperTest.test(
            endpointConfig = GetTenant.endpointConfig,
            pathParams = mapOf(GetTenant.tenantDomain to tenantRep.domain)
        ) {
            val actual = json.parse<TenantRep.Complete>(response.content!!)
            assertEquals(tenantRep, actual)
        }
    }
}
