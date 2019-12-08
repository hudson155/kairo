package io.limberapp.backend.module.orgs.endpoint.org

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.module.orgs.endpoint.org.membership.CreateMembership
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import io.limberapp.backend.module.orgs.testing.fixtures.membership.MembershipRepFixtures
import io.limberapp.backend.module.orgs.testing.fixtures.org.OrgRepFixtures
import org.junit.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class GetOrgsByMemberIdTest : ResourceTest() {

    @Test
    fun happyPathNoOrgs() {

        // Setup
        val userId = UUID.randomUUID()

        // GetOrgsByMemberId
        piperTest.test(
            endpointConfig = GetOrgsByMemberId.endpointConfig,
            queryParams = mapOf(GetOrgsByMemberId.memberId to userId.toString())
        ) {
            val actual = objectMapper.readValue<List<OrgRep.Complete>>(response.content!!)
            assertTrue(actual.isEmpty())
        }
    }

    @Test
    fun happyPathMultipleOrgs() {

        // Setup
        val userId = UUID.randomUUID()

        // CreateOrg
        var org0Rep = OrgRepFixtures[0].complete(this, 0)
        piperTest.test(
            endpointConfig = CreateOrg.endpointConfig,
            body = OrgRepFixtures[0].creation()
        ) {}

        // CreateMembership
        val membership0Rep = MembershipRepFixtures[0].complete(this, userId)
        org0Rep = org0Rep.copy(members = org0Rep.members.plus(membership0Rep))
        piperTest.test(
            endpointConfig = CreateMembership.endpointConfig,
            pathParams = mapOf(CreateMembership.orgId to org0Rep.id.toString()),
            body = MembershipRepFixtures[0].creation(userId)
        ) {}

        // CreateOrg
        var org1Rep = OrgRepFixtures[1].complete(this, 2)
        piperTest.test(
            endpointConfig = CreateOrg.endpointConfig,
            body = OrgRepFixtures[1].creation()
        ) {}

        // CreateMembership
        val membership1Rep = MembershipRepFixtures[1].complete(this, userId)
        org1Rep = org1Rep.copy(members = org1Rep.members.plus(membership1Rep))
        piperTest.test(
            endpointConfig = CreateMembership.endpointConfig,
            pathParams = mapOf(CreateMembership.orgId to org1Rep.id.toString()),
            body = MembershipRepFixtures[1].creation(userId)
        ) {}

        // GetOrgsByMemberId
        piperTest.test(
            endpointConfig = GetOrgsByMemberId.endpointConfig,
            queryParams = mapOf(GetOrgsByMemberId.memberId to userId.toString())
        ) {
            val actual = objectMapper.readValue<List<OrgRep.Complete>>(response.content!!)
            assertEquals(listOf(org0Rep, org1Rep), actual)
        }
    }
}
