package io.limberapp.backend.module.orgs.endpoint.org

import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import io.limberapp.backend.module.orgs.testing.fixtures.org.OrgRepFixtures
import kotlinx.serialization.parse
import kotlinx.serialization.stringify
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class PostOrgTest : ResourceTest() {

    @Test
    fun happyPath() {

        // PostOrg
        val orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
        piperTest.test(
            endpointConfig = PostOrg.endpointConfig,
            body = json.stringify(OrgRepFixtures.crankyPastaFixture.creation())
        ) {
            val actual = json.parse<OrgRep.Complete>(response.content!!)
            assertEquals(orgRep, actual)
        }

        // GetOrg
        piperTest.test(
            endpointConfig = GetOrg.endpointConfig,
            pathParams = mapOf(GetOrg.orgId to orgRep.id)
        ) {
            val actual = json.parse<OrgRep.Complete>(response.content!!)
            assertEquals(orgRep, actual)
        }
    }
}
