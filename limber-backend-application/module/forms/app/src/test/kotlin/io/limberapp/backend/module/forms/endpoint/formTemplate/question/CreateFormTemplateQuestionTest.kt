package io.limberapp.backend.module.forms.endpoint.formTemplate.question

import com.fasterxml.jackson.module.kotlin.readValue
import com.piperframework.exception.exception.badRequest.RankOutOfBounds
import io.limberapp.backend.module.forms.endpoint.formTemplate.CreateFormTemplate
import io.limberapp.backend.module.forms.endpoint.formTemplate.GetFormTemplate
import io.limberapp.backend.module.forms.exception.notFound.FormTemplateNotFound
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.backend.module.forms.testing.ResourceTest
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateQuestionRepFixtures
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import org.junit.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class CreateFormTemplateQuestionTest : ResourceTest() {

    @Test
    fun formTemplateDoesNotExist() {

        // Setup
        val formTemplateId = UUID.randomUUID()

        // CreateFormTemplateQuestion
        piperTest.test(
            endpointConfig = CreateFormTemplateQuestion.endpointConfig,
            pathParams = mapOf(CreateFormTemplateQuestion.formTemplateId to formTemplateId),
            body = FormTemplateQuestionRepFixtures[0].creation(),
            expectedException = FormTemplateNotFound()
        )
    }

    @Test
    fun rankOutOfBoundsLow() {

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
            pathParams = mapOf(CreateFormTemplateQuestion.formTemplateId to formTemplateRep.id),
            queryParams = mapOf(CreateFormTemplateQuestion.rank to -1),
            body = FormTemplateQuestionRepFixtures[0].creation(),
            expectedException = RankOutOfBounds(-1)
        )
    }

    @Test
    fun rankOutOfBoundsHigh() {

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
            pathParams = mapOf(CreateFormTemplateQuestion.formTemplateId to formTemplateRep.id),
            queryParams = mapOf(CreateFormTemplateQuestion.rank to FormTemplateQuestionRepFixtures.defaults.size + 1),
            body = FormTemplateQuestionRepFixtures[0].creation(),
            expectedException = RankOutOfBounds((FormTemplateQuestionRepFixtures.defaults.size + 1))
        )
    }

    @Test
    fun happyPathFirstRank() {

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
        piperTest.test(
            endpointConfig = CreateFormTemplateQuestion.endpointConfig,
            pathParams = mapOf(CreateFormTemplateQuestion.formTemplateId to formTemplateRep.id),
            queryParams = mapOf(CreateFormTemplateQuestion.rank to 0),
            body = FormTemplateQuestionRepFixtures[0].creation()
        ) {
            val actual = objectMapper.readValue<FormTemplateQuestionRep.Complete>(response.content!!)
            assertEquals(formTemplateQuestionRep, actual)
        }

        // GetFormTemplate
        piperTest.test(
            endpointConfig = GetFormTemplate.endpointConfig,
            pathParams = mapOf(GetFormTemplate.formTemplateId to formTemplateRep.id)
        ) {
            val actual = objectMapper.readValue<FormTemplateRep.Complete>(response.content!!)
            assertEquals(formTemplateRep, actual)
        }
    }

    @Test
    fun happyPathLastRank() {

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
            questions = formTemplateRep.questions.plus(formTemplateQuestionRep)
        )
        piperTest.test(
            endpointConfig = CreateFormTemplateQuestion.endpointConfig,
            pathParams = mapOf(CreateFormTemplateQuestion.formTemplateId to formTemplateRep.id),
            body = FormTemplateQuestionRepFixtures[0].creation()
        ) {
            val actual = objectMapper.readValue<FormTemplateQuestionRep.Complete>(response.content!!)
            assertEquals(formTemplateQuestionRep, actual)
        }

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
