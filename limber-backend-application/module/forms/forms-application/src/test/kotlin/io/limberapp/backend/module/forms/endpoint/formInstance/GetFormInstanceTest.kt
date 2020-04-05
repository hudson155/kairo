package io.limberapp.backend.module.forms.endpoint.formInstance

import io.limberapp.backend.module.forms.endpoint.formTemplate.PostFormTemplate
import io.limberapp.backend.module.forms.exception.formInstance.FormInstanceNotFound
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import io.limberapp.backend.module.forms.testing.ResourceTest
import io.limberapp.backend.module.forms.testing.fixtures.formInstance.FormInstanceRepFixtures
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class GetFormInstanceTest : ResourceTest() {

    @Test
    fun doesNotExist() {

        // Setup
        val formInstanceId = UUID.randomUUID()

        // GetFormInstance
        piperTest.test(
            endpointConfig = GetFormInstance.endpointConfig,
            pathParams = mapOf(GetFormInstance.formInstanceId to formInstanceId),
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

        // GetFormInstance
        piperTest.test(
            endpointConfig = GetFormInstance.endpointConfig,
            pathParams = mapOf(GetFormInstance.formInstanceId to formInstanceRep.id)
        ) {
            val actual = json.parse<FormInstanceRep.Complete>(response.content!!)
            assertEquals(formInstanceRep, actual)
        }
    }
}
