package io.limberapp.backend.module.orgs.endpoint.org

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.module.orgs.endpoint.org.membership.CreateMembership
import io.limberapp.backend.module.orgs.mapper.api.org.DEFAULT_FEATURE_CREATION_REP
import io.limberapp.backend.module.orgs.rep.feature.FeatureRep
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
        val org0CreationRep = OrgRep.Creation("Cranky Pasta")
        val org0DefaultFeatureRep = FeatureRep.Complete(
            id = deterministicUuidGenerator[1],
            created = LocalDateTime.now(fixedClock),
            name = DEFAULT_FEATURE_CREATION_REP.name,
            path = DEFAULT_FEATURE_CREATION_REP.path,
            type = DEFAULT_FEATURE_CREATION_REP.type
        )
        var org0Rep = OrgRep.Complete(
            id = deterministicUuidGenerator[0],
            created = LocalDateTime.now(fixedClock),
            name = org0CreationRep.name,
            features = listOf(org0DefaultFeatureRep),
            members = emptyList()
        )
        piperTest.test(
            endpointConfig = CreateOrg.endpointConfig,
            body = org0CreationRep
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
        val org1CreationRep = OrgRep.Creation("Discreet Bulb")
        val org1DefaultFeatureRep = FeatureRep.Complete(
            id = deterministicUuidGenerator[3],
            created = LocalDateTime.now(fixedClock),
            name = DEFAULT_FEATURE_CREATION_REP.name,
            path = DEFAULT_FEATURE_CREATION_REP.path,
            type = DEFAULT_FEATURE_CREATION_REP.type
        )
        var org1Rep = OrgRep.Complete(
            id = deterministicUuidGenerator[2],
            created = LocalDateTime.now(fixedClock),
            name = org1CreationRep.name,
            features = listOf(org1DefaultFeatureRep),
            members = emptyList()
        )
        piperTest.test(
            endpointConfig = CreateOrg.endpointConfig,
            body = org1CreationRep
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
