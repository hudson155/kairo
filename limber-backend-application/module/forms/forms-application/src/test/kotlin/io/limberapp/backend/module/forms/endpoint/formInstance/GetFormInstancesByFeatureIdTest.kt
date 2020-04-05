package io.limberapp.backend.module.forms.endpoint.formInstance

import io.limberapp.backend.module.forms.endpoint.formTemplate.PostFormTemplate
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import io.limberapp.backend.module.forms.testing.ResourceTest
import io.limberapp.backend.module.forms.testing.fixtures.formInstance.FormInstanceRepFixtures
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class GetFormInstancesByFeatureIdTest : ResourceTest() {

    @Test
    fun happyPathNoFormInstances() {

        // Setup
        val featureId = UUID.randomUUID()

        // GetFormInstancesByFeatureId
        piperTest.test(
            endpointConfig = GetFormInstancesByFeatureId.endpointConfig,
            queryParams = mapOf(GetFormInstancesByFeatureId.featureId to featureId)
        ) {
            val actual = json.parseList<FormInstanceRep.Complete>(response.content!!).toSet()
            assertTrue(actual.isEmpty())
        }
    }

    @Test
    fun happyPathMultipleFormInstances() {

        // Setup
        val featureId = UUID.randomUUID()

        // PostFormTemplate
        val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, featureId, 0)
        piperTest.setup(
            endpointConfig = PostFormTemplate.endpointConfig,
            body = FormTemplateRepFixtures.exampleFormFixture.creation(featureId)
        )

        // PostFormInstance
        val formInstance0Rep = FormInstanceRepFixtures.fixture.complete(this, featureId, formTemplateRep.id, 6)
        piperTest.setup(
            endpointConfig = PostFormInstance.endpointConfig,
            body = FormInstanceRepFixtures.fixture.creation(featureId, formTemplateRep.id)
        )

        // PostFormInstance
        val formInstance1Rep = FormInstanceRepFixtures.fixture.complete(this, featureId, formTemplateRep.id, 7)
        piperTest.setup(
            endpointConfig = PostFormInstance.endpointConfig,
            body = FormInstanceRepFixtures.fixture.creation(featureId, formTemplateRep.id)
        )

        // GetFormInstancesByFeatureId
        piperTest.test(
            endpointConfig = GetFormInstancesByFeatureId.endpointConfig,
            queryParams = mapOf(GetFormInstancesByFeatureId.featureId to featureId)
        ) {
            val actual = json.parseList<FormInstanceRep.Complete>(response.content!!).toSet()
            assertEquals(setOf(formInstance0Rep, formInstance1Rep), actual)
        }
    }
}
