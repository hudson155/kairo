package io.limberapp.backend.module.forms.endpoint.formInstance

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.module.forms.endpoint.formTemplate.CreateFormTemplate
import io.limberapp.backend.module.forms.exception.formInstance.FormTemplateCannotBeInstantiatedInAnotherOrg
import io.limberapp.backend.module.forms.exception.formTemplate.FormTemplateNotFound
import io.limberapp.backend.module.forms.exception.formTemplate.FormTemplateQuestionNotFound
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import io.limberapp.backend.module.forms.testing.ResourceTest
import io.limberapp.backend.module.forms.testing.fixtures.formInstance.FormInstanceRepFixtures
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class CreateFormInstanceTest : ResourceTest() {

    @Test
    fun formTemplateDoesNotExist() {

        // Setup
        val orgId = UUID.randomUUID()
        val formTemplateId = UUID.randomUUID()

        // CreateFormInstance
        FormInstanceRepFixtures.fixture.complete(this, orgId, formTemplateId, 4)
        piperTest.test(
            endpointConfig = CreateFormInstance.endpointConfig,
            body = FormInstanceRepFixtures.fixture.creation(orgId, formTemplateId),
            expectedException = FormTemplateNotFound()
        )
    }

    @Test
    fun orgDoesNotMatchFormTemplate() {

        // Setup
        val org0Id = UUID.randomUUID()
        val org1Id = UUID.randomUUID()

        // CreateFormTemplate
        val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, org0Id, 0)
        piperTest.setup(
            endpointConfig = CreateFormTemplate.endpointConfig,
            body = FormTemplateRepFixtures.exampleFormFixture.creation(org0Id)
        )

        // CreateFormInstance
        FormInstanceRepFixtures.fixture.complete(this, org1Id, formTemplateRep.id, 4)
        piperTest.test(
            endpointConfig = CreateFormInstance.endpointConfig,
            body = FormInstanceRepFixtures.fixture.creation(org1Id, formTemplateRep.id),
            expectedException = FormTemplateCannotBeInstantiatedInAnotherOrg()
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
        piperTest.test(
            endpointConfig = CreateFormInstance.endpointConfig,
            body = FormInstanceRepFixtures.fixture.creation(orgId, formTemplateRep.id)
        ) {
            val actual = objectMapper.readValue<FormInstanceRep.Complete>(response.content!!)
            assertEquals(formInstanceRep, actual)
        }

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
