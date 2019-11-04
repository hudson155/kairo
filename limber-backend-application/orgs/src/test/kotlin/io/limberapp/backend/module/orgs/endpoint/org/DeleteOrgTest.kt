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
            config = DeleteOrg.config,
            pathParams = mapOf("orgId" to orgId.toString()),
            expectedStatusCode = HttpStatusCode.NotFound
        ) {}
    }

    @Test
    fun exists() {

        val creationRep = OrgRep.Creation("Cranky Pasta")
        val id = uuidGenerator[0]
        limberTest.test(
            config = CreateOrg.config,
            body = creationRep
        ) {}

        limberTest.test(
            config = DeleteOrg.config,
            pathParams = mapOf("orgId" to id.toString())
        ) {}

        limberTest.test(
            config = GetOrg.config,
            pathParams = mapOf("orgId" to id.toString()),
            expectedStatusCode = HttpStatusCode.NotFound
        ) {}
    }
}
