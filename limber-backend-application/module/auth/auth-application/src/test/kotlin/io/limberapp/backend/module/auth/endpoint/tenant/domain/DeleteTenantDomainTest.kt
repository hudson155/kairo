package io.limberapp.backend.module.auth.endpoint.tenant.domain

import io.limberapp.backend.module.auth.api.tenant.TenantApi
import io.limberapp.backend.module.auth.api.tenant.domain.TenantDomainApi
import io.limberapp.backend.module.auth.exception.tenant.TenantDomainNotFound
import io.limberapp.backend.module.auth.rep.tenant.TenantDomainRep
import io.limberapp.backend.module.auth.rep.tenant.TenantRep
import io.limberapp.backend.module.auth.testing.ResourceTest
import io.limberapp.backend.module.auth.testing.fixtures.tenant.TenantDomainRepFixtures
import io.limberapp.backend.module.auth.testing.fixtures.tenant.TenantRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class DeleteTenantDomainTest : ResourceTest() {
    @Test
    fun orgDoesNotExist() {
        val orgId = UUID.randomUUID()
        val tenantDomain = "fakedomain.com"

        piperTest.test(
            endpoint = TenantDomainApi.Delete(orgId, tenantDomain),
            expectedException = TenantDomainNotFound()
        )
    }

    @Test
    fun domainDoesNotExist() {
        val orgId = UUID.randomUUID()
        val tenantDomain = "fakedomain.com"

        val tenantRep = TenantRepFixtures.limberappFixture.complete(this, orgId)
        piperTest.setup(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(orgId)))

        piperTest.test(
            endpoint = TenantDomainApi.Delete(orgId, tenantDomain),
            expectedException = TenantDomainNotFound()
        )

        piperTest.test(TenantApi.Get(orgId)) {
            val actual = json.parse<TenantRep.Complete>(response.content!!)
            assertEquals(tenantRep, actual)
        }
    }

    @Test
    fun happyPath() {
        val orgId = UUID.randomUUID()

        var tenantRep = TenantRepFixtures.limberappFixture.complete(this, orgId)
        piperTest.setup(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(orgId)))

        val tenantDomainRep = TenantDomainRepFixtures.someclientFixture.complete(this)
        tenantRep = tenantRep.copy(domains = tenantRep.domains.plus(tenantDomainRep))
        piperTest.test(TenantDomainApi.Post(orgId, TenantDomainRepFixtures.someclientFixture.creation())) {
            val actual = json.parse<TenantDomainRep.Complete>(response.content!!)
            assertEquals(tenantDomainRep, actual)
        }

        tenantRep = tenantRep.copy(domains = tenantRep.domains.minus(tenantDomainRep))
        piperTest.test(TenantDomainApi.Delete(orgId, tenantDomainRep.domain)) {}

        piperTest.test(TenantApi.Get(orgId)) {
            val actual = json.parse<TenantRep.Complete>(response.content!!)
            assertEquals(tenantRep, actual)
        }
    }
}
