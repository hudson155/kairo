package io.limberapp.backend.module.auth.endpoint.tenant

import io.limberapp.backend.module.auth.exception.tenant.Auth0ClientIdAlreadyRegistered
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
        val orgId = UUID.randomUUID()

        // PatchTenant
        val tenantUpdateRep = TenantRep.Update(
            auth0ClientId = "zyxwvutsrqponmlkjihgfedcbazyxwvu"
        )
        piperTest.test(
            endpointConfig = PatchTenant.endpointConfig,
            body = tenantUpdateRep,
            pathParams = mapOf(PatchTenant.orgId to orgId),
            expectedException = TenantNotFound()
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
        piperTest.setup(
            endpointConfig = PostTenant.endpointConfig,
            body = TenantRepFixtures.someclientFixture.creation(someclientOrgId)
        )

        // PatchTenant
        val tenantUpdateRep = TenantRep.Update(auth0ClientId = limberappTenantRep.auth0ClientId)
        piperTest.test(
            endpointConfig = PatchTenant.endpointConfig,
            body = tenantUpdateRep,
            pathParams = mapOf(PatchTenant.orgId to someclientOrgId),
            expectedException = Auth0ClientIdAlreadyRegistered(limberappTenantRep.auth0ClientId)
        )
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
            auth0ClientId = "zyxwvutsrqponmlkjihgfedcbazyxwvu"
        )
        tenantRep = tenantRep.copy(auth0ClientId = tenantUpdateRep.auth0ClientId!!)
        piperTest.test(
            endpointConfig = PatchTenant.endpointConfig,
            body = tenantUpdateRep,
            pathParams = mapOf(PatchTenant.orgId to originalTenantRep.orgId)
        ) {
            val actual = json.parse<TenantRep.Complete>(response.content!!)
            assertEquals(tenantRep, actual)
        }

        // GetTenant
        piperTest.test(
            endpointConfig = GetTenant.endpointConfig,
            pathParams = mapOf(GetTenant.orgId to orgId)
        ) {
            val actual = json.parse<TenantRep.Complete>(response.content!!)
            assertEquals(tenantRep, actual)
        }
    }
}
