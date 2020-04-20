package io.limberapp.backend.module.auth.endpoint.tenant.domain

import io.limberapp.backend.module.auth.api.tenant.TenantApi
import io.limberapp.backend.module.auth.api.tenant.domain.TenantDomainApi
import io.limberapp.backend.module.auth.exception.tenant.TenantDomainAlreadyRegistered
import io.limberapp.backend.module.auth.exception.tenant.TenantNotFound
import io.limberapp.backend.module.auth.rep.tenant.TenantDomainRep
import io.limberapp.backend.module.auth.rep.tenant.TenantRep
import io.limberapp.backend.module.auth.testing.ResourceTest
import io.limberapp.backend.module.auth.testing.fixtures.tenant.TenantDomainRepFixtures
import io.limberapp.backend.module.auth.testing.fixtures.tenant.TenantRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class PostTenantDomainTest : ResourceTest() {

    @Test
    fun orgDoesNotExist() {

        val orgId = UUID.randomUUID()

        piperTest.test(
            endpoint = TenantDomainApi.Post(orgId, TenantDomainRepFixtures.limberappFixture.creation()),
            expectedException = TenantNotFound()
        )
    }

    @Test
    fun duplicateDomain() {

        val limberappOrgId = UUID.randomUUID()
        val someclientOrgId = UUID.randomUUID()

        val limberappTenantRep = TenantRepFixtures.limberappFixture.complete(this, limberappOrgId)
        piperTest.setup(TenantApi.Post(TenantRepFixtures.limberappFixture.creation(limberappOrgId)))

        piperTest.setup(TenantApi.Post(TenantRepFixtures.someclientFixture.creation(someclientOrgId)))

        val tenantDomainRep = TenantDomainRepFixtures.someclientFixture.complete(this)
        piperTest.test(
            endpoint = TenantDomainApi.Post(limberappOrgId, TenantDomainRepFixtures.someclientFixture.creation()),
            expectedException = TenantDomainAlreadyRegistered(tenantDomainRep.domain)
        )

        piperTest.test(TenantApi.Get(limberappOrgId)) {
            val actual = json.parse<TenantRep.Complete>(response.content!!)
            assertEquals(limberappTenantRep, actual)
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

        piperTest.test(TenantApi.Get(orgId)) {
            val actual = json.parse<TenantRep.Complete>(response.content!!)
            assertEquals(tenantRep, actual)
        }
    }
}
