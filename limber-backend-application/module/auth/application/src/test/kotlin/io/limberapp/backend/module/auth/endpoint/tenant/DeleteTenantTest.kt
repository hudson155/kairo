package io.limberapp.backend.module.auth.endpoint.tenant

import com.piperframework.serialization.stringify
import io.limberapp.backend.module.auth.exception.tenant.TenantNotFound
import io.limberapp.backend.module.auth.testing.ResourceTest
import io.limberapp.backend.module.auth.testing.fixtures.tenant.TenantRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID

internal class DeleteTenantTest : ResourceTest() {

    @Test
    fun doesNotExist() {

        // Setup
        val tenantDomain = "fakedomain.com"

        // DeleteTenant
        piperTest.test(
            endpointConfig = DeleteTenant.endpointConfig,
            pathParams = mapOf(DeleteTenant.tenantDomain to tenantDomain),
            expectedException = TenantNotFound()
        )
    }

    @Test
    fun happyPath() {

        // Setup
        val orgId = UUID.randomUUID()

        // PostTenant
        val tenantRep = TenantRepFixtures.limberappFixture.complete(this, orgId)
        piperTest.setup(
            endpointConfig = PostTenant.endpointConfig,
            body = json.stringify(TenantRepFixtures.limberappFixture.creation(orgId))
        )

        // DeleteTenant
        piperTest.test(
            endpointConfig = DeleteTenant.endpointConfig,
            pathParams = mapOf(DeleteTenant.tenantDomain to tenantRep.domain)
        ) {}

        // GetTenant
        piperTest.test(
            endpointConfig = GetTenant.endpointConfig,
            pathParams = mapOf(GetTenant.tenantDomain to orgId),
            expectedException = TenantNotFound()
        )
    }
}
