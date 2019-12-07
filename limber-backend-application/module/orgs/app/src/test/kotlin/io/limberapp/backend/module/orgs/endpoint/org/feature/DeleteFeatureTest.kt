package io.limberapp.backend.module.orgs.endpoint.org.feature

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.module.orgs.endpoint.org.CreateOrg
import io.limberapp.backend.module.orgs.endpoint.org.GetOrg
import io.limberapp.backend.module.orgs.exception.notFound.FeatureNotFound
import io.limberapp.backend.module.orgs.exception.notFound.OrgNotFound
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import io.limberapp.backend.module.orgs.testing.fixtures.feature.FeatureRepFixtures
import io.limberapp.backend.module.orgs.testing.fixtures.org.OrgRepFixtures
import org.junit.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class DeleteFeatureTest : ResourceTest() {

    @Test
    fun orgDoesNotExist() {

        // Setup
        val orgId = UUID.randomUUID()
        val featureId = UUID.randomUUID()

        // DeleteFeature
        piperTest.test(
            endpointConfig = DeleteFeature.endpointConfig,
            pathParams = mapOf(
                DeleteFeature.orgId to orgId.toString(),
                DeleteFeature.featureId to featureId.toString()
            ),
            expectedException = OrgNotFound()
        )
    }

    @Test
    fun featureDoesNotExist() {

        // Setup
        val featureId = UUID.randomUUID()

        // CreateOrg
        val orgRep = OrgRepFixtures.Complete[0](0)
        piperTest.test(
            endpointConfig = CreateOrg.endpointConfig,
            body = OrgRepFixtures.Creation[0]
        ) {}

        // DeleteFeature
        piperTest.test(
            endpointConfig = DeleteFeature.endpointConfig,
            pathParams = mapOf(
                DeleteFeature.orgId to orgRep.id.toString(),
                DeleteFeature.featureId to featureId.toString()
            ),
            expectedException = FeatureNotFound()
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

        // CreateOrg
        var orgRep = OrgRepFixtures.Complete[0](0)
        piperTest.test(
            endpointConfig = CreateOrg.endpointConfig,
            body = OrgRepFixtures.Creation[0]
        ) {}

        // CreateFeature
        val featureRep = FeatureRepFixtures.Complete[1](2)
        orgRep = orgRep.copy(features = orgRep.features.plus(featureRep))
        piperTest.test(
            endpointConfig = CreateFeature.endpointConfig,
            pathParams = mapOf(CreateFeature.orgId to orgRep.id.toString()),
            body = FeatureRepFixtures.Creation[1]
        ) {}

        // DeleteFeature
        orgRep = orgRep.copy(features = orgRep.features.filter { it.id != featureRep.id })
        piperTest.test(
            endpointConfig = DeleteFeature.endpointConfig,
            pathParams = mapOf(
                DeleteFeature.orgId to orgRep.id.toString(),
                DeleteFeature.featureId to featureRep.id.toString()
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
