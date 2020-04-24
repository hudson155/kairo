package io.limberapp.backend.module.forms.endpoint.formInstance

import io.limberapp.backend.module.forms.api.formInstance.FormInstanceApi
import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
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

        piperTest.test(FormInstanceApi.GetByFeatureId(featureId)) {
            val actual = json.parseList<FormInstanceRep.Complete>(response.content!!).toSet()
            assertTrue(actual.isEmpty())
        }
    }

    @Test
    fun happyPathMultipleFormInstances() {
        val featureId = UUID.randomUUID()

        val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, featureId, 0)
        piperTest.setup(FormTemplateApi.Post(FormTemplateRepFixtures.exampleFormFixture.creation(featureId)))

        val formInstance0Rep = FormInstanceRepFixtures.fixture.complete(this, featureId, formTemplateRep.id, 5)
        piperTest.setup(FormInstanceApi.Post(FormInstanceRepFixtures.fixture.creation(featureId, formTemplateRep.id)))

        val formInstance1Rep = FormInstanceRepFixtures.fixture.complete(this, featureId, formTemplateRep.id, 6)
        piperTest.setup(FormInstanceApi.Post(FormInstanceRepFixtures.fixture.creation(featureId, formTemplateRep.id)))

        piperTest.test(FormInstanceApi.GetByFeatureId(featureId)) {
            val actual = json.parseList<FormInstanceRep.Complete>(response.content!!).toSet()
            assertEquals(setOf(formInstance0Rep, formInstance1Rep), actual)
        }
    }
}
