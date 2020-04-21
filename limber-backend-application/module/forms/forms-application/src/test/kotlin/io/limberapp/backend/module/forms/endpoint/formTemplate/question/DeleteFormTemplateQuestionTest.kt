package io.limberapp.backend.module.forms.endpoint.formTemplate.question

import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.backend.module.forms.api.formTemplate.question.FormTemplateQuestionApi
import io.limberapp.backend.module.forms.exception.formTemplate.FormTemplateNotFound
import io.limberapp.backend.module.forms.exception.formTemplate.FormTemplateQuestionNotFound
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.backend.module.forms.testing.ResourceTest
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateQuestionRepFixtures
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class DeleteFormTemplateQuestionTest : ResourceTest() {

    @Test
    fun formTemplateDoesNotExist() {

        val formTemplateId = UUID.randomUUID()
        val questionId = UUID.randomUUID()

        piperTest.test(
            endpoint = FormTemplateQuestionApi.Delete(formTemplateId, questionId),
            expectedException = FormTemplateNotFound()
        )
    }

    @Test
    fun formTemplateQuestionDoesNotExist() {

        val featureId = UUID.randomUUID()
        val questionId = UUID.randomUUID()

        val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, featureId, 0)
        piperTest.setup(FormTemplateApi.Post(FormTemplateRepFixtures.exampleFormFixture.creation(featureId)))

        piperTest.test(
            endpoint = FormTemplateQuestionApi.Delete(formTemplateRep.id, questionId),
            expectedException = FormTemplateQuestionNotFound()
        )
    }

    @Test
    fun happyPath() {

        val featureId = UUID.randomUUID()

        var formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, featureId, 0)
        piperTest.setup(FormTemplateApi.Post(FormTemplateRepFixtures.exampleFormFixture.creation(featureId)))

        val formTemplateQuestionRep = FormTemplateQuestionRepFixtures.textFixture.complete(this, 5)
        formTemplateRep = formTemplateRep.copy(
            questions = listOf(formTemplateQuestionRep).plus(formTemplateRep.questions)
        )
        piperTest.setup(
            endpoint = FormTemplateQuestionApi.Post(
                formTemplateId = formTemplateRep.id,
                rank = 0,
                rep = FormTemplateQuestionRepFixtures.textFixture.creation()
            )
        )

        formTemplateRep = formTemplateRep.copy(
            questions = formTemplateRep.questions.filter { it.id != formTemplateQuestionRep.id }
        )
        piperTest.test(FormTemplateQuestionApi.Delete(formTemplateRep.id, formTemplateQuestionRep.id)) {}

        piperTest.test(FormTemplateApi.Get(formTemplateRep.id)) {
            val actual = json.parse<FormTemplateRep.Complete>(response.content!!)
            assertEquals(formTemplateRep, actual)
        }
    }
}
