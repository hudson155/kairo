package io.limberapp.backend.module.forms.endpoint.formTemplate

import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.backend.module.forms.testing.ResourceTest
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class GetFormTemplatesByFeatureIdTest : ResourceTest() {

    @Test
    fun happyPathNoFormTemplates() {

        // Setup
        val featureId = UUID.randomUUID()

        // GetFormTemplatesByFeatureId
        piperTest.test(
            endpointConfig = GetFormTemplatesByFeatureId.endpointConfig,
            queryParams = mapOf(GetFormTemplatesByFeatureId.featureId to featureId)
        ) {
            val actual = json.parse<List<FormTemplateRep.Complete>>(response.content!!)
            assertTrue(actual.isEmpty())
        }
    }

    @Test
    fun happyPathMultipleFormTemplates() {

        // Setup
        val featureId = UUID.randomUUID()

        // PostFormTemplate
        val formTemplate0Rep = FormTemplateRepFixtures.exampleFormFixture.complete(this, featureId, 0)
        piperTest.setup(
            endpointConfig = PostFormTemplate.endpointConfig,
            body = FormTemplateRepFixtures.exampleFormFixture.creation(featureId)
        )

        // PostFormTemplate
        val formTemplate1Rep = FormTemplateRepFixtures.vehicleInspectionFixture.complete(this, featureId, 4)
        piperTest.setup(
            endpointConfig = PostFormTemplate.endpointConfig,
            body = FormTemplateRepFixtures.vehicleInspectionFixture.creation(featureId)
        )

        // GetFormTemplatesByFeatureId
        piperTest.test(
            endpointConfig = GetFormTemplatesByFeatureId.endpointConfig,
            queryParams = mapOf(GetFormTemplatesByFeatureId.featureId to featureId)
        ) {
            val actual = json.parse<List<FormTemplateRep.Complete>>(response.content!!).toSet()
            assertEquals(setOf(formTemplate0Rep, formTemplate1Rep), actual)
        }
    }
}
