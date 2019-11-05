package io.limberapp.backend.module.orgs.endpoint.org

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.module.orgs.endpoint.org.membership.CreateMembership
import io.limberapp.backend.module.orgs.rep.membership.MembershipRep
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import org.junit.Test
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class GetOrgsByMemberIdTest : ResourceTest() {

    @Test
    fun noOrgs() {
        val userId = UUID.randomUUID()
        limberTest.test(
            config = GetOrgsByMemberId.config,
            queryParams = mapOf(GetOrgsByMemberId.memberId to userId.toString())
        ) {
            val actual = objectMapper.readValue<List<OrgRep.Complete>>(response.content!!)
            assertTrue(actual.isEmpty())
        }
    }

    @Test
    fun multipleOrgs() {

        val orgCreationRep = OrgRep.Creation("Cranky Pasta")
        val orgId = uuidGenerator[0]
        limberTest.test(
            config = CreateOrg.config,
            body = orgCreationRep
        ) {}

        val userId = UUID.randomUUID()
        val membershipCreationRep = MembershipRep.Creation(userId)
        limberTest.test(
            config = CreateMembership.config,
            pathParams = mapOf(CreateMembership.orgId to orgId.toString()),
            body = membershipCreationRep
        ) {}

        limberTest.test(
            config = GetOrgsByMemberId.config,
            queryParams = mapOf(GetOrgsByMemberId.memberId to userId.toString())
        ) {
            val actual = objectMapper.readValue<List<OrgRep.Complete>>(response.content!!)
            val expected = listOf(
                OrgRep.Complete(
                    id = orgId,
                    created = LocalDateTime.now(clock),
                    name = orgCreationRep.name,
                    members = listOf(MembershipRep.Complete(LocalDateTime.now(clock), userId))
                )
            )
            assertEquals(expected, actual)
        }
    }
}
