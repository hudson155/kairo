package io.limberapp.backend.module.orgs.endpoint.org

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.module.orgs.exception.org.OrgNotFound
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import io.limberapp.backend.module.orgs.testing.fixtures.org.OrgRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class PatchOrgTest : ResourceTest() {

    @Test
    fun doesNotExist() {

        // Setup
        val orgId = UUID.randomUUID()

        // PatchOrg
        val orgUpdateRep = OrgRep.Update("Standing Teeth")
        piperTest.test(
            endpointConfig = PatchOrg.endpointConfig,
            pathParams = mapOf(PatchOrg.orgId to orgId),
            body = orgUpdateRep,
            expectedException = OrgNotFound()
        )
    }

    @Test
    fun happyPath() {

        // PostOrg
        var orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
        piperTest.setup(
            endpointConfig = PostOrg.endpointConfig,
            body = OrgRepFixtures.crankyPastaFixture.creation()
        )

        // PatchOrg
        val orgUpdateRep = OrgRep.Update("Standing Teeth")
        orgRep = orgRep.copy(name = orgUpdateRep.name!!)
        piperTest.test(
            endpointConfig = PatchOrg.endpointConfig,
            pathParams = mapOf(PatchOrg.orgId to orgRep.id),
            body = orgUpdateRep
        ) {
            val actual = objectMapper.readValue<OrgRep.Complete>(response.content!!)
            assertEquals(orgRep, actual)
        }

        // GetOrg
        piperTest.test(
            endpointConfig = GetOrg.endpointConfig,
            pathParams = mapOf(PatchOrg.orgId to orgRep.id)
        ) {
            val actual = objectMapper.readValue<OrgRep.Complete>(response.content!!)
            assertEquals(orgRep, actual)
        }
    }
}
