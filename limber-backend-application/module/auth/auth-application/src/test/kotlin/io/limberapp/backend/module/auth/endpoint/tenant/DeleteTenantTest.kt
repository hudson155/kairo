package io.limberapp.backend.module.auth.endpoint.tenant

import io.limberapp.backend.module.auth.api.tenant.TenantApi
import io.limberapp.backend.module.auth.exception.tenant.TenantNotFound
import io.limberapp.backend.module.auth.testing.ResourceTest
import io.limberapp.backend.module.auth.testing.fixtures.tenant.TenantRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID

internal class DeleteTenantTest : ResourceTest() {

    @Test
    fun doesNotExist() {

        // Setup
        val orgId = UUID.randomUUID()

        // DeleteTenant
        piperTest.test(
            endpoint = TenantApi.Delete(orgId),
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

        // DeleteTenant
        piperTest.test(TenantApi.Delete(tenantRep.orgId)) {}

        // GetTenant
        piperTest.test(
            endpoint = TenantApi.Get(orgId),
            expectedException = TenantNotFound()
        )
    }
}
