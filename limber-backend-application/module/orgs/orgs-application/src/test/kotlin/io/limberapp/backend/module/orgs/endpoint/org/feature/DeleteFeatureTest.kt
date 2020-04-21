package io.limberapp.backend.module.orgs.endpoint.org.feature

import io.limberapp.backend.module.orgs.api.org.OrgApi
import io.limberapp.backend.module.orgs.api.org.feature.OrgFeatureApi
import io.limberapp.backend.module.orgs.exception.org.FeatureNotFound
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import io.limberapp.backend.module.orgs.testing.fixtures.feature.FeatureRepFixtures
import io.limberapp.backend.module.orgs.testing.fixtures.org.OrgRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class DeleteFeatureTest : ResourceTest() {

    @Test
    fun orgDoesNotExist() {

        val orgId = UUID.randomUUID()
        val featureId = UUID.randomUUID()

        piperTest.test(
            endpoint = OrgFeatureApi.Delete(orgId, featureId),
            expectedException = FeatureNotFound()
        )
    }

    @Test
    fun featureDoesNotExist() {

        val orgOwnerAccountId = UUID.randomUUID()
        val featureId = UUID.randomUUID()

        val orgRep = OrgRepFixtures.crankyPastaFixture.complete(this, orgOwnerAccountId, 0)
        piperTest.setup(OrgApi.Post(OrgRepFixtures.crankyPastaFixture.creation(orgOwnerAccountId)))

        piperTest.test(
            endpoint = OrgFeatureApi.Delete(orgRep.id, featureId),
            expectedException = FeatureNotFound()
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
        piperTest.setup(OrgFeatureApi.Post(orgRep.id, FeatureRepFixtures.formsFixture.creation()))

        orgRep = orgRep.copy(features = orgRep.features.filter { it.id != featureRep.id })
        piperTest.test(OrgFeatureApi.Delete(orgRep.id, featureRep.id)) {}

        piperTest.test(OrgApi.Get(orgRep.id)) {
            val actual = json.parse<OrgRep.Complete>(response.content!!)
            assertEquals(orgRep, actual)
        }
    }
}
