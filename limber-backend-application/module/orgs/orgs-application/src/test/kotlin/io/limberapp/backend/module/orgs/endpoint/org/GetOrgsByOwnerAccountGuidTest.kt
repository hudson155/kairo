package io.limberapp.backend.module.orgs.endpoint.org

import io.limberapp.backend.module.orgs.api.org.OrgApi
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import io.limberapp.backend.module.orgs.testing.fixtures.org.OrgRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class GetOrgsByOwnerAccountGuidTest : ResourceTest() {
    @Test
    fun happyPathNoOrgs() {
        val orgOwnerAccountGuid = UUID.randomUUID()

        piperTest.test(OrgApi.GetByOwnerAccountGuid(orgOwnerAccountGuid)) {
            val actual = json.parseSet<OrgRep.Complete>(response.content!!)
            assertTrue(actual.isEmpty())
        }
    }

    @Test
    fun happyPathMultipleOrgs() {
        val orgOwnerAccountGuid = UUID.randomUUID()

        val crankyPastaOrgRep = OrgRepFixtures.crankyPastaFixture.complete(this, orgOwnerAccountGuid, 0)
        piperTest.setup(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation(orgOwnerAccountGuid)))

        val dynamicTennisOrgRep = OrgRepFixtures.dynamicTennisFixture.complete(this, orgOwnerAccountGuid, 2)
        piperTest.setup(OrgApi.Post(OrgRepFixtures.dynamicTennisFixture.creation(orgOwnerAccountGuid)))

        piperTest.test(OrgApi.GetByOwnerAccountGuid(orgOwnerAccountGuid)) {
            val actual = json.parseSet<OrgRep.Complete>(response.content!!)
            assertEquals(setOf(crankyPastaOrgRep, dynamicTennisOrgRep), actual)
        }
    }
}
