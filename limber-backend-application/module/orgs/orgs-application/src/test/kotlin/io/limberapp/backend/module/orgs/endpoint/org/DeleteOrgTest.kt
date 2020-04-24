package io.limberapp.backend.module.orgs.endpoint.org

import io.limberapp.backend.module.orgs.api.org.OrgApi
import io.limberapp.backend.module.orgs.exception.org.OrgNotFound
import io.limberapp.backend.module.orgs.testing.ResourceTest
import io.limberapp.backend.module.orgs.testing.fixtures.org.OrgRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID

internal class DeleteOrgTest : ResourceTest() {
    @Test
    fun doesNotExist() {
        val orgId = UUID.randomUUID()

        piperTest.test(
            endpoint = OrgApi.Delete(orgId),
            expectedException = OrgNotFound()
        )
    }

    @Test
    fun happyPath() {
        val orgOwnerAccountId = UUID.randomUUID()

        val orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, orgOwnerAccountId, 0)
        piperTest.setup(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation(orgOwnerAccountId)))

        piperTest.test(OrgApi.Delete(orgRep.id)) {}

        piperTest.test(
            endpoint = OrgApi.Get(orgRep.id),
            expectedException = OrgNotFound()
        )
    }
}
