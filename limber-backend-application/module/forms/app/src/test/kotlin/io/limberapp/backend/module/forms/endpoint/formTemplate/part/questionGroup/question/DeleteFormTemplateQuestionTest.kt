package io.limberapp.backend.module.forms.endpoint.formTemplate.part.questionGroup.question

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.backend.module.forms.endpoint.formTemplate.CreateFormTemplate
import io.limberapp.backend.module.forms.endpoint.formTemplate.GetFormTemplate
import io.limberapp.backend.module.forms.exception.notFound.FormTemplateNotFound
import io.limberapp.backend.module.forms.exception.notFound.FormTemplatePartNotFound
import io.limberapp.backend.module.forms.exception.notFound.FormTemplateQuestionGroupNotFound
import io.limberapp.backend.module.forms.exception.notFound.FormTemplateQuestionNotFound
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.backend.module.forms.testing.ResourceTest
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateQuestionRepFixtures
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import org.junit.Ignore
import org.junit.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class DeleteFormTemplateQuestionTest : ResourceTest() {

    @Test
    fun formTemplateDoesNotExist() {

        // Setup
        val formTemplateId = UUID.randomUUID()
        val partId = UUID.randomUUID()
        val questionGroupId = UUID.randomUUID()
        val questionId = UUID.randomUUID()

        // DeleteFormTemplate
        piperTest.test(
            endpointConfig = DeleteFormTemplateQuestion.endpointConfig,
            pathParams = mapOf(
                DeleteFormTemplateQuestion.formTemplateId to formTemplateId,
                DeleteFormTemplateQuestion.partId to partId,
                DeleteFormTemplateQuestion.questionGroupId to questionGroupId,
                DeleteFormTemplateQuestion.questionId to questionId
            ),
            expectedException = FormTemplateNotFound()
        )
    }

    @Test
    fun formTemplatePartDoesNotExist() {

        // Setup
        val orgId = UUID.randomUUID()
        val partId = UUID.randomUUID()
        val questionGroupId = UUID.randomUUID()
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
                DeleteFormTemplateQuestion.partId to partId,
                DeleteFormTemplateQuestion.questionGroupId to questionGroupId,
                DeleteFormTemplateQuestion.questionId to questionId
            ),
            expectedException = FormTemplatePartNotFound()
        )
    }

    @Test
    fun formTemplateQuestionGroupDoesNotExist() {

        // Setup
        val orgId = UUID.randomUUID()
        val questionGroupId = UUID.randomUUID()
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
                DeleteFormTemplateQuestion.partId to formTemplateRep.parts.single().id,
                DeleteFormTemplateQuestion.questionGroupId to questionGroupId,
                DeleteFormTemplateQuestion.questionId to questionId
            ),
            expectedException = FormTemplateQuestionGroupNotFound()
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
                DeleteFormTemplateQuestion.partId to formTemplateRep.parts.single().id,
                DeleteFormTemplateQuestion.questionGroupId to formTemplateRep.parts.single().questionGroups.single().id,
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
        val formTemplateQuestionRep = FormTemplateQuestionRepFixtures[0].complete(this, 6)
        formTemplateRep = formTemplateRep.copy(
            parts = listOf(with(formTemplateRep.parts.single()) part@{
                this@part.copy(
                    questionGroups = listOf(with(this@part.questionGroups.single()) questionGroup@{
                        this@questionGroup.copy(
                            questions = listOf(formTemplateQuestionRep).plus(this@questionGroup.questions)
                        )
                    })
                )
            })
        )
        piperTest.setup(
            endpointConfig = CreateFormTemplateQuestion.endpointConfig,
            pathParams = mapOf(
                CreateFormTemplateQuestion.formTemplateId to formTemplateRep.id,
                CreateFormTemplateQuestion.partId to formTemplateRep.parts.single().id,
                CreateFormTemplateQuestion.questionGroupId to formTemplateRep.parts.single().questionGroups.single().id
            ),
            queryParams = mapOf(CreateFormTemplateQuestion.index to 0),
            body = FormTemplateQuestionRepFixtures[0].creation()
        )

        // DeleteFormTemplatePart
        formTemplateRep = formTemplateRep.copy(
            parts = listOf(with(formTemplateRep.parts.single()) part@{
                this@part.copy(
                    questionGroups = listOf(with(this@part.questionGroups.single()) questionGroup@{
                        this@questionGroup.copy(
                            questions = this@questionGroup.questions.filter { it.id != formTemplateQuestionRep.id }
                        )
                    })
                )
            })
        )
        piperTest.test(
            endpointConfig = DeleteFormTemplateQuestion.endpointConfig,
            pathParams = mapOf(
                DeleteFormTemplateQuestion.formTemplateId to formTemplateRep.id,
                DeleteFormTemplateQuestion.partId to formTemplateRep.parts.single().id,
                DeleteFormTemplateQuestion.questionGroupId to formTemplateRep.parts.single().questionGroups.single().id,
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
