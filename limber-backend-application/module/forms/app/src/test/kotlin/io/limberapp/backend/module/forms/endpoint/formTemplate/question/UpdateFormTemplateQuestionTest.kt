package io.limberapp.backend.module.forms.endpoint.formTemplate.question

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.module.forms.endpoint.formTemplate.CreateFormTemplate
import io.limberapp.backend.module.forms.endpoint.formTemplate.GetFormTemplate
import io.limberapp.backend.module.forms.exception.notFound.FormTemplateNotFound
import io.limberapp.backend.module.forms.exception.notFound.FormTemplateQuestionNotFound
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion.FormTemplateTextQuestionRep
import io.limberapp.backend.module.forms.testing.ResourceTest
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateQuestionRepFixtures
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import org.junit.Ignore
import org.junit.Test
import java.util.UUID
import kotlin.test.assertEquals

@Ignore
internal class UpdateFormTemplateQuestionTest : ResourceTest() {

    @Test
    fun formTemplateDoesNotExist() {

        // Setup
        val formTemplateId = UUID.randomUUID()
        val questionId = UUID.randomUUID()

        // UpdateFormTemplate
        val formTemplateQuestionUpdateRep = FormTemplateTextQuestionRep.Update("Renamed Question")
        piperTest.test(
            endpointConfig = UpdateFormTemplateQuestion.endpointConfig,
            pathParams = mapOf(
                UpdateFormTemplateQuestion.formTemplateId to formTemplateId,
                UpdateFormTemplateQuestion.questionId to questionId
            ),
            body = formTemplateQuestionUpdateRep,
            expectedException = FormTemplateNotFound()
        )
    }

    @Test
    fun formTemplateQuestionDoesNotExist() {

        // Setup
        val orgId = UUID.randomUUID()
        val questionId = UUID.randomUUID()

        // CreateFormTemplate
        val formTemplateRep = FormTemplateRepFixtures[0].complete(this, orgId, 0)
        piperTest.setup(
            endpointConfig = CreateFormTemplate.endpointConfig,
            body = FormTemplateRepFixtures[0].creation(orgId)
        )

        // UpdateFormTemplateQuestion
        val formTemplateQuestionUpdateRep = FormTemplateTextQuestionRep.Update("Renamed Question")
        piperTest.test(
            endpointConfig = UpdateFormTemplateQuestion.endpointConfig,
            pathParams = mapOf(
                UpdateFormTemplateQuestion.formTemplateId to formTemplateRep.id,
                UpdateFormTemplateQuestion.questionId to questionId
            ),
            body = formTemplateQuestionUpdateRep,
            expectedException = FormTemplateQuestionNotFound()
        )
    }

    @Test
    fun happyPath() {

        // Setup
        val orgId = UUID.randomUUID()

        // CreateFormTemplate
        var formTemplateRep = FormTemplateRepFixtures[0].complete(this, orgId, 0)
        piperTest.setup(
            endpointConfig = CreateFormTemplate.endpointConfig,
            body = FormTemplateRepFixtures[0].creation(orgId)
        )

        // CreateFormTemplateQuestion
        var formTemplateQuestionRep = FormTemplateQuestionRepFixtures[0].complete(this, 4)
                as FormTemplateTextQuestionRep.Complete
        formTemplateRep = formTemplateRep.copy(
            questions = listOf(formTemplateQuestionRep).plus(formTemplateRep.questions)
        )
        piperTest.setup(
            endpointConfig = CreateFormTemplateQuestion.endpointConfig,
            pathParams = mapOf(CreateFormTemplateQuestion.formTemplateId to formTemplateRep.id),
            queryParams = mapOf(CreateFormTemplateQuestion.index to 0),
            body = FormTemplateQuestionRepFixtures[0].creation()
        )

        // UpdateFormTemplateQuestion
        val formTemplateQuestionUpdateRep = FormTemplateTextQuestionRep.Update("Renamed Question")
        formTemplateQuestionRep = formTemplateQuestionRep.copy(label = formTemplateQuestionUpdateRep.label!!)
        formTemplateRep = formTemplateRep.copy(
            questions = formTemplateRep.questions.map {
                if (it.id == formTemplateQuestionRep.id) formTemplateQuestionRep else it
            }
        )
        piperTest.test(
            endpointConfig = UpdateFormTemplateQuestion.endpointConfig,
            pathParams = mapOf(
                UpdateFormTemplateQuestion.formTemplateId to formTemplateRep.id,
                UpdateFormTemplateQuestion.questionId to formTemplateQuestionRep.id
            ),
            body = formTemplateQuestionUpdateRep
        ) {}

        // GetFormTemplate
        piperTest.test(
            endpointConfig = GetFormTemplate.endpointConfig,
            pathParams = mapOf(GetFormTemplate.formTemplateId to formTemplateRep.id)
        ) {
            val actual = objectMapper.readValue<FormTemplateRep.Complete>(response.content!!)
            assertEquals(formTemplateRep, actual)
        }
    }
}
