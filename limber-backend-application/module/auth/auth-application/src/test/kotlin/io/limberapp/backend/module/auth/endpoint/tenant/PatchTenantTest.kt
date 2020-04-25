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
        val orgGuid = UUID.randomUUID()

        val tenantUpdateRep = TenantRep.Update(auth0ClientId = "zyxwvutsrqponmlkjihgfedcbazyxwvu")
        piperTest.test(
            endpoint = TenantApi.Patch(orgGuid, tenantUpdateRep),
            expectedException = TenantNotFound()
        )
    }

    @Test
    fun duplicateAuth0ClientId() {
        val limberappOrgGuid = UUID.randomUUID()
        val someclientOrgGuid = UUID.randomUUID()

        val limberappTenantRep = TenantRepFixtures.limberappFixture.complete(this, limberappOrgGuid)
        piperTest.setup(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(limberappOrgGuid)))

        piperTest.setup(TenantApi.Post(TenantRepFixtures.someclientFixture.creation(someclientOrgGuid)))

        val tenantUpdateRep = TenantRep.Update(auth0ClientId = limberappTenantRep.auth0ClientId)
        piperTest.test(
            endpoint = TenantApi.Patch(someclientOrgGuid, tenantUpdateRep),
            expectedException = Auth0ClientIdAlreadyRegistered(limberappTenantRep.auth0ClientId)
        )
    }

    @Test
    fun happyPath() {
        val orgGuid = UUID.randomUUID()

        val originalTenantRep = TenantRepFixtures.limberappFixture.complete(this, orgGuid)
        var tenantRep = TenantRepFixtures.limberappFixture.complete(this, orgGuid)
        piperTest.setup(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(orgGuid)))

        val tenantUpdateRep = TenantRep.Update(auth0ClientId = "zyxwvutsrqponmlkjihgfedcbazyxwvu")
        tenantRep = tenantRep.copy(auth0ClientId = tenantUpdateRep.auth0ClientId!!)
        piperTest.test(TenantApi.Patch(originalTenantRep.orgGuid, tenantUpdateRep)) {
            val actual = json.parse<TenantRep.Complete>(response.content!!)
            assertEquals(tenantRep, actual)
        }

        piperTest.test(TenantApi.Get(orgGuid)) {
            val actual = json.parse<TenantRep.Complete>(response.content!!)
            assertEquals(tenantRep, actual)
        }
    }
}
