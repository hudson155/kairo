package io.limberapp.backend.module.orgs.endpoint.org.feature

import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.http.HttpStatusCode
import io.limberapp.backend.module.orgs.endpoint.org.CreateOrg
import io.limberapp.backend.module.orgs.endpoint.org.GetOrg
import io.limberapp.backend.module.orgs.exception.notFound.OrgNotFound
import io.limberapp.backend.module.orgs.mapper.api.org.DEFAULT_FEATURE_CREATION_REP
import io.limberapp.backend.module.orgs.model.org.FeatureModel
import io.limberapp.backend.module.orgs.rep.feature.FeatureRep
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import org.junit.Test
import java.time.LocalDateTime
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

        // DeleteFeature
        piperTest.test(
            endpointConfig = DeleteFeature.endpointConfig,
            pathParams = mapOf(
                DeleteFeature.orgId to orgRep.id.toString(),
                DeleteFeature.featureId to featureId.toString()
            ),
            expectedException = OrgNotFound()
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
        val feature0Rep = FeatureRep.Complete(
            id = deterministicUuidGenerator[2],
            created = LocalDateTime.now(fixedClock),
            name = featureCreationRep.name,
            path = featureCreationRep.path,
            type = featureCreationRep.type
        )
        orgRep = orgRep.copy(features = orgRep.features.plus(feature0Rep))
        piperTest.test(
            endpointConfig = CreateFeature.endpointConfig,
            pathParams = mapOf(CreateFeature.orgId to orgRep.id.toString()),
            body = featureCreationRep
        ) {}

        // DeleteFeature
        orgRep = orgRep.copy(features = orgRep.features.filter { it.id != feature0Rep.id })
        piperTest.test(
            endpointConfig = DeleteFeature.endpointConfig,
            pathParams = mapOf(
                DeleteFeature.orgId to orgRep.id.toString(),
                DeleteFeature.featureId to feature0Rep.id.toString()
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
