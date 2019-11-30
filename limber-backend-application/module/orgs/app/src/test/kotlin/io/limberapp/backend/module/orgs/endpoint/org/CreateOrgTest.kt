package io.limberapp.backend.module.orgs.endpoint.org

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.module.orgs.mapper.api.org.DEFAULT_FEATURE_CREATION_REP
import io.limberapp.backend.module.orgs.rep.feature.FeatureRep
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import org.junit.Test
import java.time.LocalDateTime
import kotlin.test.assertEquals

internal class CreateOrgTest : ResourceTest() {

    @Test
    fun create() {
        val creationRep = OrgRep.Creation("Cranky Pasta")
        val id = deterministicUuidGenerator[0]
        val featureId = deterministicUuidGenerator[1]
        piperTest.test(
            endpointConfig = CreateOrg.endpointConfig,
            body = creationRep
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
                id = id,
                created = LocalDateTime.now(fixedClock),
                name = creationRep.name,
                features = listOf(defaultFeature),
                members = emptyList()
            )
            assertEquals(expected, actual)
        }
    }
}
