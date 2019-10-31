package io.limberapp.backend.module.orgs.endpoint.org

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import io.limberapp.framework.jackson.objectMapper.LimberObjectMapper
import org.junit.Test
import kotlin.test.assertEquals

private val objectMapper = LimberObjectMapper(prettyPrint = false)

class CreateOrgTest : ResourceTest() {

    @Test
    fun ok() = limberTest.post(
        config = CreateOrg.config,
        body = OrgRep.Creation("Cranky Pasta")
    ) {
        val actual = objectMapper.readValue<OrgRep.Complete>(response.content!!)
        val expected = OrgRep.Complete(actual.id, actual.created, 0, "Cranky Pasta")
        assertEquals(expected, actual)
    }
}
