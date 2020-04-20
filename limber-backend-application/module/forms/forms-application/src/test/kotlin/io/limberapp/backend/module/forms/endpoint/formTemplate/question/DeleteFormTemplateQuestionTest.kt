package io.limberapp.backend.module.forms.endpoint.formTemplate.question

import io.limberapp.backend.module.forms.endpoint.formTemplate.GetFormTemplate
import io.limberapp.backend.module.forms.endpoint.formTemplate.PostFormTemplate
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
            endpointConfig = DeleteFormTemplateQuestion.endpointConfig,
            pathParams = mapOf(
                DeleteFormTemplateQuestion.formTemplateId to formTemplateId,
                DeleteFormTemplateQuestion.questionId to questionId
            ),
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

        piperTest.test(
            endpointConfig = DeleteFormTemplateQuestion.endpointConfig,
            pathParams = mapOf(
                DeleteFormTemplateQuestion.formTemplateId to formTemplateRep.id,
                DeleteFormTemplateQuestion.questionId to questionId
            ),
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

        val formTemplateQuestionRep = FormTemplateQuestionRepFixtures.textFixture.complete(this, 4)
        formTemplateRep = formTemplateRep.copy(
            questions = listOf(formTemplateQuestionRep).plus(formTemplateRep.questions)
        )
        piperTest.setup(
            endpointConfig = PostFormTemplateQuestion.endpointConfig,
            pathParams = mapOf(PostFormTemplateQuestion.formTemplateId to formTemplateRep.id),
            queryParams = mapOf(PostFormTemplateQuestion.rank to 0),
            body = FormTemplateQuestionRepFixtures.textFixture.creation()
        )

        formTemplateRep = formTemplateRep.copy(
            questions = formTemplateRep.questions.filter { it.id != formTemplateQuestionRep.id }
        )
        piperTest.test(
            endpointConfig = DeleteFormTemplateQuestion.endpointConfig,
            pathParams = mapOf(
                DeleteFormTemplateQuestion.formTemplateId to formTemplateRep.id,
                DeleteFormTemplateQuestion.questionId to formTemplateQuestionRep.id
            )
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
