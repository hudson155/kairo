package io.limberapp.backend.module.auth.endpoint.tenant

import io.limberapp.backend.module.auth.api.tenant.TenantApi
import io.limberapp.backend.module.auth.exception.tenant.TenantNotFound
import io.limberapp.backend.module.auth.rep.tenant.TenantRep
import io.limberapp.backend.module.auth.testing.ResourceTest
import io.limberapp.backend.module.auth.testing.fixtures.tenant.TenantRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class GetTenantByDomainTest : ResourceTest() {

    @Test
    fun doesNotExist() {

        // Setup
        val tenantDomain = "fakedomain.com"

        // GetTenantByDomain
        piperTest.test(
            endpoint = TenantApi.GetByDomain(tenantDomain),
            expectedException = TenantNotFound()
        )
    }

    @Test
    fun happyPath() {

        // Setup
        val orgId = UUID.randomUUID()

        // PostTenant
        val tenantRep = TenantRepFixtures.limberappFixture.complete(this, orgId)
        piperTest.setup(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(orgId)))

        // GetTenantByDomain
        piperTest.test(TenantApi.GetByDomain(tenantRep.domains.single().domain)) {
            val actual = json.parse<TenantRep.Complete>(response.content!!)
            assertEquals(tenantRep, actual)
        }
    }
}
