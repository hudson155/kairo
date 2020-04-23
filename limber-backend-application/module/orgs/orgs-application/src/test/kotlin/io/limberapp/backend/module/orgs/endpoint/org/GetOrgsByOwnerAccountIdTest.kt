package io.limberapp.backend.module.orgs.endpoint.org

import io.limberapp.backend.module.orgs.api.org.OrgApi
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import io.limberapp.backend.module.orgs.testing.fixtures.org.OrgRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class GetOrgsByOwnerAccountIdTest : ResourceTest() {

    @Test
    fun happyPathNoOrgs() {

        val orgOwnerAccountId = UUID.randomUUID()

        piperTest.test(OrgApi.GetByOwnerAccountId(orgOwnerAccountId)) {
            val actual = json.parseList<OrgRep.Complete>(response.content!!)
            assertTrue(actual.isEmpty())
        }
    }

    @Test
    fun happyPathMultipleOrgs() {

        val orgOwnerAccountId = UUID.randomUUID()

        val crankyPastaOrgRep = OrgRepFixtures.crankyPastaFixture.complete(this, orgOwnerAccountId, 0)
        piperTest.setup(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation(orgOwnerAccountId)))

        val dynamicTennisOrgRep = OrgRepFixtures.dynamicTennisFixture.complete(this, orgOwnerAccountId, 2)
        piperTest.setup(OrgApi.Post(OrgRepFixtures.dynamicTennisFixture.creation(orgOwnerAccountId)))

        piperTest.test(OrgApi.GetByOwnerAccountId(orgOwnerAccountId)) {
            val actual = json.parseList<OrgRep.Complete>(response.content!!).toSet()
            assertEquals(setOf(crankyPastaOrgRep, dynamicTennisOrgRep), actual)
        }
    }
}
