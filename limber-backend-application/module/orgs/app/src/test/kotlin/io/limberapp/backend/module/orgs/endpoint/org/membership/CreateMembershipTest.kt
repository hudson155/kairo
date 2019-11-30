package io.limberapp.backend.module.orgs.endpoint.org.membership

import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.http.HttpStatusCode
import io.limberapp.backend.module.orgs.endpoint.org.CreateOrg
import io.limberapp.backend.module.orgs.endpoint.org.GetOrg
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
    fun create() {

        val orgCreationRep = OrgRep.Creation("Cranky Pasta")
        val orgId = deterministicUuidGenerator[0]
        val featureId = deterministicUuidGenerator[1]
        piperTest.test(
            endpointConfig = CreateOrg.endpointConfig,
            body = orgCreationRep
        ) {}

        val userId = UUID.randomUUID()
        val membershipCreationRep = MembershipRep.Creation(userId)
        piperTest.test(
            endpointConfig = CreateMembership.endpointConfig,
            pathParams = mapOf(CreateMembership.orgId to orgId.toString()),
            body = membershipCreationRep
        ) {
            val actual = objectMapper.readValue<MembershipRep.Complete>(response.content!!)
            val expected = MembershipRep.Complete(LocalDateTime.now(fixedClock), userId)
            assertEquals(expected, actual)
        }

        piperTest.test(
            endpointConfig = GetOrg.endpointConfig,
            pathParams = mapOf(GetOrg.orgId to orgId.toString())
        ) {
            val actual = objectMapper.readValue<OrgRep.Complete>(response.content!!)
            val defaultFeature = FeatureRep.Complete(
                id = featureId,
                created = LocalDateTime.now(fixedClock),
                name = DEFAULT_FEATURE_CREATION_REP.name,
                path = DEFAULT_FEATURE_CREATION_REP.path,
                type = DEFAULT_FEATURE_CREATION_REP.type
            )
            val expected = OrgRep.Complete(
                id = orgId,
                created = LocalDateTime.now(fixedClock),
                name = orgCreationRep.name,
                features = listOf(defaultFeature),
                members = listOf(MembershipRep.Complete(LocalDateTime.now(fixedClock), userId))
            )
            assertEquals(expected, actual)
        }
    }

    @Test
    fun createDuplicate() {

        val orgCreationRep = OrgRep.Creation("Cranky Pasta")
        val orgId = deterministicUuidGenerator[0]
        piperTest.test(
            endpointConfig = CreateOrg.endpointConfig,
            body = orgCreationRep
        ) {}

        val userId = UUID.randomUUID()
        val membershipCreationRep = MembershipRep.Creation(userId)
        piperTest.test(
            endpointConfig = CreateMembership.endpointConfig,
            pathParams = mapOf(CreateMembership.orgId to orgId.toString()),
            body = membershipCreationRep
        ) {}
        piperTest.test(
            endpointConfig = CreateMembership.endpointConfig,
            pathParams = mapOf(CreateMembership.orgId to orgId.toString()),
            body = membershipCreationRep,
            expectedStatusCode = HttpStatusCode.Conflict
        ) {}
    }
}
