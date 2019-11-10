package io.limberapp.backend.module.orgs.endpoint.org

import io.ktor.http.HttpStatusCode
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import org.junit.Test
import java.util.UUID

internal class DeleteOrgTest : ResourceTest() {

    @Test
    fun doesNotExist() {
        val orgId = UUID.randomUUID()
        limberTest.test(
            endpointConfig = DeleteOrg.endpointConfig,
            pathParams = mapOf(DeleteOrg.orgId to orgId.toString()),
            expectedStatusCode = HttpStatusCode.NotFound
        ) {}
    }

    @Test
    fun exists() {

        val creationRep = OrgRep.Creation("Cranky Pasta")
        val id = uuidGenerator[0]
        limberTest.test(
            endpointConfig = CreateOrg.endpointConfig,
            body = creationRep
        ) {}

        limberTest.test(
            endpointConfig = DeleteOrg.endpointConfig,
            pathParams = mapOf(DeleteOrg.orgId to id.toString())
        ) {}

        limberTest.test(
            endpointConfig = GetOrg.endpointConfig,
            pathParams = mapOf(GetOrg.orgId to id.toString()),
            expectedStatusCode = HttpStatusCode.NotFound
        ) {}
    }
}
