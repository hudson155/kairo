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

        val featureId = UUID.randomUUID()

        piperTest.test(
            endpointConfig = GetFormTemplatesByFeatureId.endpointConfig,
            queryParams = mapOf(GetFormTemplatesByFeatureId.featureId to featureId)
        ) {
            val actual = json.parseList<FormTemplateRep.Complete>(response.content!!).toSet()
            assertTrue(actual.isEmpty())
        }
    }

    @Test
    fun happyPathMultipleFormTemplates() {

        val featureId = UUID.randomUUID()

        val formTemplate0Rep = FormTemplateRepFixtures.exampleFormFixture.complete(this, featureId, 0)
        piperTest.setup(
            endpointConfig = PostFormTemplate.endpointConfig,
            body = FormTemplateRepFixtures.exampleFormFixture.creation(featureId)
        )

        val formTemplate1Rep = FormTemplateRepFixtures.vehicleInspectionFixture.complete(this, featureId, 5)
        piperTest.setup(
            endpointConfig = PostFormTemplate.endpointConfig,
            body = FormTemplateRepFixtures.vehicleInspectionFixture.creation(featureId)
        )

        piperTest.test(
            endpointConfig = GetFormTemplatesByFeatureId.endpointConfig,
            queryParams = mapOf(GetFormTemplatesByFeatureId.featureId to featureId)
        ) {
            val actual = json.parseList<FormTemplateRep.Complete>(response.content!!).toSet()
            assertEquals(setOf(formTemplate0Rep, formTemplate1Rep), actual)
        }
    }
}
