package io.limberapp.backend.module.orgs.endpoint.org.membership

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.module.orgs.endpoint.org.CreateOrg
import io.limberapp.backend.module.orgs.endpoint.org.GetOrg
import io.limberapp.backend.module.orgs.exception.notFound.MembershipNotFound
import io.limberapp.backend.module.orgs.exception.notFound.OrgNotFound
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import io.limberapp.backend.module.orgs.testing.fixtures.membership.MembershipRepFixtures
import io.limberapp.backend.module.orgs.testing.fixtures.org.OrgRepFixtures
import org.junit.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class DeleteMembershipTest : ResourceTest() {

    @Test
    fun orgDoesNotExist() {

        // Setup
        val orgId = UUID.randomUUID()
        val userId = UUID.randomUUID()

        // DeleteMembership
        piperTest.test(
            endpointConfig = DeleteMembership.endpointConfig,
            pathParams = mapOf(
                DeleteMembership.orgId to orgId.toString(),
                DeleteMembership.memberId to userId.toString()
            ),
            expectedException = OrgNotFound()
        )
    }

    @Test
    fun membershipDoesNotExist() {

        // Setup
        val userId = UUID.randomUUID()

        // CreateOrg
        val orgRep = OrgRepFixtures[0].complete(this, 0)
        piperTest.test(
            endpointConfig = CreateOrg.endpointConfig,
            body = OrgRepFixtures[0].creation()
        ) {}

        // DeleteMembership
        piperTest.test(
            endpointConfig = DeleteMembership.endpointConfig,
            pathParams = mapOf(
                DeleteMembership.orgId to orgRep.id.toString(),
                DeleteMembership.memberId to userId.toString()
            ),
            expectedException = MembershipNotFound()
        )

        // GetOrg
        piperTest.test(
            endpointConfig = GetOrg.endpointConfig,
            pathParams = mapOf("orgId" to orgRep.id.toString())
        ) {
            val actual = objectMapper.readValue<OrgRep.Complete>(response.content!!)
            assertEquals(orgRep, actual)
        }
    }

    @Test
    fun happyPath() {

        // Setup
        val user0Id = UUID.randomUUID()
        val user1Id = UUID.randomUUID()

        // CreateOrg
        var orgRep = OrgRepFixtures[0].complete(this, 0)
        piperTest.test(
            endpointConfig = CreateOrg.endpointConfig,
            body = OrgRepFixtures[0].creation()
        ) {}

        // CreateMembership
        val membership0Rep = MembershipRepFixtures[0].complete(this, user0Id)
        orgRep = orgRep.copy(members = orgRep.members.plus(membership0Rep))
        piperTest.test(
            endpointConfig = CreateMembership.endpointConfig,
            pathParams = mapOf(CreateMembership.orgId to orgRep.id.toString()),
            body = MembershipRepFixtures[0].creation(user0Id)
        ) {}

        // CreateMembership
        val membership1Rep = MembershipRepFixtures[1].complete(this, user1Id)
        orgRep = orgRep.copy(members = orgRep.members.plus(membership1Rep))
        piperTest.test(
            endpointConfig = CreateMembership.endpointConfig,
            pathParams = mapOf(CreateMembership.orgId to orgRep.id.toString()),
            body = MembershipRepFixtures[1].creation(user1Id)
        ) {}

        // DeleteMembership
        orgRep = orgRep.copy(members = orgRep.members.filter { it.userId != user0Id })
        piperTest.test(
            endpointConfig = DeleteMembership.endpointConfig,
            pathParams = mapOf(
                DeleteMembership.orgId to orgRep.id.toString(),
                DeleteMembership.memberId to user0Id.toString()
            )
        ) {}

        // GetOrg
        piperTest.test(
            endpointConfig = GetOrg.endpointConfig,
            pathParams = mapOf("orgId" to orgRep.id.toString())
        ) {
            val actual = objectMapper.readValue<OrgRep.Complete>(response.content!!)
            assertEquals(orgRep, actual)
        }
    }
}
