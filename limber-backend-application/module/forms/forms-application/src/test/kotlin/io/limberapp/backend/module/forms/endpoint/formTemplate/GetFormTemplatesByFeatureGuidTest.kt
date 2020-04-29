package io.limberapp.backend.module.forms.endpoint.formTemplate

import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.backend.module.forms.testing.ResourceTest
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.summary
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class GetFormTemplatesByFeatureGuidTest : ResourceTest() {
    @Test
    fun happyPathNoFormTemplates() {
        val featureGuid = UUID.randomUUID()

        piperTest.test(FormTemplateApi.GetByFeatureGuid(featureGuid)) {
            val actual = json.parseSet<FormTemplateRep.Summary>(response.content!!)
            assertTrue(actual.isEmpty())
        }
    }

    @Test
    fun happyPathMultipleFormTemplates() {
        val featureGuid = UUID.randomUUID()

        val formTemplate0Rep = FormTemplateRepFixtures.exampleFormFixture.complete(this, featureGuid, 0)
        piperTest.setup(FormTemplateApi.Post(FormTemplateRepFixtures.exampleFormFixture.creation(featureGuid)))

        val formTemplate1Rep = FormTemplateRepFixtures.vehicleInspectionFixture.complete(this, featureGuid, 5)
        piperTest.setup(FormTemplateApi.Post(FormTemplateRepFixtures.vehicleInspectionFixture.creation(featureGuid)))

        piperTest.test(FormTemplateApi.GetByFeatureGuid(featureGuid)) {
            val actual = json.parseSet<FormTemplateRep.Summary>(response.content!!)
            assertEquals(setOf(formTemplate0Rep.summary(), formTemplate1Rep.summary()), actual)
        }
    }
}
