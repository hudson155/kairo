package io.limberapp.backend.module.forms.endpoint.formInstance

import io.limberapp.backend.module.forms.api.formInstance.FormInstanceApi
import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.backend.module.forms.exception.formInstance.FormInstanceNotFound
import io.limberapp.backend.module.forms.testing.ResourceTest
import io.limberapp.backend.module.forms.testing.fixtures.formInstance.FormInstanceRepFixtures
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID

internal class DeleteFormInstanceTest : ResourceTest() {

    @Test
    fun doesNotExist() {

        val formInstanceId = UUID.randomUUID()

        piperTest.test(
            endpoint = FormInstanceApi.Delete(formInstanceId),
            expectedException = FormInstanceNotFound()
        )
    }

    @Test
    fun happyPath() {

        val featureId = UUID.randomUUID()

        val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, featureId, 0)
        piperTest.setup(FormTemplateApi.Post(FormTemplateRepFixtures.exampleFormFixture.creation(featureId)))

        val formInstanceRep = FormInstanceRepFixtures.fixture.complete(this, featureId, formTemplateRep.id, 4)
        piperTest.setup(FormInstanceApi.Post(FormInstanceRepFixtures.fixture.creation(featureId, formTemplateRep.id)))

        piperTest.test(FormInstanceApi.Delete(formInstanceRep.id)) {}

        piperTest.test(
            endpoint = FormInstanceApi.Get(formTemplateRep.id),
            expectedException = FormInstanceNotFound()
        )
    }
}
