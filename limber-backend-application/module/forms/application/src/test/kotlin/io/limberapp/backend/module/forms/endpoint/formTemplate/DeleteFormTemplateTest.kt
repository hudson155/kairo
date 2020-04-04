package io.limberapp.backend.module.forms.endpoint.formTemplate

import io.limberapp.backend.module.forms.exception.formTemplate.FormTemplateNotFound
import io.limberapp.backend.module.forms.testing.ResourceTest
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID

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
        val featureId = UUID.randomUUID()

        // PostFormTemplate
        val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, featureId, 0)
        piperTest.setup(
            endpointConfig = PostFormTemplate.endpointConfig,
            body = FormTemplateRepFixtures.exampleFormFixture.creation(featureId)
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
