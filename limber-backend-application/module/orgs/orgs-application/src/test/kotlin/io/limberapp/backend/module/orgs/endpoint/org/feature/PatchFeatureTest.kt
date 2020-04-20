package io.limberapp.backend.module.orgs.endpoint.org.feature

import io.limberapp.backend.module.orgs.api.org.OrgApi
import io.limberapp.backend.module.orgs.api.org.feature.OrgFeatureApi
import io.limberapp.backend.module.orgs.exception.org.FeatureIsNotUnique
import io.limberapp.backend.module.orgs.exception.org.FeatureNotFound
import io.limberapp.backend.module.orgs.rep.org.FeatureRep
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import io.limberapp.backend.module.orgs.testing.fixtures.feature.FeatureRepFixtures
import io.limberapp.backend.module.orgs.testing.fixtures.org.OrgRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class PatchFeatureTest : ResourceTest() {

    @Test
    fun orgDoesNotExist() {

        val orgId = UUID.randomUUID()
        val featureId = UUID.randomUUID()

        val featureUpdateRep = FeatureRep.Update(name = "Renamed Feature")
        piperTest.test(
            endpoint = OrgFeatureApi.Patch(orgId, featureId, featureUpdateRep),
            expectedException = FeatureNotFound()
        )
    }

    @Test
    fun featureDoesNotExist() {

        val featureId = UUID.randomUUID()

        val orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
        piperTest.setup(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation()))

        val featureUpdateRep = FeatureRep.Update(name = "Renamed Feature")
        piperTest.test(
            endpoint = OrgFeatureApi.Patch(orgRep.id, featureId, featureUpdateRep),
            expectedException = FeatureNotFound()
        )

        piperTest.test(OrgApi.Get(orgRep.id)) {
            val actual = json.parse<OrgRep.Complete>(response.content!!)
            assertEquals(orgRep, actual)
        }
    }

    @Test
    fun pathConflict() {

        var orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
        piperTest.setup(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation()))

        val featureRep = FeatureRepFixtures.formsFixture.complete(this, 2)
        orgRep = orgRep.copy(features = orgRep.features.plus(featureRep))
        piperTest.setup(OrgFeatureApi.Post(orgRep.id, FeatureRepFixtures.formsFixture.creation()))

        val featureUpdateRep = FeatureRep.Update(path = orgRep.features.first().path)
        piperTest.test(
            endpoint = OrgFeatureApi.Patch(orgRep.id, featureRep.id, featureUpdateRep),
            expectedException = FeatureIsNotUnique()
        )

        piperTest.test(OrgApi.Get(orgRep.id)) {
            val actual = json.parse<OrgRep.Complete>(response.content!!)
            assertEquals(orgRep, actual)
        }
    }

    @Test
    fun happyPath() {

        var orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
        piperTest.setup(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation()))

        var featureRep = FeatureRepFixtures.formsFixture.complete(this, 2)
        orgRep = orgRep.copy(features = orgRep.features.plus(featureRep))
        piperTest.setup(OrgFeatureApi.Post(orgRep.id, FeatureRepFixtures.formsFixture.creation()))

        val featureUpdateRep = FeatureRep.Update(name = "Renamed Feature")
        featureRep = featureRep.copy(name = featureUpdateRep.name!!)
        orgRep = orgRep.copy(features = orgRep.features.map { if (it.id == featureRep.id) featureRep else it })
        piperTest.test(OrgFeatureApi.Patch(orgRep.id, featureRep.id, featureUpdateRep)) {
            val actual = json.parse<FeatureRep.Complete>(response.content!!)
            assertEquals(featureRep, actual)
        }

        piperTest.test(OrgApi.Get(orgRep.id)) {
            val actual = json.parse<OrgRep.Complete>(response.content!!)
            assertEquals(orgRep, actual)
        }
    }

    @Test
    fun happyPathSetAndRemoveDefault() {

        var orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, 0)
        piperTest.setup(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation()))

        var featureRep = FeatureRepFixtures.formsFixture.complete(this, 2)
        orgRep = orgRep.copy(features = orgRep.features.plus(featureRep))
        piperTest.setup(OrgFeatureApi.Post(orgRep.id, FeatureRepFixtures.formsFixture.creation()))

        val featureUpdateRep0 = FeatureRep.Update(isDefaultFeature = true)
        featureRep = featureRep.copy(isDefaultFeature = true)
        orgRep = orgRep.copy(
            features = orgRep.features.map {
                if (it.id == featureRep.id) featureRep else it.copy(isDefaultFeature = false)
            }
        )
        piperTest.test(OrgFeatureApi.Patch(orgRep.id, featureRep.id, featureUpdateRep0)) {
            val actual = json.parse<FeatureRep.Complete>(response.content!!)
            assertEquals(featureRep, actual)
        }

        val featureUpdateRep1 = FeatureRep.Update(isDefaultFeature = false)
        featureRep = featureRep.copy(isDefaultFeature = false)
        orgRep = orgRep.copy(features = orgRep.features.map { if (it.id == featureRep.id) featureRep else it })
        piperTest.test(OrgFeatureApi.Patch(orgRep.id, featureRep.id, featureUpdateRep1)) {
            val actual = json.parse<FeatureRep.Complete>(response.content!!)
            assertEquals(featureRep, actual)
        }

        piperTest.test(OrgApi.Get(orgRep.id)) {
            val actual = json.parse<OrgRep.Complete>(response.content!!)
            assertEquals(orgRep, actual)
        }
    }
}
