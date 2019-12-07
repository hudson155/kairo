package io.limberapp.backend.module.orgs.endpoint.org

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.module.orgs.endpoint.org.membership.CreateMembership
import io.limberapp.backend.module.orgs.rep.membership.MembershipRep
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import io.limberapp.backend.module.orgs.testing.fixtures.org.OrgRepFixtures
import org.junit.Test
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class GetOrgsByMemberIdTest : ResourceTest() {

    @Test
    fun happyPathNoOrgs() {

        // Setup
        val userId = UUID.randomUUID()

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
        var org0Rep = OrgRepFixtures.Complete[0](0)
        piperTest.test(
            endpointConfig = CreateOrg.endpointConfig,
            body = OrgRepFixtures.Creation[0]
        ) {}

        // CreateMembership
        val membership0CreationRep = MembershipRep.Creation(userId)
        val membership0Rep = MembershipRep.Complete(
            created = LocalDateTime.now(fixedClock),
            userId = membership0CreationRep.userId
        )
        org0Rep = org0Rep.copy(members = org0Rep.members.plus(membership0Rep))
        piperTest.test(
            endpointConfig = CreateMembership.endpointConfig,
            pathParams = mapOf(CreateMembership.orgId to org0Rep.id.toString()),
            body = membership0CreationRep
        ) {}

        // CreateOrg
        var org1Rep = OrgRepFixtures.Complete[1](2)
        piperTest.test(
            endpointConfig = CreateOrg.endpointConfig,
            body = OrgRepFixtures.Creation[1]
        ) {}

        // CreateMembership
        val membership1CreationRep = MembershipRep.Creation(userId)
        val membership1Rep = MembershipRep.Complete(
            created = LocalDateTime.now(fixedClock),
            userId = membership1CreationRep.userId
        )
        org1Rep = org1Rep.copy(members = org1Rep.members.plus(membership1Rep))
        piperTest.test(
            endpointConfig = CreateMembership.endpointConfig,
            pathParams = mapOf(CreateMembership.orgId to org1Rep.id.toString()),
            body = membership1CreationRep
        ) {}

        piperTest.test(
            endpointConfig = GetOrgsByMemberId.endpointConfig,
            queryParams = mapOf(GetOrgsByMemberId.memberId to userId.toString())
        ) {
            val actual = objectMapper.readValue<List<OrgRep.Complete>>(response.content!!)
            assertEquals(listOf(org0Rep, org1Rep), actual)
        }
    }
}
