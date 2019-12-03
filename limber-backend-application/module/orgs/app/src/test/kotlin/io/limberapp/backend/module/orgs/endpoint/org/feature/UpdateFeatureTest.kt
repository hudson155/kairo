package io.limberapp.backend.module.orgs.endpoint.org.feature

import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.http.HttpStatusCode
import io.limberapp.backend.module.orgs.endpoint.org.CreateOrg
import io.limberapp.backend.module.orgs.endpoint.org.GetOrg
import io.limberapp.backend.module.orgs.mapper.api.org.DEFAULT_FEATURE_CREATION_REP
import io.limberapp.backend.module.orgs.model.org.FeatureModel
import io.limberapp.backend.module.orgs.rep.feature.FeatureRep
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import org.junit.Test
import java.time.LocalDateTime
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
            expectedStatusCode = HttpStatusCode.NotFound
        ) {}
    }

    @Test
    fun featureDoesNotExist() {

        // Setup
        val featureId = UUID.randomUUID()

        // CreateOrg
        val orgCreationRep = OrgRep.Creation("Cranky Pasta")
        val defaultFeatureRep = FeatureRep.Complete(
            id = deterministicUuidGenerator[1],
            created = LocalDateTime.now(fixedClock),
            name = DEFAULT_FEATURE_CREATION_REP.name,
            path = DEFAULT_FEATURE_CREATION_REP.path,
            type = DEFAULT_FEATURE_CREATION_REP.type
        )
        val orgRep = OrgRep.Complete(
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

        // UpdateFeature
        val featureUpdateRep = FeatureRep.Update(name = "Renamed Feature")
        piperTest.test(
            endpointConfig = UpdateFeature.endpointConfig,
            pathParams = mapOf(
                UpdateFeature.orgId to orgRep.id.toString(),
                UpdateFeature.featureId to featureId.toString()
            ),
            body = featureUpdateRep,
            expectedStatusCode = HttpStatusCode.NotFound
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

    @Test
    fun nameConflict() {

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

        // CreateFeature
        val featureCreationRep = FeatureRep.Creation("Events", "/events", FeatureModel.Type.HOME)
        val featureRep = FeatureRep.Complete(
            id = deterministicUuidGenerator[2],
            created = LocalDateTime.now(fixedClock),
            name = featureCreationRep.name,
            path = featureCreationRep.path,
            type = featureCreationRep.type
        )
        orgRep = orgRep.copy(features = orgRep.features.plus(featureRep))
        piperTest.test(
            endpointConfig = CreateFeature.endpointConfig,
            pathParams = mapOf(CreateFeature.orgId to orgRep.id.toString()),
            body = featureCreationRep
        ) {}

        // UpdateFeature
        val featureUpdateRep = FeatureRep.Update(name = DEFAULT_FEATURE_CREATION_REP.name)
        piperTest.test(
            endpointConfig = UpdateFeature.endpointConfig,
            pathParams = mapOf(
                UpdateFeature.orgId to orgRep.id.toString(),
                UpdateFeature.featureId to featureRep.id.toString()
            ),
            body = featureUpdateRep,
            expectedStatusCode = HttpStatusCode.Conflict
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

    @Test
    fun pathConflict() {

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

        // CreateFeature
        val featureCreationRep = FeatureRep.Creation("Events", "/events", FeatureModel.Type.HOME)
        val featureRep = FeatureRep.Complete(
            id = deterministicUuidGenerator[2],
            created = LocalDateTime.now(fixedClock),
            name = featureCreationRep.name,
            path = featureCreationRep.path,
            type = featureCreationRep.type
        )
        orgRep = orgRep.copy(features = orgRep.features.plus(featureRep))
        piperTest.test(
            endpointConfig = CreateFeature.endpointConfig,
            pathParams = mapOf(CreateFeature.orgId to orgRep.id.toString()),
            body = featureCreationRep
        ) {}

        // UpdateFeature
        val featureUpdateRep = FeatureRep.Update(path = DEFAULT_FEATURE_CREATION_REP.path)
        piperTest.test(
            endpointConfig = UpdateFeature.endpointConfig,
            pathParams = mapOf(
                UpdateFeature.orgId to orgRep.id.toString(),
                UpdateFeature.featureId to featureRep.id.toString()
            ),
            body = featureUpdateRep,
            expectedStatusCode = HttpStatusCode.Conflict
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

    @Test
    fun happyPath() {

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

        // CreateFeature
        val featureCreationRep = FeatureRep.Creation("Events", "/events", FeatureModel.Type.HOME)
        var featureRep = FeatureRep.Complete(
            id = deterministicUuidGenerator[2],
            created = LocalDateTime.now(fixedClock),
            name = featureCreationRep.name,
            path = featureCreationRep.path,
            type = featureCreationRep.type
        )
        orgRep = orgRep.copy(features = orgRep.features.plus(featureRep))
        piperTest.test(
            endpointConfig = CreateFeature.endpointConfig,
            pathParams = mapOf(CreateFeature.orgId to orgRep.id.toString()),
            body = featureCreationRep
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
