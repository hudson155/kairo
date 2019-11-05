package io.limberapp.backend.module.orgs.endpoint.org

import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.http.HttpStatusCode
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import org.junit.Test
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.assertEquals

internal class GetOrgTest : ResourceTest() {

    @Test
    fun doesNotExist() {
        val orgId = UUID.randomUUID()
        limberTest.test(
            config = GetOrg.config,
            pathParams = mapOf(GetOrg.orgId to orgId.toString()),
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
            config = GetOrg.config,
            pathParams = mapOf(GetOrg.orgId to id.toString())
        ) {
            val actual = objectMapper.readValue<OrgRep.Complete>(response.content!!)
            val expected = OrgRep.Complete(
                id = id,
                created = LocalDateTime.now(clock),
                name = creationRep.name,
                members = emptyList()
            )
            assertEquals(expected, actual)
        }
    }
}
