package io.limberapp.backend.module.orgs.endpoint.org

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.module.orgs.endpoint.org.membership.PostMembership
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import io.limberapp.backend.module.orgs.testing.fixtures.membership.MembershipRepFixtures
import io.limberapp.backend.module.orgs.testing.fixtures.org.OrgRepFixtures
import org.junit.jupiter.api.Test
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
            queryParams = mapOf(GetOrgsByMemberId.memberId to userId)
        ) {
            val actual = objectMapper.readValue<Set<OrgRep.Complete>>(response.content!!)
            assertTrue(actual.isEmpty())
        }
    }

    @Test
    fun happyPathMultipleOrgs() {

        // Setup
        val userId = UUID.randomUUID()

        // PostOrg
        var org0Rep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
        piperTest.setup(
            endpointConfig = PostOrg.endpointConfig,
            body = OrgRepFixtures.crankyPastaFixture.creation()
        )

        // PostMembership
        val membership0Rep = MembershipRepFixtures.fixture.complete(this, userId)
        org0Rep = org0Rep.copy(members = org0Rep.members.plus(membership0Rep))
        piperTest.setup(
            endpointConfig = PostMembership.endpointConfig,
            pathParams = mapOf(PostMembership.orgId to org0Rep.id),
            body = MembershipRepFixtures.fixture.creation(userId)
        )

        // PostOrg
        var org1Rep = OrgRepFixtures.discreetBulbFixture.complete(this, 2)
        piperTest.setup(
            endpointConfig = PostOrg.endpointConfig,
            body = OrgRepFixtures.discreetBulbFixture.creation()
        )

        // PostMembership
        val membership1Rep = MembershipRepFixtures.fixture.complete(this, userId)
        org1Rep = org1Rep.copy(members = org1Rep.members.plus(membership1Rep))
        piperTest.setup(
            endpointConfig = PostMembership.endpointConfig,
            pathParams = mapOf(PostMembership.orgId to org1Rep.id),
            body = MembershipRepFixtures.fixture.creation(userId)
        )

        // GetOrgsByMemberId
        piperTest.test(
            endpointConfig = GetOrgsByMemberId.endpointConfig,
            queryParams = mapOf(GetOrgsByMemberId.memberId to userId)
        ) {
            val actual = objectMapper.readValue<Set<OrgRep.Complete>>(response.content!!)
            assertEquals(setOf(org0Rep, org1Rep), actual)
        }
    }
}
