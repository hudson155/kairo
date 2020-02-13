package io.limberapp.backend.module.forms.endpoint.formTemplate

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.backend.module.forms.testing.ResourceTest
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class GetFormTemplatesByOrgIdTest : ResourceTest() {

    @Test
    fun happyPathNoFormTemplates() {

        // Setup
        val orgId = UUID.randomUUID()

        // GetFormTemplatesByOrgId
        piperTest.test(
            endpointConfig = GetFormTemplatesByOrgId.endpointConfig,
            queryParams = mapOf(GetFormTemplatesByOrgId.orgId to orgId)
        ) {
            val actual = objectMapper.readValue<Set<FormTemplateRep.Complete>>(response.content!!)
            assertTrue(actual.isEmpty())
        }
    }

    @Test
    fun happyPathMultipleFormTemplates() {

        // Setup
        val orgId = UUID.randomUUID()

        // PostFormTemplate
        val formTemplate0Rep = FormTemplateRepFixtures.exampleFormFixture.complete(this, orgId, 0)
        piperTest.setup(
            endpointConfig = PostFormTemplate.endpointConfig,
            body = FormTemplateRepFixtures.exampleFormFixture.creation(orgId)
        )

        // PostFormTemplate
        val formTemplate1Rep = FormTemplateRepFixtures.vehicleInspectionFixture.complete(this, orgId, 4)
        piperTest.setup(
            endpointConfig = PostFormTemplate.endpointConfig,
            body = FormTemplateRepFixtures.vehicleInspectionFixture.creation(orgId)
        )

        // GetFormTemplatesByOrgId
        piperTest.test(
            endpointConfig = GetFormTemplatesByOrgId.endpointConfig,
            queryParams = mapOf(GetFormTemplatesByOrgId.orgId to orgId)
        ) {
            val actual = objectMapper.readValue<Set<FormTemplateRep.Complete>>(response.content!!)
            assertEquals(setOf(formTemplate0Rep, formTemplate1Rep), actual)
        }
    }
}
