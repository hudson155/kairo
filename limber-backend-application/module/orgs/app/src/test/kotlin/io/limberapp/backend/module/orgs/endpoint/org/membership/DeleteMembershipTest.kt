package io.limberapp.backend.module.orgs.endpoint.org.membership

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.module.orgs.endpoint.org.CreateOrg
import io.limberapp.backend.module.orgs.endpoint.org.GetOrg
import io.limberapp.backend.module.orgs.exception.notFound.MembershipNotFound
import io.limberapp.backend.module.orgs.exception.notFound.OrgNotFound
import io.limberapp.backend.module.orgs.rep.membership.MembershipRep
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import io.limberapp.backend.module.orgs.testing.util.defaultFeatureRep
import org.junit.Test
import java.time.LocalDateTime
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
        val orgCreationRep = OrgRep.Creation("Cranky Pasta")
        val defaultFeatureRep = defaultFeatureRep(deterministicUuidGenerator[1])
        val orgRep = OrgRep.Complete(
            id = deterministicUuidGenerator[0],
            created = LocalDateTime.now(fixedClock),
            name = orgCreationRep.name,
            features = listOf(defaultFeatureRep),
            members = emptyList()
        )
        piperTest.test(
            endpointConfig = CreateOrg.endpointConfig,
            body = orgCreationRep
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
        val orgCreationRep = OrgRep.Creation("Cranky Pasta")
        val defaultFeatureRep = defaultFeatureRep(deterministicUuidGenerator[1])
        var orgRep = OrgRep.Complete(
            id = deterministicUuidGenerator[0],
            created = LocalDateTime.now(fixedClock),
            name = orgCreationRep.name,
            features = listOf(defaultFeatureRep),
            members = emptyList()
        )
        piperTest.test(
            endpointConfig = CreateOrg.endpointConfig,
            body = orgCreationRep
        ) {}

        // CreateMembership
        val membership0CreationRep = MembershipRep.Creation(user0Id)
        val membership0Rep = MembershipRep.Complete(
            created = LocalDateTime.now(fixedClock),
            userId = membership0CreationRep.userId
        )
        orgRep = orgRep.copy(members = orgRep.members.plus(membership0Rep))
        piperTest.test(
            endpointConfig = CreateMembership.endpointConfig,
            pathParams = mapOf(CreateMembership.orgId to orgRep.id.toString()),
            body = membership0CreationRep
        ) {}

        // CreateMembership
        val membership1CreationRep = MembershipRep.Creation(user1Id)
        val membership1Rep = MembershipRep.Complete(
            created = LocalDateTime.now(fixedClock),
            userId = membership1CreationRep.userId
        )
        orgRep = orgRep.copy(members = orgRep.members.plus(membership1Rep))
        piperTest.test(
            endpointConfig = CreateMembership.endpointConfig,
            pathParams = mapOf(CreateMembership.orgId to orgRep.id.toString()),
            body = membership1CreationRep
        ) {}

        // DeleteMembership
        orgRep = orgRep.copy(members = orgRep.members.filter { it.userId != membership0Rep.userId })
        piperTest.test(
            endpointConfig = DeleteMembership.endpointConfig,
            pathParams = mapOf(
                DeleteMembership.orgId to orgRep.id.toString(),
                DeleteMembership.memberId to membership0Rep.userId.toString()
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
