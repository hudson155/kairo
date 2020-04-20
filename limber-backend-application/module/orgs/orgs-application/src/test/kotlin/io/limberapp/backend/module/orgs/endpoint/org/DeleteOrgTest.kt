package io.limberapp.backend.module.orgs.endpoint.org

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
            endpointConfig = DeleteOrg.endpointConfig,
            pathParams = mapOf(DeleteOrg.orgId to orgId),
            expectedException = OrgNotFound()
        )
    }

    @Test
    fun happyPath() {

        val orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
        piperTest.setup(
            endpointConfig = PostOrg.endpointConfig,
            body = OrgRepFixtures.crankyPastaFixture.creation()
        )

        piperTest.test(
            endpointConfig = DeleteOrg.endpointConfig,
            pathParams = mapOf(DeleteOrg.orgId to orgRep.id)
        ) {}

        piperTest.test(
            endpointConfig = GetOrg.endpointConfig,
            pathParams = mapOf(GetOrg.orgId to orgRep.id),
            expectedException = OrgNotFound()
        )
    }
}
