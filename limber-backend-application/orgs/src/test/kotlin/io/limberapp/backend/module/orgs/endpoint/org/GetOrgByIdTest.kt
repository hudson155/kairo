package io.limberapp.backend.module.orgs.endpoint.org

import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.http.HttpStatusCode
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import org.junit.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class GetOrgByIdTest : ResourceTest() {

    @Test
    fun doesNotExist() {
        val orgId = UUID.randomUUID()
        limberTest.get(
            config = GetOrgById.config,
            pathParams = mapOf("orgId" to orgId.toString()),
            expectedStatusCode = HttpStatusCode.NotFound
        ) {}
    }

    @Test
    fun exists() {

        val creationRep = OrgRep.Creation("Cranky Pasta")
        lateinit var orgId: UUID
        limberTest.post(
            config = CreateOrg.config,
            body = creationRep
        ) {
            orgId = objectMapper.readValue<OrgRep.Complete>(response.content!!).id
        }

        limberTest.get(
            config = GetOrgById.config,
            pathParams = mapOf("orgId" to orgId.toString())
        ) {
            val actual = objectMapper.readValue<OrgRep.Complete>(response.content!!)
            val expected = OrgRep.Complete(actual.id, actual.created, 0, "Cranky Pasta")
            assertEquals(expected, actual)
        }
    }
}
