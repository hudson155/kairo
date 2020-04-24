package io.limberapp.backend.module.forms.endpoint.formTemplate

import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.backend.module.forms.exception.formTemplate.FormTemplateNotFound
import io.limberapp.backend.module.forms.testing.ResourceTest
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID

internal class DeleteFormTemplateTest : ResourceTest() {
    @Test
    fun doesNotExist() {
        val formTemplateId = UUID.randomUUID()

        piperTest.test(
            endpoint = FormTemplateApi.Delete(formTemplateId),
            expectedException = FormTemplateNotFound()
        )
    }

    @Test
    fun happyPath() {
        val featureId = UUID.randomUUID()

        val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, featureId, 0)
        piperTest.setup(FormTemplateApi.Post(FormTemplateRepFixtures.exampleFormFixture.creation(featureId)))

        piperTest.test(FormTemplateApi.Delete(formTemplateRep.id)) {}

        piperTest.test(
            endpoint = FormTemplateApi.Get(formTemplateRep.id),
            expectedException = FormTemplateNotFound()
        )
    }
}
