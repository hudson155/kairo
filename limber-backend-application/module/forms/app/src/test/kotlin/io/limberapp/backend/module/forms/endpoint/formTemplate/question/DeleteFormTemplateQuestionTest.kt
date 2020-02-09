package io.limberapp.backend.module.forms.endpoint.formTemplate.question

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.module.forms.endpoint.formTemplate.CreateFormTemplate
import io.limberapp.backend.module.forms.endpoint.formTemplate.GetFormTemplate
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

        // Setup
        val formTemplateId = UUID.randomUUID()
        val questionId = UUID.randomUUID()

        // DeleteFormTemplateQuestion
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

        // Setup
        val orgId = UUID.randomUUID()
        val questionId = UUID.randomUUID()

        // CreateFormTemplate
        val formTemplateRep = FormTemplateRepFixtures[0].complete(this, orgId, 0)
        piperTest.setup(
            endpointConfig = CreateFormTemplate.endpointConfig,
            body = FormTemplateRepFixtures[0].creation(orgId)
        )

        // DeleteFormTemplateQuestion
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

        // Setup
        val orgId = UUID.randomUUID()

        // CreateFormTemplate
        var formTemplateRep = FormTemplateRepFixtures[0].complete(this, orgId, 0)
        piperTest.setup(
            endpointConfig = CreateFormTemplate.endpointConfig,
            body = FormTemplateRepFixtures[0].creation(orgId)
        )

        // CreateFormTemplateQuestion
        val formTemplateQuestionRep = FormTemplateQuestionRepFixtures[0].complete(this, 4)
        formTemplateRep = formTemplateRep.copy(
            questions = listOf(formTemplateQuestionRep).plus(formTemplateRep.questions)
        )
        piperTest.setup(
            endpointConfig = CreateFormTemplateQuestion.endpointConfig,
            pathParams = mapOf(CreateFormTemplateQuestion.formTemplateId to formTemplateRep.id),
            queryParams = mapOf(CreateFormTemplateQuestion.rank to 0),
            body = FormTemplateQuestionRepFixtures[0].creation()
        )

        // DeleteFormTemplateQuestion
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
