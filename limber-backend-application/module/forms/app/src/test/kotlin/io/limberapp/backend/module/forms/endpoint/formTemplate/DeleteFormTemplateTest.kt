package io.limberapp.backend.module.forms.endpoint.formTemplate

import io.limberapp.backend.module.forms.exception.notFound.FormTemplateNotFound
import io.limberapp.backend.module.forms.testing.ResourceTest
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import org.junit.Ignore
import org.junit.Test
import java.util.UUID

@Ignore
internal class DeleteFormTemplateTest : ResourceTest() {

    @Test
    fun doesNotExist() {

        // Setup
        val formTemplateId = UUID.randomUUID()

        // DeleteFormTemplate
        piperTest.test(
            endpointConfig = DeleteFormTemplate.endpointConfig,
            pathParams = mapOf(DeleteFormTemplate.formTemplateId to formTemplateId),
            expectedException = FormTemplateNotFound()
        )
    }

    @Test
    fun happyPath() {

        // Setup
        val orgId = UUID.randomUUID()

        // CreateFormTemplate
        val formTemplateRep = FormTemplateRepFixtures[0].complete(this, orgId, 0)
        piperTest.setup(
            endpointConfig = CreateFormTemplate.endpointConfig,
            body = FormTemplateRepFixtures[0].creation(orgId)
        )

        // DeleteFormTemplate
        piperTest.test(
            endpointConfig = DeleteFormTemplate.endpointConfig,
            pathParams = mapOf(DeleteFormTemplate.formTemplateId to formTemplateRep.id)
        ) {}

        // GetFormTemplate
        piperTest.test(
            endpointConfig = GetFormTemplate.endpointConfig,
            pathParams = mapOf(GetFormTemplate.formTemplateId to formTemplateRep.id),
            expectedException = FormTemplateNotFound()
        )
    }
}
