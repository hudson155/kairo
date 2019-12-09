package io.limberapp.backend.module.forms.endpoint.formTemplate.part

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.module.forms.endpoint.formTemplate.CreateFormTemplate
import io.limberapp.backend.module.forms.endpoint.formTemplate.GetFormTemplate
import io.limberapp.backend.module.forms.exception.notFound.FormTemplateNotFound
import io.limberapp.backend.module.forms.exception.notFound.FormTemplatePartNotFound
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplatePartRep
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.backend.module.forms.testing.ResourceTest
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplatePartRepFixtures
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import org.junit.Ignore
import org.junit.Test
import java.util.UUID
import kotlin.test.assertEquals

@Ignore
internal class UpdateFormTemplatePartTest : ResourceTest() {

    @Test
    fun formTemplateDoesNotExist() {

        // Setup
        val formTemplateId = UUID.randomUUID()
        val partId = UUID.randomUUID()

        // UpdateFormTemplatePart
        val formTemplatePartUpdateRep = FormTemplatePartRep.Update("Crazy Form")
        piperTest.test(
            endpointConfig = UpdateFormTemplatePart.endpointConfig,
            pathParams = mapOf(
                UpdateFormTemplatePart.formTemplateId to formTemplateId,
                UpdateFormTemplatePart.partId to partId
            ),
            body = formTemplatePartUpdateRep,
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

        // UpdateFormTemplatePart
        val formTemplatePartUpdateRep = FormTemplatePartRep.Update("Crazy Form")
        piperTest.test(
            endpointConfig = UpdateFormTemplatePart.endpointConfig,
            pathParams = mapOf(
                UpdateFormTemplatePart.formTemplateId to formTemplateRep.id,
                UpdateFormTemplatePart.partId to partId
            ),
            body = formTemplatePartUpdateRep,
            expectedException = FormTemplatePartNotFound()
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
        var formTemplatePartRep = FormTemplatePartRepFixtures[0].complete(this, 6)
        formTemplateRep = formTemplateRep.copy(parts = formTemplateRep.parts.plus(formTemplatePartRep))
        piperTest.setup(
            endpointConfig = CreateFormTemplatePart.endpointConfig,
            pathParams = mapOf(CreateFormTemplatePart.formTemplateId to formTemplateRep.id),
            body = FormTemplatePartRepFixtures[0].creation()
        )

        // UpdateFormTemplatePart
        val formTemplatePartUpdateRep = FormTemplatePartRep.Update("Part 1 (updated)")
        formTemplatePartRep = formTemplatePartRep.copy(title = formTemplatePartUpdateRep.title)
        formTemplateRep = formTemplateRep.copy(
            parts = formTemplateRep.parts.map { if (it.id == formTemplatePartRep.id) formTemplatePartRep else it }
        )
        piperTest.test(
            endpointConfig = UpdateFormTemplatePart.endpointConfig,
            pathParams = mapOf(
                UpdateFormTemplatePart.formTemplateId to formTemplateRep.id,
                UpdateFormTemplatePart.partId to formTemplatePartRep.id
            ),
            body = formTemplatePartUpdateRep
        ) {
            val actual = objectMapper.readValue<FormTemplatePartRep.Complete>(response.content!!)
            assertEquals(formTemplatePartRep, actual)
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
