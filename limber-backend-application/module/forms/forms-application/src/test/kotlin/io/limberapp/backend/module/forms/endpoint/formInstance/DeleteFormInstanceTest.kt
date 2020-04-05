package io.limberapp.backend.module.forms.endpoint.formInstance

import io.limberapp.backend.module.forms.endpoint.formTemplate.PostFormTemplate
import io.limberapp.backend.module.forms.exception.formInstance.FormInstanceNotFound
import io.limberapp.backend.module.forms.testing.ResourceTest
import io.limberapp.backend.module.forms.testing.fixtures.formInstance.FormInstanceRepFixtures
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID

internal class DeleteFormInstanceTest : ResourceTest() {

    @Test
    fun doesNotExist() {

        // Setup
        val formInstanceId = UUID.randomUUID()

        // DeleteFormInstance
        piperTest.test(
            endpointConfig = DeleteFormInstance.endpointConfig,
            pathParams = mapOf(DeleteFormInstance.formInstanceId to formInstanceId),
            expectedException = FormInstanceNotFound()
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

        // PostFormInstance
        val formInstanceRep = FormInstanceRepFixtures.fixture.complete(this, featureId, formTemplateRep.id, 6)
        piperTest.setup(
            endpointConfig = PostFormInstance.endpointConfig,
            body = FormInstanceRepFixtures.fixture.creation(featureId, formTemplateRep.id)
        )

        // DeleteFormInstance
        piperTest.test(
            endpointConfig = DeleteFormInstance.endpointConfig,
            pathParams = mapOf(DeleteFormInstance.formInstanceId to formInstanceRep.id)
        ) {}

        // GetFormInstance
        piperTest.test(
            endpointConfig = GetFormInstance.endpointConfig,
            pathParams = mapOf(GetFormInstance.formInstanceId to formTemplateRep.id),
            expectedException = FormInstanceNotFound()
        )
    }
}
