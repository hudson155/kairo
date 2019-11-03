package io.limberapp.backend.module.orgs.endpoint.org.membership

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.module.orgs.endpoint.org.CreateOrg
import io.limberapp.backend.module.orgs.endpoint.org.GetOrg
import io.limberapp.backend.module.orgs.rep.membership.MembershipRep
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import org.junit.Test
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.assertEquals

internal class CreateMembershipTest : ResourceTest() {

    @Test
    fun create() {

        lateinit var orgId: UUID
        val orgCreationRep = OrgRep.Creation("Cranky Pasta")
        limberTest.test(
            config = CreateOrg.config,
            body = orgCreationRep
        ) {
            orgId = objectMapper.readValue<OrgRep.Complete>(response.content!!).id
        }

        val userId = UUID.randomUUID()
        val membershipCreationRep = MembershipRep.Creation(userId)
        limberTest.test(
            config = CreateMembership.config,
            pathParams = mapOf("orgId" to orgId.toString()),
            body = membershipCreationRep
        ) {}

        limberTest.test(
            config = GetOrg.config,
            pathParams = mapOf("orgId" to orgId.toString())
        ) {
            val actual = objectMapper.readValue<OrgRep.Complete>(response.content!!)
            val expected = OrgRep.Complete(
                id = uuidGenerator[0],
                created = LocalDateTime.now(clock),
                name = "Cranky Pasta",
                members = listOf(MembershipRep.Complete(actual.members[0].created, userId))
            )
            assertEquals(expected, actual)
        }
    }
}
