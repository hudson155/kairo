package io.limberapp.backend.module.orgs.endpoint.org

import io.limberapp.backend.module.orgs.exception.notFound.OrgNotFound
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import org.junit.Test
import java.util.UUID

internal class DeleteOrgTest : ResourceTest() {

    @Test
    fun doesNotExist() {

        // Setup
        val orgId = UUID.randomUUID()

        // DeleteOrg
        piperTest.test(
            endpointConfig = DeleteOrg.endpointConfig,
            pathParams = mapOf(DeleteOrg.orgId to orgId.toString()),
            expectedException = OrgNotFound()
        )
    }

    @Test
    fun happyPath() {

        // CreateOrg
        val orgCreationRep = OrgRep.Creation("Cranky Pasta")
        val orgId = deterministicUuidGenerator[0]
        piperTest.test(
            endpointConfig = CreateOrg.endpointConfig,
            body = orgCreationRep
        ) {}

        // DeleteOrg
        piperTest.test(
            endpointConfig = DeleteOrg.endpointConfig,
            pathParams = mapOf(DeleteOrg.orgId to orgId.toString())
        ) {}

        // GetOrg
        piperTest.test(
            endpointConfig = GetOrg.endpointConfig,
            pathParams = mapOf(GetOrg.orgId to orgId.toString()),
            expectedException = OrgNotFound()
        )
    }
}
