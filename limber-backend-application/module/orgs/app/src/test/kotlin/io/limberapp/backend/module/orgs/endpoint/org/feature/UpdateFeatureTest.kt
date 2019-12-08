package io.limberapp.backend.module.orgs.endpoint.org.feature

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.module.orgs.endpoint.org.CreateOrg
import io.limberapp.backend.module.orgs.endpoint.org.GetOrg
import io.limberapp.backend.module.orgs.exception.conflict.ConflictsWithAnotherFeature
import io.limberapp.backend.module.orgs.exception.notFound.FeatureNotFound
import io.limberapp.backend.module.orgs.exception.notFound.OrgNotFound
import io.limberapp.backend.module.orgs.rep.feature.FeatureRep
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import io.limberapp.backend.module.orgs.testing.fixtures.feature.FeatureRepFixtures
import io.limberapp.backend.module.orgs.testing.fixtures.org.OrgRepFixtures
import org.junit.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class UpdateFeatureTest : ResourceTest() {

    @Test
    fun orgDoesNotExist() {

        // Setup
        val orgId = UUID.randomUUID()
        val featureId = UUID.randomUUID()

        // UpdateFeature
        val featureUpdateRep = FeatureRep.Update(name = "Renamed Feature")
        piperTest.test(
            endpointConfig = UpdateFeature.endpointConfig,
            pathParams = mapOf(
                UpdateFeature.orgId to orgId.toString(),
                UpdateFeature.featureId to featureId.toString()
            ),
            body = featureUpdateRep,
            expectedException = OrgNotFound()
        )
    }

    @Test
    fun featureDoesNotExist() {

        // Setup
        val featureId = UUID.randomUUID()

        // CreateOrg
        val orgRep = OrgRepFixtures[0].complete(this, 0)
        piperTest.test(
            endpointConfig = CreateOrg.endpointConfig,
            body = OrgRepFixtures[0].creation()
        ) {}

        // UpdateFeature
        val featureUpdateRep = FeatureRep.Update(name = "Renamed Feature")
        piperTest.test(
            endpointConfig = UpdateFeature.endpointConfig,
            pathParams = mapOf(
                UpdateFeature.orgId to orgRep.id.toString(),
                UpdateFeature.featureId to featureId.toString()
            ),
            body = featureUpdateRep,
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
    fun nameConflict() {

        // CreateOrg
        var orgRep = OrgRepFixtures[0].complete(this, 0)
        piperTest.test(
            endpointConfig = CreateOrg.endpointConfig,
            body = OrgRepFixtures[0].creation()
        ) {}

        // CreateFeature
        val featureRep = FeatureRepFixtures[0].complete(this, 2)
        orgRep = orgRep.copy(features = orgRep.features.plus(featureRep))
        piperTest.test(
            endpointConfig = CreateFeature.endpointConfig,
            pathParams = mapOf(CreateFeature.orgId to orgRep.id.toString()),
            body = FeatureRepFixtures[0].creation()
        ) {}

        // UpdateFeature
        val featureUpdateRep = FeatureRep.Update(name = orgRep.features.first().name)
        piperTest.test(
            endpointConfig = UpdateFeature.endpointConfig,
            pathParams = mapOf(
                UpdateFeature.orgId to orgRep.id.toString(),
                UpdateFeature.featureId to featureRep.id.toString()
            ),
            body = featureUpdateRep,
            expectedException = ConflictsWithAnotherFeature()
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
    fun pathConflict() {

        // CreateOrg
        var orgRep = OrgRepFixtures[0].complete(this, 0)
        piperTest.test(
            endpointConfig = CreateOrg.endpointConfig,
            body = OrgRepFixtures[0].creation()
        ) {}

        // CreateFeature
        val featureRep = FeatureRepFixtures[0].complete(this, 2)
        orgRep = orgRep.copy(features = orgRep.features.plus(featureRep))
        piperTest.test(
            endpointConfig = CreateFeature.endpointConfig,
            pathParams = mapOf(CreateFeature.orgId to orgRep.id.toString()),
            body = FeatureRepFixtures[0].creation()
        ) {}

        // UpdateFeature
        val featureUpdateRep = FeatureRep.Update(path = orgRep.features.first().path)
        piperTest.test(
            endpointConfig = UpdateFeature.endpointConfig,
            pathParams = mapOf(
                UpdateFeature.orgId to orgRep.id.toString(),
                UpdateFeature.featureId to featureRep.id.toString()
            ),
            body = featureUpdateRep,
            expectedException = ConflictsWithAnotherFeature()
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
        var orgRep = OrgRepFixtures[0].complete(this, 0)
        piperTest.test(
            endpointConfig = CreateOrg.endpointConfig,
            body = OrgRepFixtures[0].creation()
        ) {}

        // CreateFeature
        var featureRep = FeatureRepFixtures[0].complete(this, 2)
        orgRep = orgRep.copy(features = orgRep.features.plus(featureRep))
        piperTest.test(
            endpointConfig = CreateFeature.endpointConfig,
            pathParams = mapOf(CreateFeature.orgId to orgRep.id.toString()),
            body = FeatureRepFixtures[0].creation()
        ) {}

        // UpdateFeature
        val featureUpdateRep = FeatureRep.Update(name = "Renamed Feature")
        featureRep = featureRep.copy(name = featureUpdateRep.name!!)
        orgRep = orgRep.copy(features = orgRep.features.map { if (it.id == featureRep.id) featureRep else it })
        piperTest.test(
            endpointConfig = UpdateFeature.endpointConfig,
            pathParams = mapOf(
                UpdateFeature.orgId to orgRep.id.toString(),
                UpdateFeature.featureId to featureRep.id.toString()
            ),
            body = featureUpdateRep
        ) {
            val actual = objectMapper.readValue<FeatureRep.Complete>(response.content!!)
            assertEquals(featureRep, actual)
        }

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
