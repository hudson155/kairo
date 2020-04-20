package io.limberapp.backend.module.forms.endpoint.formTemplate

import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.backend.module.forms.exception.formTemplate.FormTemplateNotFound
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.backend.module.forms.testing.ResourceTest
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class PatchFormTemplateTest : ResourceTest() {

    @Test
    fun doesNotExist() {

        val formTemplateId = UUID.randomUUID()

        val formTemplateUpdateRep = FormTemplateRep.Update("Crazy Form")
        piperTest.test(
            endpoint = FormTemplateApi.Patch(formTemplateId, formTemplateUpdateRep),
            expectedException = FormTemplateNotFound()
        )
    }

    @Test
    fun happyPath() {

        val featureId = UUID.randomUUID()

        var formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, featureId, 0)
        piperTest.setup(FormTemplateApi.Post(FormTemplateRepFixtures.exampleFormFixture.creation(featureId)))

        val formTemplateUpdateRep = FormTemplateRep.Update("Crazy Form")
        formTemplateRep = formTemplateRep.copy(title = formTemplateUpdateRep.title!!)
        piperTest.test(FormTemplateApi.Patch(formTemplateRep.id, formTemplateUpdateRep)) {
            val actual = json.parse<FormTemplateRep.Complete>(response.content!!)
            assertEquals(formTemplateRep, actual)
        }

        piperTest.test(FormTemplateApi.Get(formTemplateRep.id)) {
            val actual = json.parse<FormTemplateRep.Complete>(response.content!!)
            assertEquals(formTemplateRep, actual)
        }
    }
}
