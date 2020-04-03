package io.limberapp.backend.module.orgs.endpoint.org.feature

import io.limberapp.backend.module.orgs.endpoint.org.GetOrg
import io.limberapp.backend.module.orgs.endpoint.org.PostOrg
import io.limberapp.backend.module.orgs.exception.org.FeatureIsNotUnique
import io.limberapp.backend.module.orgs.exception.org.OrgNotFound
import io.limberapp.backend.module.orgs.rep.org.FeatureRep
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import io.limberapp.backend.module.orgs.testing.fixtures.feature.FeatureRepFixtures
import io.limberapp.backend.module.orgs.testing.fixtures.org.OrgRepFixtures
import kotlinx.serialization.parse
import kotlinx.serialization.stringify
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class PostFeatureTest : ResourceTest() {

    @Test
    fun orgDoesNotExist() {

        // Setup
        val orgId = UUID.randomUUID()

        // PostFeature
        piperTest.test(
            endpointConfig = PostFeature.endpointConfig,
            pathParams = mapOf(PostFeature.orgId to orgId),
            body = json.stringify(FeatureRepFixtures.formsFixture.creation()),
            expectedException = OrgNotFound()
        )
    }

    @Test
    fun duplicatePath() {

        // PostOrg
        val orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
        piperTest.setup(
            endpointConfig = PostOrg.endpointConfig,
            body = json.stringify(OrgRepFixtures.crankyPastaFixture.creation())
        )

        // PostFeature
        piperTest.test(
            endpointConfig = PostFeature.endpointConfig,
            pathParams = mapOf(PostFeature.orgId to orgRep.id),
            body = json.stringify(FeatureRepFixtures.formsFixture.creation().copy(path = FeatureRepFixtures.default.creation().path)),
            expectedException = FeatureIsNotUnique()
        )

        // GetOrg
        piperTest.test(
            endpointConfig = GetOrg.endpointConfig,
            pathParams = mapOf(GetOrg.orgId to orgRep.id)
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
        piperTest.test(
            endpointConfig = PostFeature.endpointConfig,
            pathParams = mapOf(PostFeature.orgId to orgRep.id),
            body = json.stringify(FeatureRepFixtures.formsFixture.creation())
        ) {
            val actual = json.parse<FeatureRep.Complete>(response.content!!)
            assertEquals(featureRep, actual)
        }

        // GetOrg
        piperTest.test(
            endpointConfig = GetOrg.endpointConfig,
            pathParams = mapOf(GetOrg.orgId to orgRep.id)
        ) {
            val actual = json.parse<OrgRep.Complete>(response.content!!)
            assertEquals(orgRep, actual)
        }
    }
}
