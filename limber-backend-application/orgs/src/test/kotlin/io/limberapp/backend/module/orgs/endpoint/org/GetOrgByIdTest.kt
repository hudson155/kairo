package io.limberapp.backend.module.orgs.endpoint.org

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import org.junit.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class GetOrgByIdTest : ResourceTest() {

    @Test
    fun exists() {

        val creationRep = OrgRep.Creation("Cranky Pasta")
        lateinit var orgId: UUID
        limberTest.post(CreateOrg.config, body = creationRep) {
            orgId = objectMapper.readValue<OrgRep.Complete>(response.content!!).id
        }

        limberTest.get(GetOrgById.config, mapOf("orgId" to orgId.toString())) {
            val actual = objectMapper.readValue<OrgRep.Complete>(response.content!!)
            val expected = OrgRep.Complete(actual.id, actual.created, 0, "Cranky Pasta")
            assertEquals(expected, actual)
        }
    }
}
