package io.limberapp.backend.module.orgs.endpoint.org.role

import io.limberapp.backend.module.orgs.api.org.OrgApi
import io.limberapp.backend.module.orgs.api.org.role.OrgRoleApi
import io.limberapp.backend.module.orgs.exception.org.OrgNotFound
import io.limberapp.backend.module.orgs.rep.org.OrgRoleRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import io.limberapp.backend.module.orgs.testing.fixtures.org.OrgRepFixtures
import io.limberapp.backend.module.orgs.testing.fixtures.orgRole.OrgRoleRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class GetOrgRolesByOrgGuidTest : ResourceTest() {
    @Test
    fun orgDoesNotExist() {
        val orgGuid = UUID.randomUUID()

        piperTest.test(
            endpoint = OrgRoleApi.GetByOrgGuid(orgGuid),
            expectedException = OrgNotFound()
        )
    }

    @Test
    fun happyPathNoneExist() {
        val orgOwnerAccountGuid = UUID.randomUUID()

        val orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, orgOwnerAccountGuid, 0)
        piperTest.setup(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation(orgOwnerAccountGuid)))

        piperTest.test(OrgRoleApi.GetByOrgGuid(orgRep.guid)) {
            val actual = json.parseSet<OrgRoleRep.Complete>(response.content!!)
            assertTrue(actual.isEmpty())
        }
    }

    @Test
    fun happyPathMultipleExist() {
        val orgOwnerAccountGuid = UUID.randomUUID()

        val orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, orgOwnerAccountGuid, 0)
        piperTest.setup(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation(orgOwnerAccountGuid)))

        val adminOrgRoleRep = OrgRoleRepFixtures.adminFixture.complete(this, 2)
        piperTest.setup(OrgRoleApi.Post(orgRep.guid, OrgRoleRepFixtures.adminFixture.creation()))

        val memberOrgRoleRep = OrgRoleRepFixtures.memberFixture.complete(this, 3)
        piperTest.setup(OrgRoleApi.Post(orgRep.guid, OrgRoleRepFixtures.memberFixture.creation()))

        piperTest.test(OrgRoleApi.GetByOrgGuid(orgRep.guid)) {
            val actual = json.parseSet<OrgRoleRep.Complete>(response.content!!)
            assertEquals(setOf(adminOrgRoleRep, memberOrgRoleRep), actual)
        }
    }
}
