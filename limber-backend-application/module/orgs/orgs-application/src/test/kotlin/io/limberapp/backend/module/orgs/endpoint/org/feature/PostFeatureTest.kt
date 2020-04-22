package io.limberapp.backend.module.orgs.endpoint.org.feature

import io.limberapp.backend.module.orgs.api.org.OrgApi
import io.limberapp.backend.module.orgs.api.org.feature.OrgFeatureApi
import io.limberapp.backend.module.orgs.exception.org.FeatureIsNotUnique
import io.limberapp.backend.module.orgs.exception.org.OrgNotFound
import io.limberapp.backend.module.orgs.rep.org.FeatureRep
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import io.limberapp.backend.module.orgs.testing.fixtures.feature.FeatureRepFixtures
import io.limberapp.backend.module.orgs.testing.fixtures.org.OrgRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class PostFeatureTest : ResourceTest() {

    @Test
    fun orgDoesNotExist() {

        val orgId = UUID.randomUUID()

        piperTest.test(
            endpoint = OrgFeatureApi.Post(orgId, FeatureRepFixtures.formsFixture.creation()),
            expectedException = OrgNotFound()
        )
    }

    @Test
    fun duplicatePath() {

        val orgOwnerAccountId = UUID.randomUUID()

        val orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, orgOwnerAccountId, 0)
        piperTest.setup(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation(orgOwnerAccountId)))

        piperTest.test(
            endpoint = OrgFeatureApi.Post(
                orgId = orgRep.id,
                rep = FeatureRepFixtures.formsFixture.creation().copy(path = FeatureRepFixtures.default.creation().path)
            ),
            expectedException = FeatureIsNotUnique()
        )

        piperTest.test(OrgApi.Get(orgRep.id)) {
            val actual = json.parse<OrgRep.Complete>(response.content!!)
            assertEquals(orgRep, actual)
        }
    }

    @Test
    fun happyPath() {

        val orgOwnerAccountId = UUID.randomUUID()

        var orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, orgOwnerAccountId, 0)
        piperTest.setup(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation(orgOwnerAccountId)))

        val featureRep = FeatureRepFixtures.formsFixture.complete(this, 2)
        orgRep = orgRep.copy(features = orgRep.features.plus(featureRep))
        piperTest.test(OrgFeatureApi.Post(orgRep.id, FeatureRepFixtures.formsFixture.creation())) {
            val actual = json.parse<FeatureRep.Complete>(response.content!!)
            assertEquals(featureRep, actual)
        }

        piperTest.test(OrgApi.Get(orgRep.id)) {
            val actual = json.parse<OrgRep.Complete>(response.content!!)
            assertEquals(orgRep, actual)
        }
    }
}
