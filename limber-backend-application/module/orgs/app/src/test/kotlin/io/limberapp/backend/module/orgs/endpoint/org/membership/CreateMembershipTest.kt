package io.limberapp.backend.module.orgs.endpoint.org.membership

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.module.orgs.endpoint.org.CreateOrg
import io.limberapp.backend.module.orgs.endpoint.org.GetOrg
import io.limberapp.backend.module.orgs.exception.conflict.UserIsAlreadyAMemberOfOrg
import io.limberapp.backend.module.orgs.exception.notFound.OrgNotFound
import io.limberapp.backend.module.orgs.rep.membership.MembershipRep
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import io.limberapp.backend.module.orgs.testing.fixtures.membership.MembershipRepFixtures
import io.limberapp.backend.module.orgs.testing.fixtures.org.OrgRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class CreateMembershipTest : ResourceTest() {

    @Test
    fun orgDoesNotExist() {

        // Setup
        val orgId = UUID.randomUUID()
        val userId = UUID.randomUUID()

        // CreateMembership
        piperTest.test(
            endpointConfig = CreateMembership.endpointConfig,
            pathParams = mapOf(CreateMembership.orgId to orgId),
            body = MembershipRepFixtures[0].creation(userId),
            expectedException = OrgNotFound()
        )
    }

    @Test
    fun duplicate() {

        // Setup
        val userId = UUID.randomUUID()

        // CreateOrg
        var orgRep = OrgRepFixtures[0].complete(this, 0)
        piperTest.setup(
            endpointConfig = CreateOrg.endpointConfig,
            body = OrgRepFixtures[0].creation()
        )

        // CreateMembership
        val membershipRep = MembershipRepFixtures[0].complete(this, userId)
        orgRep = orgRep.copy(members = orgRep.members.plus(membershipRep))
        piperTest.setup(
            endpointConfig = CreateMembership.endpointConfig,
            pathParams = mapOf(CreateMembership.orgId to orgRep.id),
            body = MembershipRepFixtures[0].creation(userId)
        )

        // CreateMembership
        piperTest.test(
            endpointConfig = CreateMembership.endpointConfig,
            pathParams = mapOf(CreateMembership.orgId to orgRep.id),
            body = MembershipRepFixtures[1].creation(userId),
            expectedException = UserIsAlreadyAMemberOfOrg()
        )

        // GetOrg
        piperTest.test(
            endpointConfig = GetOrg.endpointConfig,
            pathParams = mapOf(GetOrg.orgId to orgRep.id)
        ) {
            val actual = objectMapper.readValue<OrgRep.Complete>(response.content!!)
            assertEquals(orgRep, actual)
        }
    }

    @Test
    fun happyPath() {

        // Setup
        val userId = UUID.randomUUID()

        // CreateOrg
        var orgRep = OrgRepFixtures[0].complete(this, 0)
        piperTest.setup(
            endpointConfig = CreateOrg.endpointConfig,
            body = OrgRepFixtures[0].creation()
        )

        // CreateMembership
        val membershipRep = MembershipRepFixtures[0].complete(this, userId)
        orgRep = orgRep.copy(members = orgRep.members.plus(membershipRep))
        piperTest.test(
            endpointConfig = CreateMembership.endpointConfig,
            pathParams = mapOf(CreateMembership.orgId to orgRep.id),
            body = MembershipRepFixtures[0].creation(userId)
        ) {
            val actual = objectMapper.readValue<MembershipRep.Complete>(response.content!!)
            assertEquals(membershipRep, actual)
        }

        // GetOrg
        piperTest.test(
            endpointConfig = GetOrg.endpointConfig,
            pathParams = mapOf(GetOrg.orgId to orgRep.id)
        ) {
            val actual = objectMapper.readValue<OrgRep.Complete>(response.content!!)
            assertEquals(orgRep, actual)
        }
    }
}
