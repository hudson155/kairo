package io.limberapp.backend.module.orgs.endpoint.org.role

import io.limberapp.backend.authorization.permissions.OrgPermissions
import io.limberapp.backend.module.orgs.api.org.OrgApi
import io.limberapp.backend.module.orgs.api.org.role.OrgRoleApi
import io.limberapp.backend.module.orgs.exception.org.OrgRoleIsNotUnique
import io.limberapp.backend.module.orgs.exception.org.OrgRoleNotFound
import io.limberapp.backend.module.orgs.rep.org.OrgRoleRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import io.limberapp.backend.module.orgs.testing.fixtures.org.OrgRepFixtures
import io.limberapp.backend.module.orgs.testing.fixtures.orgRole.OrgRoleRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class PatchOrgRoleTest : ResourceTest() {
    @Test
    fun orgDoesNotExist() {
        val orgGuid = UUID.randomUUID()
        val orgRoleGuid = UUID.randomUUID()

        val orgRoleUpdateRep = OrgRoleRep.Update(permissions = OrgPermissions.fromBitString("110"))
        piperTest.test(
            endpoint = OrgRoleApi.Patch(orgGuid, orgRoleGuid, orgRoleUpdateRep),
            expectedException = OrgRoleNotFound()
        )
    }

    @Test
    fun orgRoleDoesNotExist() {
        val orgOwnerAccountGuid = UUID.randomUUID()
        val orgRoleGuid = UUID.randomUUID()

        val orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, orgOwnerAccountGuid, 0)
        piperTest.setup(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation(orgOwnerAccountGuid)))

        val orgRoleUpdateRep = OrgRoleRep.Update(permissions = OrgPermissions.fromBitString("110"))
        piperTest.test(
            endpoint = OrgRoleApi.Patch(orgRep.guid, orgRoleGuid, orgRoleUpdateRep),
            expectedException = OrgRoleNotFound()
        )
    }

    @Test
    fun duplicateName() {
        val orgOwnerAccountGuid = UUID.randomUUID()

        val orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, orgOwnerAccountGuid, 0)
        piperTest.setup(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation(orgOwnerAccountGuid)))

        val adminOrgRoleRep = OrgRoleRepFixtures.adminFixture.complete(this, 2)
        piperTest.setup(OrgRoleApi.Post(orgRep.guid, OrgRoleRepFixtures.adminFixture.creation()))

        val memberOrgRoleRep = OrgRoleRepFixtures.memberFixture.complete(this, 3)
        piperTest.setup(OrgRoleApi.Post(orgRep.guid, OrgRoleRepFixtures.memberFixture.creation()))

        val orgRoleUpdateRep = OrgRoleRep.Update(name = adminOrgRoleRep.name)
        piperTest.test(
            endpoint = OrgRoleApi.Patch(orgRep.guid, memberOrgRoleRep.guid, orgRoleUpdateRep),
            expectedException = OrgRoleIsNotUnique()
        )

        piperTest.test(OrgRoleApi.GetByOrgGuid(orgRep.guid)) {
            val actual = json.parseSet<OrgRoleRep.Complete>(response.content!!)
            assertEquals(setOf(adminOrgRoleRep, memberOrgRoleRep), actual)
        }
    }

    @Test
    fun happyPath() {
        val orgOwnerAccountGuid = UUID.randomUUID()

        val orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, orgOwnerAccountGuid, 0)
        piperTest.setup(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation(orgOwnerAccountGuid)))

        var orgRoleRep = OrgRoleRepFixtures.adminFixture.complete(this, 2)
        piperTest.setup(OrgRoleApi.Post(orgRep.guid, OrgRoleRepFixtures.adminFixture.creation()))

        val orgRoleUpdateRep = OrgRoleRep.Update(permissions = OrgPermissions.fromBitString("110"))
        orgRoleRep = orgRoleRep.copy(permissions = orgRoleUpdateRep.permissions!!)
        piperTest.test(OrgRoleApi.Patch(orgRep.guid, orgRoleRep.guid, orgRoleUpdateRep)) {
            val actual = json.parse<OrgRoleRep.Complete>(response.content!!)
            assertEquals(orgRoleRep, actual)
        }

        piperTest.test(OrgRoleApi.GetByOrgGuid(orgRep.guid)) {
            val actual = json.parseSet<OrgRoleRep.Complete>(response.content!!)
            assertEquals(setOf(orgRoleRep), actual)
        }
    }
}
