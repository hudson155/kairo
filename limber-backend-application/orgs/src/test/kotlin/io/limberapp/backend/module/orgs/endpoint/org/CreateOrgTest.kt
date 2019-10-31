package io.limberapp.backend.module.orgs.endpoint.org

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import org.junit.Test
import kotlin.test.assertEquals

internal class CreateOrgTest : ResourceTest() {

    @Test
    fun createOrg() {
        val creationRep = OrgRep.Creation("Cranky Pasta")
        limberTest.post(CreateOrg.config, body = creationRep) {
            val actual = objectMapper.readValue<OrgRep.Complete>(response.content!!)
            val expected = OrgRep.Complete(actual.id, actual.created, 0, "Cranky Pasta")
            assertEquals(expected, actual)
        }
    }
}
