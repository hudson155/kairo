package io.limberapp.backend.module.auth.endpoint.tenant

import io.limberapp.backend.module.auth.api.tenant.TenantApi
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
        val orgId = UUID.randomUUID()

        val tenantUpdateRep = TenantRep.Update(auth0ClientId = "zyxwvutsrqponmlkjihgfedcbazyxwvu")
        piperTest.test(
            endpoint = TenantApi.Patch(orgId, tenantUpdateRep),
            expectedException = TenantNotFound()
        )
    }

    @Test
    fun duplicateAuth0ClientId() {
        val limberappOrgId = UUID.randomUUID()
        val someclientOrgId = UUID.randomUUID()

        val limberappTenantRep = TenantRepFixtures.limberappFixture.complete(this, limberappOrgId)
        piperTest.setup(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(limberappOrgId)))

        piperTest.setup(TenantApi.Post(TenantRepFixtures.someclientFixture.creation(someclientOrgId)))

        val tenantUpdateRep = TenantRep.Update(auth0ClientId = limberappTenantRep.auth0ClientId)
        piperTest.test(
            endpoint = TenantApi.Patch(someclientOrgId, tenantUpdateRep),
            expectedException = Auth0ClientIdAlreadyRegistered(limberappTenantRep.auth0ClientId)
        )
    }

    @Test
    fun happyPath() {
        val orgId = UUID.randomUUID()

        val originalTenantRep = TenantRepFixtures.limberappFixture.complete(this, orgId)
        var tenantRep = TenantRepFixtures.limberappFixture.complete(this, orgId)
        piperTest.setup(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(orgId)))

        val tenantUpdateRep = TenantRep.Update(auth0ClientId = "zyxwvutsrqponmlkjihgfedcbazyxwvu")
        tenantRep = tenantRep.copy(auth0ClientId = tenantUpdateRep.auth0ClientId!!)
        piperTest.test(TenantApi.Patch(originalTenantRep.orgId, tenantUpdateRep)) {
            val actual = json.parse<TenantRep.Complete>(response.content!!)
            assertEquals(tenantRep, actual)
        }

        piperTest.test(TenantApi.Get(orgId)) {
            val actual = json.parse<TenantRep.Complete>(response.content!!)
            assertEquals(tenantRep, actual)
        }
    }
}
