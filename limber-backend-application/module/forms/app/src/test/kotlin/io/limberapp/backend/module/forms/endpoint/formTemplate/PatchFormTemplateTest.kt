package io.limberapp.backend.module.forms.endpoint.formTemplate

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.module.forms.exception.formTemplate.FormTemplateNotFound
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.backend.module.forms.testing.ResourceTest
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class PatchFormTemplateTest : ResourceTest() {

    @Test
    fun doesNotExist() {

        // Setup
        val formTemplateId = UUID.randomUUID()

        // PatchFormTemplate
        val formTemplateUpdateRep = FormTemplateRep.Update("Crazy Form")
        piperTest.test(
            endpointConfig = PatchFormTemplate.endpointConfig,
            pathParams = mapOf(PatchFormTemplate.formTemplateId to formTemplateId),
            body = formTemplateUpdateRep,
            expectedException = FormTemplateNotFound()
        )
    }

    @Test
    fun happyPath() {

        // Setup
        val orgId = UUID.randomUUID()

        // PostFormTemplate
        var formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, orgId, 0)
        piperTest.setup(
            endpointConfig = PostFormTemplate.endpointConfig,
            body = FormTemplateRepFixtures.exampleFormFixture.creation(orgId)
        )

        // PatchFormTemplate
        val formTemplateUpdateRep = FormTemplateRep.Update("Crazy Form")
        formTemplateRep = formTemplateRep.copy(title = formTemplateUpdateRep.title!!)
        piperTest.test(
            endpointConfig = PatchFormTemplate.endpointConfig,
            pathParams = mapOf(PatchFormTemplate.formTemplateId to formTemplateRep.id),
            body = formTemplateUpdateRep
        ) {
            val actual = objectMapper.readValue<FormTemplateRep.Complete>(response.content!!)
            assertEquals(formTemplateRep, actual)
        }

        // GetFormTemplate
        piperTest.test(
            endpointConfig = GetFormTemplate.endpointConfig,
            pathParams = mapOf(GetFormTemplate.formTemplateId to formTemplateRep.id)
        ) {
            val actual = objectMapper.readValue<FormTemplateRep.Complete>(response.content!!)
            assertEquals(formTemplateRep, actual)
        }
    }
}
