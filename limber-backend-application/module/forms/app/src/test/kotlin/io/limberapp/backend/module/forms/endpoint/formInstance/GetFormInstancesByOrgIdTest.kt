package io.limberapp.backend.module.forms.endpoint.formInstance

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.module.forms.endpoint.formTemplate.PostFormTemplate
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import io.limberapp.backend.module.forms.testing.ResourceTest
import io.limberapp.backend.module.forms.testing.fixtures.formInstance.FormInstanceRepFixtures
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class GetFormInstancesByOrgIdTest : ResourceTest() {

    @Test
    fun happyPathNoFormInstances() {

        // Setup
        val orgId = UUID.randomUUID()

        // GetFormInstancesByOrgId
        piperTest.test(
            endpointConfig = GetFormInstancesByOrgId.endpointConfig,
            queryParams = mapOf(GetFormInstancesByOrgId.orgId to orgId)
        ) {
            val actual = objectMapper.readValue<Set<FormInstanceRep.Complete>>(response.content!!)
            assertTrue(actual.isEmpty())
        }
    }

    @Test
    fun happyPathMultipleFormInstances() {

        // Setup
        val orgId = UUID.randomUUID()

        // PostFormTemplate
        val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, orgId, 0)
        piperTest.setup(
            endpointConfig = PostFormTemplate.endpointConfig,
            body = FormTemplateRepFixtures.exampleFormFixture.creation(orgId)
        )

        // PostFormInstance
        val formInstance0Rep = FormInstanceRepFixtures.fixture.complete(this, orgId, formTemplateRep.id, 4)
        piperTest.setup(
            endpointConfig = PostFormInstance.endpointConfig,
            body = FormInstanceRepFixtures.fixture.creation(orgId, formTemplateRep.id)
        )

        // PostFormInstance
        val formInstance1Rep = FormInstanceRepFixtures.fixture.complete(this, orgId, formTemplateRep.id, 5)
        piperTest.setup(
            endpointConfig = PostFormInstance.endpointConfig,
            body = FormInstanceRepFixtures.fixture.creation(orgId, formTemplateRep.id)
        )

        // GetFormInstancesByOrgId
        piperTest.test(
            endpointConfig = GetFormInstancesByOrgId.endpointConfig,
            queryParams = mapOf(GetFormInstancesByOrgId.orgId to orgId)
        ) {
            val actual = objectMapper.readValue<Set<FormInstanceRep.Complete>>(response.content!!)
            assertEquals(setOf(formInstance0Rep, formInstance1Rep), actual)
        }
    }
}
