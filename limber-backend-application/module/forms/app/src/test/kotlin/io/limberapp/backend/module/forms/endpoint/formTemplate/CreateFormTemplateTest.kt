package io.limberapp.backend.module.forms.endpoint.formTemplate

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.module.forms.exception.conflict.ConflictsWithAnotherFormTemplate
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.backend.module.forms.testing.ResourceTest
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import org.junit.Ignore
import org.junit.Test
import java.util.UUID
import kotlin.test.assertEquals

@Ignore
internal class CreateFormTemplateTest : ResourceTest() {

    @Test
    fun duplicateTitle() {

        // Setup
        val orgId = UUID.randomUUID()

        // CreateFormTemplate
        val formTemplate0Rep = FormTemplateRepFixtures[0].complete(this, orgId, 0)
        piperTest.setup(
            endpointConfig = CreateFormTemplate.endpointConfig,
            body = FormTemplateRepFixtures[0].creation(orgId)
        )

        // CreateFormTemplate
        piperTest.test(
            endpointConfig = CreateFormTemplate.endpointConfig,
            body = FormTemplateRepFixtures[1].creation(orgId).copy(title = formTemplate0Rep.title),
            expectedException = ConflictsWithAnotherFormTemplate()
        )
    }

    @Test
    fun happyPath() {

        // Setup
        val orgId = UUID.randomUUID()

        // CreateFormTemplate
        val formTemplateRep = FormTemplateRepFixtures[0].complete(this, orgId, 0)
        piperTest.test(
            endpointConfig = CreateFormTemplate.endpointConfig,
            body = FormTemplateRepFixtures[0].creation(orgId)
        ) {
            val actual = objectMapper.readValue<FormTemplateRep.Complete>(response.content!!)
            assertEquals(formTemplateRep, actual)
        }

        // GetFormTemplate
        piperTest.test(
            endpointConfig = GetFormTemplate.endpointConfig
        ) {
            val actual = objectMapper.readValue<FormTemplateRep.Complete>(response.content!!)
            assertEquals(formTemplateRep, actual)
        }
    }
}
