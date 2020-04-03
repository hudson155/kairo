package io.limberapp.backend.module.forms.endpoint.formTemplate

import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.backend.module.forms.testing.ResourceTest
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import kotlinx.serialization.parse
import kotlinx.serialization.stringify
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class PostFormTemplateTest : ResourceTest() {

    @Test
    fun happyPath() {

        // Setup
        val featureId = UUID.randomUUID()

        // PostFormTemplate
        val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, featureId, 0)
        piperTest.test(
            endpointConfig = PostFormTemplate.endpointConfig,
            body = json.stringify(FormTemplateRepFixtures.exampleFormFixture.creation(featureId))
        ) {
            val actual = json.parse<FormTemplateRep.Complete>(response.content!!)
            assertEquals(formTemplateRep, actual)
        }

        // GetFormTemplate
        piperTest.test(
            endpointConfig = GetFormTemplate.endpointConfig,
            pathParams = mapOf(GetFormTemplate.formTemplateId to formTemplateRep.id)
        ) {
            val actual = json.parse<FormTemplateRep.Complete>(response.content!!)
            assertEquals(formTemplateRep, actual)
        }
    }
}
