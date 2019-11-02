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
        limberTest.test(
            config = GetOrgById.config,
            pathParams = mapOf("orgId" to orgId.toString()),
            expectedStatusCode = HttpStatusCode.NotFound
        ) {}
    }

    @Test
    fun exists() {

        lateinit var orgId: UUID
        val creationRep = OrgRep.Creation("Cranky Pasta")
        limberTest.test(
            config = CreateOrg.config,
            body = creationRep
        ) {
            orgId = objectMapper.readValue<OrgRep.Complete>(response.content!!).id
        }

        limberTest.test(
            config = GetOrgById.config,
            pathParams = mapOf("orgId" to orgId.toString())
        ) {
            val actual = objectMapper.readValue<OrgRep.Complete>(response.content!!)
            val expected = OrgRep.Complete(actual.id, actual.created, "Cranky Pasta")
            assertEquals(expected, actual)
        }
    }
}
