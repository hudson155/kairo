package io.limberapp.backend.module.forms.endpoint.formInstance

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.module.forms.endpoint.formTemplate.CreateFormTemplate
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
        val orgId = UUID.randomUUID()

        // CreateFormTemplate
        val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, orgId, 0)
        piperTest.setup(
            endpointConfig = CreateFormTemplate.endpointConfig,
            body = FormTemplateRepFixtures.exampleFormFixture.creation(orgId)
        )

        // CreateFormInstance
        val formInstanceRep = FormInstanceRepFixtures.fixture.complete(this, orgId, formTemplateRep.id, 4)
        piperTest.setup(
            endpointConfig = CreateFormInstance.endpointConfig,
            body = FormInstanceRepFixtures.fixture.creation(orgId, formTemplateRep.id)
        )

        // GetFormInstance
        piperTest.test(
            endpointConfig = GetFormInstance.endpointConfig,
            pathParams = mapOf(GetFormInstance.formInstanceId to formInstanceRep.id)
        ) {
            val actual = objectMapper.readValue<FormInstanceRep.Complete>(response.content!!)
            assertEquals(formInstanceRep, actual)
        }
    }
}
