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

        val featureId = UUID.randomUUID()

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

        val featureId = UUID.randomUUID()

        val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, featureId, 0)
        piperTest.setup(
            endpointConfig = PostFormTemplate.endpointConfig,
            body = FormTemplateRepFixtures.exampleFormFixture.creation(featureId)
        )

        val formInstance0Rep = FormInstanceRepFixtures.fixture.complete(this, featureId, formTemplateRep.id, 4)
        piperTest.setup(
            endpointConfig = PostFormInstance.endpointConfig,
            body = FormInstanceRepFixtures.fixture.creation(featureId, formTemplateRep.id)
        )

        val formInstance1Rep = FormInstanceRepFixtures.fixture.complete(this, featureId, formTemplateRep.id, 5)
        piperTest.setup(
            endpointConfig = PostFormInstance.endpointConfig,
            body = FormInstanceRepFixtures.fixture.creation(featureId, formTemplateRep.id)
        )

        piperTest.test(
            endpointConfig = GetFormInstancesByFeatureId.endpointConfig,
            queryParams = mapOf(GetFormInstancesByFeatureId.featureId to featureId)
        ) {
            val actual = json.parseList<FormInstanceRep.Complete>(response.content!!).toSet()
            assertEquals(setOf(formInstance0Rep, formInstance1Rep), actual)
        }
    }
}
