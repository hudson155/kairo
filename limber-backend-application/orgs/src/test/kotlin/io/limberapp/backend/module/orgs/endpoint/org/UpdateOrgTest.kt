package io.limberapp.backend.module.orgs.endpoint.org

import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.http.HttpStatusCode
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import org.junit.Test
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.assertEquals

internal class UpdateOrgTest : ResourceTest() {

    @Test
    fun doesNotExist() {
        val orgId = UUID.randomUUID()
        val updateRep = OrgRep.Update("Standing Teeth")
        limberTest.test(
            config = UpdateOrg.config,
            pathParams = mapOf(UpdateOrg.orgId to orgId.toString()),
            body = updateRep,
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

        val updateRep = OrgRep.Update("Standing Teeth")
        limberTest.test(
            config = UpdateOrg.config,
            pathParams = mapOf(UpdateOrg.orgId to id.toString()),
            body = updateRep
        ) {
            val actual = objectMapper.readValue<OrgRep.Complete>(response.content!!)
            val expected = OrgRep.Complete(
                id = id,
                created = LocalDateTime.now(clock),
                name = updateRep.name!!,
                members = emptyList()
            )
            assertEquals(expected, actual)
        }
    }
}
