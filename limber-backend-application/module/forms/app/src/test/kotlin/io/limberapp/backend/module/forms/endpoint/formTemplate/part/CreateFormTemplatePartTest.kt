package io.limberapp.backend.module.forms.endpoint.formTemplate.part

import com.fasterxml.jackson.module.kotlin.readValue
import com.piperframework.exception.exception.badRequest.IndexOutOfBounds
import io.limberapp.backend.module.forms.endpoint.formTemplate.CreateFormTemplate
import io.limberapp.backend.module.forms.endpoint.formTemplate.GetFormTemplate
import io.limberapp.backend.module.forms.exception.notFound.FormTemplateNotFound
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
internal class CreateFormTemplatePartTest : ResourceTest() {

    @Test
    fun formTemplateDoesNotExist() {

        // Setup
        val formTemplateId = UUID.randomUUID()

        // CreateFormTemplatePart
        piperTest.test(
            endpointConfig = CreateFormTemplatePart.endpointConfig,
            pathParams = mapOf(CreateFormTemplatePart.formTemplateId to formTemplateId),
            body = FormTemplatePartRepFixtures[0].creation(),
            expectedException = FormTemplateNotFound()
        )
    }

    @Test
    fun indexOutOfBoundsLow() {

        // Setup
        val orgId = UUID.randomUUID()

        // CreateFormTemplate
        val formTemplateRep = FormTemplateRepFixtures[0].complete(this, orgId, 0)
        piperTest.setup(
            endpointConfig = CreateFormTemplate.endpointConfig,
            body = FormTemplateRepFixtures[0].creation(orgId)
        )

        // CreateFormTemplatePart
        piperTest.test(
            endpointConfig = CreateFormTemplatePart.endpointConfig,
            pathParams = mapOf(CreateFormTemplatePart.formTemplateId to formTemplateRep.id),
            queryParams = mapOf(CreateFormTemplatePart.index to -1),
            body = FormTemplatePartRepFixtures[0].creation(),
            expectedException = IndexOutOfBounds()
        )
    }

    @Test
    fun indexOutOfBoundsHigh() {

        // Setup
        val orgId = UUID.randomUUID()

        // CreateFormTemplate
        val formTemplateRep = FormTemplateRepFixtures[0].complete(this, orgId, 0)
        piperTest.setup(
            endpointConfig = CreateFormTemplate.endpointConfig,
            body = FormTemplateRepFixtures[0].creation(orgId)
        )

        // CreateFormTemplatePart
        piperTest.test(
            endpointConfig = CreateFormTemplatePart.endpointConfig,
            pathParams = mapOf(CreateFormTemplatePart.formTemplateId to formTemplateRep.id),
            queryParams = mapOf(CreateFormTemplatePart.index to 2),
            body = FormTemplatePartRepFixtures[0].creation(),
            expectedException = IndexOutOfBounds()
        )
    }

    @Test
    fun happyPathFirstIndex() {

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
        formTemplateRep = formTemplateRep.copy(parts = listOf(formTemplatePartRep).plus(formTemplateRep.parts))
        piperTest.test(
            endpointConfig = CreateFormTemplatePart.endpointConfig,
            pathParams = mapOf(CreateFormTemplatePart.formTemplateId to formTemplateRep.id),
            queryParams = mapOf(CreateFormTemplatePart.index to 0),
            body = FormTemplatePartRepFixtures[0].creation()
        ) {
            val actual = objectMapper.readValue<FormTemplatePartRep.Complete>(response.content!!)
            assertEquals(formTemplatePartRep, actual)
        }

        // GetFormTemplate
        piperTest.test(
            endpointConfig = GetFormTemplate.endpointConfig
        ) {
            val actual = objectMapper.readValue<FormTemplateRep.Complete>(response.content!!)
            assertEquals(formTemplateRep, actual)
        }
    }

    @Test
    fun happyPathLastIndex() {

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
        piperTest.test(
            endpointConfig = CreateFormTemplatePart.endpointConfig,
            pathParams = mapOf(CreateFormTemplatePart.formTemplateId to formTemplateRep.id),
            body = FormTemplatePartRepFixtures[0].creation()
        ) {
            val actual = objectMapper.readValue<FormTemplatePartRep.Complete>(response.content!!)
            assertEquals(formTemplatePartRep, actual)
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
