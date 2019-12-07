package io.limberapp.backend.module.orgs.endpoint.org

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.testing.ResourceTest
import io.limberapp.backend.module.orgs.testing.util.defaultFeatureRep
import org.junit.Test
import java.time.LocalDateTime
import kotlin.test.assertEquals

internal class CreateOrgTest : ResourceTest() {

    @Test
    fun happyPath() {

        // CreateOrg
        val orgCreationRep = OrgRep.Creation("Cranky Pasta")
        val defaultFeatureRep = defaultFeatureRep(deterministicUuidGenerator[1])
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
        ) {
            val actual = objectMapper.readValue<OrgRep.Complete>(response.content!!)
            assertEquals(orgRep, actual)
        }
    }
}
