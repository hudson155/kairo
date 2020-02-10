package io.limberapp.backend.module.orgs.endpoint.org.membership

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.module.orgs.endpoint.org.PostOrg
import io.limberapp.backend.module.orgs.endpoint.org.GetOrg
import io.limberapp.backend.module.orgs.exception.org.UserIsAlreadyAMemberOfOrg
import io.limberapp.backend.module.orgs.exception.org.OrgNotFound
import io.limberapp.backend.module.orgs.rep.org.MembershipRep
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import io.limberapp.backend.module.orgs.testing.fixtures.membership.MembershipRepFixtures
import io.limberapp.backend.module.orgs.testing.fixtures.org.OrgRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class PostMembershipTest : ResourceTest() {

    @Test
    fun orgDoesNotExist() {

        // Setup
        val orgId = UUID.randomUUID()
        val userId = UUID.randomUUID()

        // PostMembership
        piperTest.test(
            endpointConfig = PostMembership.endpointConfig,
            pathParams = mapOf(PostMembership.orgId to orgId),
            body = MembershipRepFixtures.fixture.creation(userId),
            expectedException = OrgNotFound()
        )
    }

    @Test
    fun duplicate() {

        // Setup
        val userId = UUID.randomUUID()

        // PostOrg
        var orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
        piperTest.setup(
            endpointConfig = PostOrg.endpointConfig,
            body = OrgRepFixtures.crankyPastaFixture.creation()
        )

        // PostMembership
        val membershipRep = MembershipRepFixtures.fixture.complete(this, userId)
        orgRep = orgRep.copy(members = orgRep.members.plus(membershipRep))
        piperTest.setup(
            endpointConfig = PostMembership.endpointConfig,
            pathParams = mapOf(PostMembership.orgId to orgRep.id),
            body = MembershipRepFixtures.fixture.creation(userId)
        )

        // PostMembership
        piperTest.test(
            endpointConfig = PostMembership.endpointConfig,
            pathParams = mapOf(PostMembership.orgId to orgRep.id),
            body = MembershipRepFixtures.fixture.creation(userId),
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

        // PostOrg
        var orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
        piperTest.setup(
            endpointConfig = PostOrg.endpointConfig,
            body = OrgRepFixtures.crankyPastaFixture.creation()
        )

        // PostMembership
        val membershipRep = MembershipRepFixtures.fixture.complete(this, userId)
        orgRep = orgRep.copy(members = orgRep.members.plus(membershipRep))
        piperTest.test(
            endpointConfig = PostMembership.endpointConfig,
            pathParams = mapOf(PostMembership.orgId to orgRep.id),
            body = MembershipRepFixtures.fixture.creation(userId)
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
