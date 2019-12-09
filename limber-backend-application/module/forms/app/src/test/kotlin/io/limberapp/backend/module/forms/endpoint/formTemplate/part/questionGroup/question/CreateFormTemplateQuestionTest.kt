package io.limberapp.backend.module.forms.endpoint.formTemplate.part.questionGroup.question

import com.fasterxml.jackson.module.kotlin.readValue
import com.piperframework.exception.exception.badRequest.IndexOutOfBounds
import io.limberapp.backend.module.forms.endpoint.formTemplate.CreateFormTemplate
import io.limberapp.backend.module.forms.endpoint.formTemplate.GetFormTemplate
import io.limberapp.backend.module.forms.exception.notFound.FormTemplateNotFound
import io.limberapp.backend.module.forms.exception.notFound.FormTemplatePartNotFound
import io.limberapp.backend.module.forms.exception.notFound.FormTemplateQuestionGroupNotFound
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.backend.module.forms.testing.ResourceTest
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateQuestionRepFixtures
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import org.junit.Ignore
import org.junit.Test
import java.util.UUID
import kotlin.test.assertEquals

@Ignore
internal class CreateFormTemplateQuestionTest : ResourceTest() {

    @Test
    fun formTemplateDoesNotExist() {

        // Setup
        val formTemplateId = UUID.randomUUID()
        val partId = UUID.randomUUID()
        val questionGroupId = UUID.randomUUID()

        // CreateFormTemplateQuestion
        piperTest.test(
            endpointConfig = CreateFormTemplateQuestion.endpointConfig,
            pathParams = mapOf(
                CreateFormTemplateQuestion.formTemplateId to formTemplateId,
                CreateFormTemplateQuestion.partId to partId,
                CreateFormTemplateQuestion.questionGroupId to questionGroupId
            ),
            body = FormTemplateQuestionRepFixtures[0].creation(),
            expectedException = FormTemplateNotFound()
        )
    }

    @Test
    fun formTemplatePartDoesNotExist() {

        // Setup
        val orgId = UUID.randomUUID()
        val partId = UUID.randomUUID()
        val questionGroupId = UUID.randomUUID()

        // CreateFormTemplate
        val formTemplateRep = FormTemplateRepFixtures[0].complete(this, orgId, 0)
        piperTest.setup(
            endpointConfig = CreateFormTemplate.endpointConfig,
            body = FormTemplateRepFixtures[0].creation(orgId)
        )

        // CreateFormTemplateQuestion
        piperTest.test(
            endpointConfig = CreateFormTemplateQuestion.endpointConfig,
            pathParams = mapOf(
                CreateFormTemplateQuestion.formTemplateId to formTemplateRep.id,
                CreateFormTemplateQuestion.partId to partId,
                CreateFormTemplateQuestion.questionGroupId to questionGroupId
            ),
            body = FormTemplateQuestionRepFixtures[0].creation(),
            expectedException = FormTemplatePartNotFound()
        )
    }

    @Test
    fun formTemplateQuestionGroupDoesNotExist() {

        // Setup
        val orgId = UUID.randomUUID()
        val questionGroupId = UUID.randomUUID()

        // CreateFormTemplate
        val formTemplateRep = FormTemplateRepFixtures[0].complete(this, orgId, 0)
        piperTest.setup(
            endpointConfig = CreateFormTemplate.endpointConfig,
            body = FormTemplateRepFixtures[0].creation(orgId)
        )

        // CreateFormTemplateQuestion
        piperTest.test(
            endpointConfig = CreateFormTemplateQuestion.endpointConfig,
            pathParams = mapOf(
                CreateFormTemplateQuestion.formTemplateId to formTemplateRep.id,
                CreateFormTemplateQuestion.partId to formTemplateRep.parts.single().id,
                CreateFormTemplateQuestion.questionGroupId to questionGroupId
            ),
            body = FormTemplateQuestionRepFixtures[0].creation(),
            expectedException = FormTemplateQuestionGroupNotFound()
        )
    }

    @Test
    fun indexOutOfBoundsLow() {

        // Setup
        val orgId = UUID.randomUUID()

        // CreateFormTemplate
        val formTemplateRep = FormTemplateRepFixtures[0].complete(this, orgId, 0)
        piperTest.setup(
            endpointConfig = CreateFormTemplate.endpointConfig,
            body = FormTemplateRepFixtures[0].creation(orgId)
        )

        // CreateFormTemplateQuestion
        piperTest.test(
            endpointConfig = CreateFormTemplateQuestion.endpointConfig,
            pathParams = mapOf(
                CreateFormTemplateQuestion.formTemplateId to formTemplateRep.id,
                CreateFormTemplateQuestion.partId to formTemplateRep.parts.single().id,
                CreateFormTemplateQuestion.questionGroupId to formTemplateRep.parts.single().questionGroups.single().id
            ),
            queryParams = mapOf(CreateFormTemplateQuestion.index to -1),
            body = FormTemplateQuestionRepFixtures[0].creation(),
            expectedException = IndexOutOfBounds()
        )
    }

    @Test
    fun indexOutOfBoundsHigh() {

        // Setup
        val orgId = UUID.randomUUID()

        // CreateFormTemplate
        val formTemplateRep = FormTemplateRepFixtures[0].complete(this, orgId, 0)
        piperTest.setup(
            endpointConfig = CreateFormTemplate.endpointConfig,
            body = FormTemplateRepFixtures[0].creation(orgId)
        )

        // CreateFormTemplateQuestion
        piperTest.test(
            endpointConfig = CreateFormTemplateQuestion.endpointConfig,
            pathParams = mapOf(
                CreateFormTemplateQuestion.formTemplateId to formTemplateRep.id,
                CreateFormTemplateQuestion.partId to formTemplateRep.parts.single().id,
                CreateFormTemplateQuestion.questionGroupId to formTemplateRep.parts.single().questionGroups.single().id
            ),
            queryParams = mapOf(CreateFormTemplateQuestion.index to FormTemplateQuestionRepFixtures.defaults.size + 1),
            body = FormTemplateQuestionRepFixtures[0].creation(),
            expectedException = IndexOutOfBounds()
        )
    }

    @Test
    fun happyPathFirstIndex() {

        // Setup
        val orgId = UUID.randomUUID()

        // CreateFormTemplate
        var formTemplateRep = FormTemplateRepFixtures[0].complete(this, orgId, 0)
        piperTest.setup(
            endpointConfig = CreateFormTemplate.endpointConfig,
            body = FormTemplateRepFixtures[0].creation(orgId)
        )

        // CreateFormTemplateQuestion
        val formTemplateQuestionRep = FormTemplateQuestionRepFixtures[0].complete(this, 2)
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
        piperTest.test(
            endpointConfig = CreateFormTemplateQuestion.endpointConfig,
            pathParams = mapOf(
                CreateFormTemplateQuestion.formTemplateId to formTemplateRep.id,
                CreateFormTemplateQuestion.partId to formTemplateRep.parts.single().id,
                CreateFormTemplateQuestion.questionGroupId to formTemplateRep.parts.single().questionGroups.single().id
            ),
            queryParams = mapOf(CreateFormTemplateQuestion.index to 0),
            body = FormTemplateQuestionRepFixtures[0].creation()
        ) {
            val actual = objectMapper.readValue<FormTemplateQuestionRep.Complete>(response.content!!)
            assertEquals(formTemplateQuestionRep, actual)
        }

        // GetFormTemplate
        piperTest.test(
            endpointConfig = GetFormTemplate.endpointConfig
        ) {
            val actual = objectMapper.readValue<FormTemplateRep.Complete>(response.content!!)
            assertEquals(formTemplateRep, actual)
        }
    }

    @Test
    fun happyPathLastIndex() {

        // Setup
        val orgId = UUID.randomUUID()

        // CreateFormTemplate
        var formTemplateRep = FormTemplateRepFixtures[0].complete(this, orgId, 0)
        piperTest.setup(
            endpointConfig = CreateFormTemplate.endpointConfig,
            body = FormTemplateRepFixtures[0].creation(orgId)
        )

        // CreateFormTemplateQuestion
        val formTemplateQuestionRep = FormTemplateQuestionRepFixtures[0].complete(this, 2)
        formTemplateRep = formTemplateRep.copy(
            parts = listOf(with(formTemplateRep.parts.single()) part@{
                this@part.copy(
                    questionGroups = listOf(with(this@part.questionGroups.single()) questionGroup@{
                        this@questionGroup.copy(
                            questions = this@questionGroup.questions.plus(formTemplateQuestionRep)
                        )
                    })
                )
            })
        )
        piperTest.test(
            endpointConfig = CreateFormTemplateQuestion.endpointConfig,
            pathParams = mapOf(
                CreateFormTemplateQuestion.formTemplateId to formTemplateRep.id,
                CreateFormTemplateQuestion.partId to formTemplateRep.parts.single().id,
                CreateFormTemplateQuestion.questionGroupId to formTemplateRep.parts.single().questionGroups.single().id
            ),
            body = FormTemplateQuestionRepFixtures[0].creation()
        ) {
            val actual = objectMapper.readValue<FormTemplateQuestionRep.Complete>(response.content!!)
            assertEquals(formTemplateQuestionRep, actual)
        }

        // GetFormTemplate
        piperTest.test(
            endpointConfig = GetFormTemplate.endpointConfig
        ) {
            val actual = objectMapper.readValue<FormTemplateRep.Complete>(response.content!!)
            assertEquals(formTemplateRep, actual)
        }
    }
}
