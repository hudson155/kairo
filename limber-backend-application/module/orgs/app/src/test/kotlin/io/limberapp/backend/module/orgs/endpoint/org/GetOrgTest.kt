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
        piperTest.test(
            endpointConfig = GetOrg.endpointConfig,
            pathParams = mapOf(GetOrg.orgId to orgId.toString()),
            expectedStatusCode = HttpStatusCode.NotFound
        ) {}
    }

    @Test
    fun exists() {

        val creationRep = OrgRep.Creation("Cranky Pasta")
        val id = deterministicUuidGenerator[0]
        piperTest.test(
            endpointConfig = CreateOrg.endpointConfig,
            body = creationRep
        ) {}

        piperTest.test(
            endpointConfig = GetOrg.endpointConfig,
            pathParams = mapOf(GetOrg.orgId to id.toString())
        ) {
            val actual = objectMapper.readValue<OrgRep.Complete>(response.content!!)
            val expected = OrgRep.Complete(
                id = id,
                created = LocalDateTime.now(fixedClock),
                name = creationRep.name,
                members = emptyList()
            )
            assertEquals(expected, actual)
        }
    }
}
