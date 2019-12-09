package io.limberapp.backend.module.forms.endpoint.formTemplate.part

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.module.forms.endpoint.formTemplate.CreateFormTemplate
import io.limberapp.backend.module.forms.endpoint.formTemplate.GetFormTemplate
import io.limberapp.backend.module.forms.exception.notFound.FormTemplateNotFound
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.backend.module.forms.testing.ResourceTest
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplatePartRepFixtures
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import org.junit.Ignore
import org.junit.Test
import java.util.UUID
import kotlin.test.assertEquals

@Ignore
internal class DeleteFormTemplatePartTest : ResourceTest() {

    @Test
    fun formTemplateDoesNotExist() {

        // Setup
        val formTemplateId = UUID.randomUUID()
        val partId = UUID.randomUUID()

        // DeleteFormTemplate
        piperTest.test(
            endpointConfig = DeleteFormTemplatePart.endpointConfig,
            pathParams = mapOf(
                DeleteFormTemplatePart.formTemplateId to formTemplateId,
                DeleteFormTemplatePart.partId to partId
            ),
            expectedException = FormTemplateNotFound()
        )
    }

    @Test
    fun formTemplatePartDoesNotExist() {

        // Setup
        val orgId = UUID.randomUUID()
        val partId = UUID.randomUUID()

        // CreateFormTemplate
        val formTemplateRep = FormTemplateRepFixtures[0].complete(this, orgId, 0)
        piperTest.setup(
            endpointConfig = CreateFormTemplate.endpointConfig,
            body = FormTemplateRepFixtures[0].creation(orgId)
        )

        // DeleteFormTemplate
        piperTest.test(
            endpointConfig = DeleteFormTemplatePart.endpointConfig,
            pathParams = mapOf(
                DeleteFormTemplatePart.formTemplateId to formTemplateRep.id,
                DeleteFormTemplatePart.partId to partId
            ),
            expectedException = FormTemplateNotFound()
        )
    }

    @Test
    fun happyPath() {

        // Setup
        val orgId = UUID.randomUUID()

        // CreateFormTemplate
        var formTemplateRep = FormTemplateRepFixtures[0].complete(this, orgId, 0)
        piperTest.setup(
            endpointConfig = CreateFormTemplate.endpointConfig,
            body = FormTemplateRepFixtures[0].creation(orgId)
        )

        // CreateFormTemplatePart
        val formTemplatePartRep = FormTemplatePartRepFixtures[0].complete(this, 6)
        formTemplateRep = formTemplateRep.copy(parts = formTemplateRep.parts.plus(formTemplatePartRep))
        piperTest.setup(
            endpointConfig = CreateFormTemplatePart.endpointConfig,
            pathParams = mapOf(CreateFormTemplatePart.formTemplateId to formTemplateRep.id),
            body = FormTemplatePartRepFixtures[0].creation()
        )

        // DeleteFormTemplatePart
        formTemplateRep = formTemplateRep.copy(parts = formTemplateRep.parts.filter { it.id != formTemplatePartRep.id })
        piperTest.test(
            endpointConfig = DeleteFormTemplatePart.endpointConfig,
            pathParams = mapOf(
                DeleteFormTemplatePart.formTemplateId to formTemplateRep.id,
                DeleteFormTemplatePart.partId to formTemplatePartRep.id
            )
        ) {}

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
