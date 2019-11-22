package io.limberapp.backend.module.orgs.endpoint.org

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import org.junit.Test
import java.time.LocalDateTime
import kotlin.test.assertEquals

internal class CreateOrgTest : ResourceTest() {

    @Test
    fun create() {
        val creationRep = OrgRep.Creation("Cranky Pasta")
        val id = deterministicUuidGenerator[0]
        limberTest.test(
            endpointConfig = CreateOrg.endpointConfig,
            body = creationRep
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
