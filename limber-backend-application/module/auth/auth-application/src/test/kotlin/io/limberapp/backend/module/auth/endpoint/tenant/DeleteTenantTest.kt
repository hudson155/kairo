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
        val orgId = UUID.randomUUID()

        piperTest.test(
            endpoint = TenantApi.Delete(orgId),
            expectedException = TenantNotFound()
        )
    }

    @Test
    fun happyPath() {
        val orgId = UUID.randomUUID()

        val tenantRep = TenantRepFixtures.limberappFixture.complete(this, orgId)
        piperTest.setup(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(orgId)))

        piperTest.test(TenantApi.Delete(tenantRep.orgId)) {}

        piperTest.test(
            endpoint = TenantApi.Get(orgId),
            expectedException = TenantNotFound()
        )
    }
}
