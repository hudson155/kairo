package io.limberapp.backend.module.auth.endpoint.tenant

import io.limberapp.backend.module.auth.exception.tenant.TenantNotFound
import io.limberapp.backend.module.auth.rep.tenant.TenantRep
import io.limberapp.backend.module.auth.testing.ResourceTest
import io.limberapp.backend.module.auth.testing.fixtures.tenant.TenantRepFixtures
import kotlinx.serialization.parse
import kotlinx.serialization.stringify
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class GetTenantTest : ResourceTest() {

    @Test
    fun doesNotExist() {

        // Setup
        val tenantDomain = "fakedomain.com"

        // GetTenant
        piperTest.test(
            endpointConfig = GetTenant.endpointConfig,
            pathParams = mapOf(GetTenant.tenantDomain to tenantDomain),
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
