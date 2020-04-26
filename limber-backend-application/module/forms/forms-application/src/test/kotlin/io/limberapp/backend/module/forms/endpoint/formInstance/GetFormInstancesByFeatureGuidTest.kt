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

internal class GetFormInstancesByFeatureGuidTest : ResourceTest() {
    @Test
    fun happyPathNoFormInstances() {
        val featureGuid = UUID.randomUUID()

        piperTest.test(FormInstanceApi.GetByFeatureGuid(featureGuid)) {
            val actual = json.parseSet<FormInstanceRep.Complete>(response.content!!)
            assertTrue(actual.isEmpty())
        }
    }

    @Test
    fun happyPathMultipleFormInstances() {
        val featureGuid = UUID.randomUUID()

        val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, featureGuid, 0)
        piperTest.setup(FormTemplateApi.Post(FormTemplateRepFixtures.exampleFormFixture.creation(featureGuid)))

        val formInstance0Rep = FormInstanceRepFixtures.fixture.complete(this, featureGuid, formTemplateRep.guid, 5)
        piperTest.setup(
            endpoint = FormInstanceApi.Post(FormInstanceRepFixtures.fixture.creation(featureGuid, formTemplateRep.guid))
        )

        val formInstance1Rep = FormInstanceRepFixtures.fixture.complete(this, featureGuid, formTemplateRep.guid, 6)
        piperTest.setup(
            endpoint = FormInstanceApi.Post(FormInstanceRepFixtures.fixture.creation(featureGuid, formTemplateRep.guid))
        )

        piperTest.test(FormInstanceApi.GetByFeatureGuid(featureGuid)) {
            val actual = json.parseSet<FormInstanceRep.Complete>(response.content!!)
            assertEquals(setOf(formInstance0Rep, formInstance1Rep), actual)
        }
    }
}
