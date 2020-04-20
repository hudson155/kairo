package io.limberapp.backend.module.forms.endpoint.formTemplate.question

import io.limberapp.backend.module.forms.endpoint.formTemplate.GetFormTemplate
import io.limberapp.backend.module.forms.endpoint.formTemplate.PostFormTemplate
import io.limberapp.backend.module.forms.exception.formTemplate.FormTemplateNotFound
import io.limberapp.backend.module.forms.exception.formTemplate.FormTemplateQuestionNotFound
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion.FormTemplateTextQuestionRep
import io.limberapp.backend.module.forms.testing.ResourceTest
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateQuestionRepFixtures
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class PatchFormTemplateQuestionTest : ResourceTest() {

    @Test
    fun formTemplateDoesNotExist() {

        val formTemplateId = UUID.randomUUID()
        val questionId = UUID.randomUUID()

        val formTemplateQuestionUpdateRep = FormTemplateTextQuestionRep.Update("Renamed Question")
        piperTest.test(
            endpointConfig = PatchFormTemplateQuestion.endpointConfig,
            pathParams = mapOf(
                PatchFormTemplateQuestion.formTemplateId to formTemplateId,
                PatchFormTemplateQuestion.questionId to questionId
            ),
            body = formTemplateQuestionUpdateRep,
            expectedException = FormTemplateNotFound()
        )
    }

    @Test
    fun formTemplateQuestionDoesNotExist() {

        val featureId = UUID.randomUUID()
        val questionId = UUID.randomUUID()

        val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, featureId, 0)
        piperTest.setup(
            endpointConfig = PostFormTemplate.endpointConfig,
            body = FormTemplateRepFixtures.exampleFormFixture.creation(featureId)
        )

        val formTemplateQuestionUpdateRep = FormTemplateTextQuestionRep.Update("Renamed Question")
        piperTest.test(
            endpointConfig = PatchFormTemplateQuestion.endpointConfig,
            pathParams = mapOf(
                PatchFormTemplateQuestion.formTemplateId to formTemplateRep.id,
                PatchFormTemplateQuestion.questionId to questionId
            ),
            body = formTemplateQuestionUpdateRep,
            expectedException = FormTemplateQuestionNotFound()
        )
    }

    @Test
    fun happyPath() {

        val featureId = UUID.randomUUID()

        var formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, featureId, 0)
        piperTest.setup(
            endpointConfig = PostFormTemplate.endpointConfig,
            body = FormTemplateRepFixtures.exampleFormFixture.creation(featureId)
        )

        var formTemplateQuestionRep = FormTemplateQuestionRepFixtures.textFixture.complete(this, 5)
                as FormTemplateTextQuestionRep.Complete
        formTemplateRep = formTemplateRep.copy(
            questions = listOf(formTemplateQuestionRep).plus(formTemplateRep.questions)
        )
        piperTest.setup(
            endpointConfig = PostFormTemplateQuestion.endpointConfig,
            pathParams = mapOf(PostFormTemplateQuestion.formTemplateId to formTemplateRep.id),
            queryParams = mapOf(PostFormTemplateQuestion.rank to 0),
            body = FormTemplateQuestionRepFixtures.textFixture.creation()
        )

        val formTemplateQuestionUpdateRep = FormTemplateTextQuestionRep.Update("Renamed Question")
        formTemplateQuestionRep = formTemplateQuestionRep.copy(label = formTemplateQuestionUpdateRep.label!!)
        formTemplateRep = formTemplateRep.copy(
            questions = formTemplateRep.questions.map {
                if (it.id == formTemplateQuestionRep.id) formTemplateQuestionRep else it
            }
        )
        piperTest.test(
            endpointConfig = PatchFormTemplateQuestion.endpointConfig,
            pathParams = mapOf(
                PatchFormTemplateQuestion.formTemplateId to formTemplateRep.id,
                PatchFormTemplateQuestion.questionId to formTemplateQuestionRep.id
            ),
            body = formTemplateQuestionUpdateRep
        ) {}

        piperTest.test(
            endpointConfig = GetFormTemplate.endpointConfig,
            pathParams = mapOf(GetFormTemplate.formTemplateId to formTemplateRep.id)
        ) {
            val actual = json.parse<FormTemplateRep.Complete>(response.content!!)
            assertEquals(formTemplateRep, actual)
        }
    }
}
