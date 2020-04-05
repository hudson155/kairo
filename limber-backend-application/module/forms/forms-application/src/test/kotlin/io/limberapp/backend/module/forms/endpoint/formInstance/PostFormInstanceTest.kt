package io.limberapp.backend.module.forms.endpoint.formInstance

import io.limberapp.backend.module.forms.endpoint.formTemplate.PostFormTemplate
import io.limberapp.backend.module.forms.exception.formInstance.FormTemplateCannotBeInstantiatedInAnotherFeature
import io.limberapp.backend.module.forms.exception.formTemplate.FormTemplateNotFound
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import io.limberapp.backend.module.forms.testing.ResourceTest
import io.limberapp.backend.module.forms.testing.fixtures.formInstance.FormInstanceRepFixtures
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class PostFormInstanceTest : ResourceTest() {

    @Test
    fun formTemplateDoesNotExist() {

        // Setup
        val featureId = UUID.randomUUID()
        val formTemplateId = UUID.randomUUID()

        // PostFormInstance
        FormInstanceRepFixtures.fixture.complete(this, featureId, formTemplateId, 6)
        piperTest.test(
            endpointConfig = PostFormInstance.endpointConfig,
            body = FormInstanceRepFixtures.fixture.creation(featureId, formTemplateId),
            expectedException = FormTemplateNotFound()
        )
    }

    @Test
    fun featureDoesNotMatchFormTemplate() {

        // Setup
        val feature0Id = UUID.randomUUID()
        val feature1Id = UUID.randomUUID()

        // PostFormTemplate
        val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, feature0Id, 0)
        piperTest.setup(
            endpointConfig = PostFormTemplate.endpointConfig,
            body = FormTemplateRepFixtures.exampleFormFixture.creation(feature0Id)
        )

        // PostFormInstance
        FormInstanceRepFixtures.fixture.complete(this, feature1Id, formTemplateRep.id, 6)
        piperTest.test(
            endpointConfig = PostFormInstance.endpointConfig,
            body = FormInstanceRepFixtures.fixture.creation(feature1Id, formTemplateRep.id),
            expectedException = FormTemplateCannotBeInstantiatedInAnotherFeature()
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
        piperTest.test(
            endpointConfig = PostFormInstance.endpointConfig,
            body = FormInstanceRepFixtures.fixture.creation(featureId, formTemplateRep.id)
        ) {
            val actual = json.parse<FormInstanceRep.Complete>(response.content!!)
            assertEquals(formInstanceRep, actual)
        }

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
