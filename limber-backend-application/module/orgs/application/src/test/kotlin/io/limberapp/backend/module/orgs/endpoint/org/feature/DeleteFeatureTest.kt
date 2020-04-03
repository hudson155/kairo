package io.limberapp.backend.module.orgs.endpoint.org.feature

import io.limberapp.backend.module.orgs.endpoint.org.GetOrg
import io.limberapp.backend.module.orgs.endpoint.org.PostOrg
import io.limberapp.backend.module.orgs.exception.org.FeatureNotFound
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import io.limberapp.backend.module.orgs.testing.fixtures.feature.FeatureRepFixtures
import io.limberapp.backend.module.orgs.testing.fixtures.org.OrgRepFixtures
import kotlinx.serialization.parse
import kotlinx.serialization.stringify
import org.junit.jupiter.api.Test
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
                DeleteFeature.orgId to orgId,
                DeleteFeature.featureId to featureId
            ),
            expectedException = FeatureNotFound()
        )
    }

    @Test
    fun featureDoesNotExist() {

        // Setup
        val featureId = UUID.randomUUID()

        // PostOrg
        val orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
        piperTest.setup(
            endpointConfig = PostOrg.endpointConfig,
            body = json.stringify(OrgRepFixtures.crankyPastaFixture.creation())
        )

        // DeleteFeature
        piperTest.test(
            endpointConfig = DeleteFeature.endpointConfig,
            pathParams = mapOf(
                DeleteFeature.orgId to orgRep.id,
                DeleteFeature.featureId to featureId
            ),
            expectedException = FeatureNotFound()
        )

        // GetOrg
        piperTest.test(
            endpointConfig = GetOrg.endpointConfig,
            pathParams = mapOf("orgId" to orgRep.id)
        ) {
            val actual = json.parse<OrgRep.Complete>(response.content!!)
            assertEquals(orgRep, actual)
        }
    }

    @Test
    fun happyPath() {

        // PostOrg
        var orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
        piperTest.setup(
            endpointConfig = PostOrg.endpointConfig,
            body = json.stringify(OrgRepFixtures.crankyPastaFixture.creation())
        )

        // PostFeature
        val featureRep = FeatureRepFixtures.formsFixture.complete(this, 2)
        orgRep = orgRep.copy(features = orgRep.features.plus(featureRep))
        piperTest.setup(
            endpointConfig = PostFeature.endpointConfig,
            pathParams = mapOf(PostFeature.orgId to orgRep.id),
            body = json.stringify(FeatureRepFixtures.formsFixture.creation())
        )

        // DeleteFeature
        orgRep = orgRep.copy(features = orgRep.features.filter { it.id != featureRep.id })
        piperTest.test(
            endpointConfig = DeleteFeature.endpointConfig,
            pathParams = mapOf(
                DeleteFeature.orgId to orgRep.id,
                DeleteFeature.featureId to featureRep.id
            )
        ) {}

        // GetOrg
        piperTest.test(
            endpointConfig = GetOrg.endpointConfig,
            pathParams = mapOf("orgId" to orgRep.id)
        ) {
            val actual = json.parse<OrgRep.Complete>(response.content!!)
            assertEquals(orgRep, actual)
        }
    }
}
