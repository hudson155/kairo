package io.limberapp.backend.module.orgs.endpoint.org.membership

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.module.orgs.endpoint.org.CreateOrg
import io.limberapp.backend.module.orgs.endpoint.org.GetOrg
import io.limberapp.backend.module.orgs.exception.conflict.ConflictsWithAnotherMembership
import io.limberapp.backend.module.orgs.exception.notFound.OrgNotFound
import io.limberapp.backend.module.orgs.mapper.api.org.DEFAULT_FEATURE_CREATION_REP
import io.limberapp.backend.module.orgs.rep.feature.FeatureRep
import io.limberapp.backend.module.orgs.rep.membership.MembershipRep
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import org.junit.Test
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.assertEquals

internal class CreateMembershipTest : ResourceTest() {

    @Test
    fun orgDoesNotExist() {

        // Setup
        val orgId = UUID.randomUUID()
        val userId = UUID.randomUUID()

        // CreateFeature
        val membershipCreationRep = MembershipRep.Creation(userId)
        piperTest.test(
            endpointConfig = CreateMembership.endpointConfig,
            pathParams = mapOf(CreateMembership.orgId to orgId.toString()),
            body = membershipCreationRep,
            expectedException = OrgNotFound()
        )
    }

    @Test
    fun duplicate() {

        // Setup
        val userId = UUID.randomUUID()

        // CreateOrg
        val orgCreationRep = OrgRep.Creation("Cranky Pasta")
        val defaultFeatureRep = FeatureRep.Complete(
            id = deterministicUuidGenerator[1],
            created = LocalDateTime.now(fixedClock),
            name = DEFAULT_FEATURE_CREATION_REP.name,
            path = DEFAULT_FEATURE_CREATION_REP.path,
            type = DEFAULT_FEATURE_CREATION_REP.type
        )
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
        val membershipCreationRep = MembershipRep.Creation(userId)
        val membershipRep = MembershipRep.Complete(
            created = LocalDateTime.now(fixedClock),
            userId = membershipCreationRep.userId
        )
        orgRep = orgRep.copy(members = orgRep.members.plus(membershipRep))
        piperTest.test(
            endpointConfig = CreateMembership.endpointConfig,
            pathParams = mapOf(CreateMembership.orgId to orgRep.id.toString()),
            body = membershipCreationRep
        ) {}

        // CreateMembership
        piperTest.test(
            endpointConfig = CreateMembership.endpointConfig,
            pathParams = mapOf(CreateMembership.orgId to orgRep.id.toString()),
            body = membershipCreationRep,
            expectedException = ConflictsWithAnotherMembership()
        )

        // GetOrg
        piperTest.test(
            endpointConfig = GetOrg.endpointConfig,
            pathParams = mapOf(GetOrg.orgId to orgRep.id.toString())
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
        val orgCreationRep = OrgRep.Creation("Cranky Pasta")
        val defaultFeatureRep = FeatureRep.Complete(
            id = deterministicUuidGenerator[1],
            created = LocalDateTime.now(fixedClock),
            name = DEFAULT_FEATURE_CREATION_REP.name,
            path = DEFAULT_FEATURE_CREATION_REP.path,
            type = DEFAULT_FEATURE_CREATION_REP.type
        )
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
        val membershipCreationRep = MembershipRep.Creation(userId)
        val membershipRep = MembershipRep.Complete(
            created = LocalDateTime.now(fixedClock),
            userId = membershipCreationRep.userId
        )
        orgRep = orgRep.copy(members = orgRep.members.plus(membershipRep))
        piperTest.test(
            endpointConfig = CreateMembership.endpointConfig,
            pathParams = mapOf(CreateMembership.orgId to orgRep.id.toString()),
            body = membershipCreationRep
        ) {
            val actual = objectMapper.readValue<MembershipRep.Complete>(response.content!!)
            assertEquals(membershipRep, actual)
        }

        // GetOrg
        piperTest.test(
            endpointConfig = GetOrg.endpointConfig,
            pathParams = mapOf(GetOrg.orgId to orgRep.id.toString())
        ) {
            val actual = objectMapper.readValue<OrgRep.Complete>(response.content!!)
            assertEquals(orgRep, actual)
        }
    }
}
