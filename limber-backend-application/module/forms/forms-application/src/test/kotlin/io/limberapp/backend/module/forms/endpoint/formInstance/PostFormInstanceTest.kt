package io.limberapp.backend.module.forms.endpoint.formInstance

import io.limberapp.backend.module.forms.api.formInstance.FormInstanceApi
import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
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

        val featureId = UUID.randomUUID()
        val formTemplateId = UUID.randomUUID()

        FormInstanceRepFixtures.fixture.complete(this, featureId, formTemplateId, 4)
        piperTest.test(
            endpoint = FormInstanceApi.Post(FormInstanceRepFixtures.fixture.creation(featureId, formTemplateId)),
            expectedException = FormTemplateNotFound()
        )
    }

    @Test
    fun featureDoesNotMatchFormTemplate() {

        val feature0Id = UUID.randomUUID()
        val feature1Id = UUID.randomUUID()

        val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, feature0Id, 0)
        piperTest.setup(FormTemplateApi.Post(FormTemplateRepFixtures.exampleFormFixture.creation(feature0Id)))

        FormInstanceRepFixtures.fixture.complete(this, feature1Id, formTemplateRep.id, 4)
        piperTest.test(
            endpoint = FormInstanceApi.Post(FormInstanceRepFixtures.fixture.creation(feature1Id, formTemplateRep.id)),
            expectedException = FormTemplateCannotBeInstantiatedInAnotherFeature()
        )
    }

    @Test
    fun happyPath() {

        val featureId = UUID.randomUUID()

        val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, featureId, 0)
        piperTest.setup(FormTemplateApi.Post(FormTemplateRepFixtures.exampleFormFixture.creation(featureId)))

        val formInstanceRep = FormInstanceRepFixtures.fixture.complete(this, featureId, formTemplateRep.id, 4)
        piperTest.test(FormInstanceApi.Post(FormInstanceRepFixtures.fixture.creation(featureId, formTemplateRep.id))) {
            val actual = json.parse<FormInstanceRep.Complete>(response.content!!)
            assertEquals(formInstanceRep, actual)
        }

        piperTest.test(FormInstanceApi.Get(formInstanceRep.id)) {
            val actual = json.parse<FormInstanceRep.Complete>(response.content!!)
            assertEquals(formInstanceRep, actual)
        }
    }
}
