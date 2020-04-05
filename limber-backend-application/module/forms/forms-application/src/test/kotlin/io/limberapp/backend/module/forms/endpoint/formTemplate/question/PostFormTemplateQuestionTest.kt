package io.limberapp.backend.module.forms.endpoint.formTemplate.question

import com.piperframework.exception.exception.badRequest.RankOutOfBounds
import io.limberapp.backend.module.forms.endpoint.formTemplate.GetFormTemplate
import io.limberapp.backend.module.forms.endpoint.formTemplate.PostFormTemplate
import io.limberapp.backend.module.forms.exception.formTemplate.FormTemplateNotFound
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.backend.module.forms.testing.ResourceTest
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateQuestionRepFixtures
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class PostFormTemplateQuestionTest : ResourceTest() {

    @Test
    fun formTemplateDoesNotExist() {

        // Setup
        val formTemplateId = UUID.randomUUID()

        // PostFormTemplateQuestion
        piperTest.test(
            endpointConfig = PostFormTemplateQuestion.endpointConfig,
            pathParams = mapOf(PostFormTemplateQuestion.formTemplateId to formTemplateId),
            body = FormTemplateQuestionRepFixtures.textFixture.creation(),
            expectedException = FormTemplateNotFound()
        )
    }

    @Test
    fun rankOutOfBoundsLow() {

        // Setup
        val featureId = UUID.randomUUID()

        // PostFormTemplate
        val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, featureId, 0)
        piperTest.setup(
            endpointConfig = PostFormTemplate.endpointConfig,
            body = FormTemplateRepFixtures.exampleFormFixture.creation(featureId)
        )

        // PostFormTemplateQuestion
        piperTest.test(
            endpointConfig = PostFormTemplateQuestion.endpointConfig,
            pathParams = mapOf(PostFormTemplateQuestion.formTemplateId to formTemplateRep.id),
            queryParams = mapOf(PostFormTemplateQuestion.rank to -1),
            body = FormTemplateQuestionRepFixtures.textFixture.creation(),
            expectedException = RankOutOfBounds(-1)
        )
    }

    @Test
    fun rankOutOfBoundsHigh() {

        // Setup
        val featureId = UUID.randomUUID()

        // PostFormTemplate
        val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, featureId, 0)
        piperTest.setup(
            endpointConfig = PostFormTemplate.endpointConfig,
            body = FormTemplateRepFixtures.exampleFormFixture.creation(featureId)
        )

        // PostFormTemplateQuestion
        piperTest.test(
            endpointConfig = PostFormTemplateQuestion.endpointConfig,
            pathParams = mapOf(PostFormTemplateQuestion.formTemplateId to formTemplateRep.id),
            queryParams = mapOf(PostFormTemplateQuestion.rank to FormTemplateQuestionRepFixtures.defaults.size + 1),
            body = FormTemplateQuestionRepFixtures.textFixture.creation(),
            expectedException = RankOutOfBounds((FormTemplateQuestionRepFixtures.defaults.size + 1))
        )
    }

    @Test
    fun happyPathFirstRank() {

        // Setup
        val featureId = UUID.randomUUID()

        // PostFormTemplate
        var formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, featureId, 0)
        piperTest.setup(
            endpointConfig = PostFormTemplate.endpointConfig,
            body = FormTemplateRepFixtures.exampleFormFixture.creation(featureId)
        )

        // PostFormTemplateQuestion
        val formTemplateQuestionRep = FormTemplateQuestionRepFixtures.textFixture.complete(this, 6)
        formTemplateRep = formTemplateRep.copy(
            questions = listOf(formTemplateQuestionRep).plus(formTemplateRep.questions)
        )
        piperTest.test(
            endpointConfig = PostFormTemplateQuestion.endpointConfig,
            pathParams = mapOf(PostFormTemplateQuestion.formTemplateId to formTemplateRep.id),
            queryParams = mapOf(PostFormTemplateQuestion.rank to 0),
            body = FormTemplateQuestionRepFixtures.textFixture.creation()
        ) {
            val actual = json.parse<FormTemplateQuestionRep.Complete>(response.content!!)
            assertEquals(formTemplateQuestionRep, actual)
        }

        // GetFormTemplate
        piperTest.test(
            endpointConfig = GetFormTemplate.endpointConfig,
            pathParams = mapOf(GetFormTemplate.formTemplateId to formTemplateRep.id)
        ) {
            val actual = json.parse<FormTemplateRep.Complete>(response.content!!)
            assertEquals(formTemplateRep, actual)
        }
    }

    @Test
    fun happyPathLastRank() {

        // Setup
        val featureId = UUID.randomUUID()

        // PostFormTemplate
        var formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, featureId, 0)
        piperTest.setup(
            endpointConfig = PostFormTemplate.endpointConfig,
            body = FormTemplateRepFixtures.exampleFormFixture.creation(featureId)
        )

        // PostFormTemplateQuestion
        val formTemplateQuestionRep = FormTemplateQuestionRepFixtures.textFixture.complete(this, 6)
        formTemplateRep = formTemplateRep.copy(
            questions = formTemplateRep.questions.plus(formTemplateQuestionRep)
        )
        piperTest.test(
            endpointConfig = PostFormTemplateQuestion.endpointConfig,
            pathParams = mapOf(PostFormTemplateQuestion.formTemplateId to formTemplateRep.id),
            body = FormTemplateQuestionRepFixtures.textFixture.creation()
        ) {
            val actual = json.parse<FormTemplateQuestionRep.Complete>(response.content!!)
            assertEquals(formTemplateQuestionRep, actual)
        }

        // GetFormTemplate
        piperTest.test(
            endpointConfig = GetFormTemplate.endpointConfig,
            pathParams = mapOf(GetFormTemplate.formTemplateId to formTemplateRep.id)
        ) {
            val actual = json.parse<FormTemplateRep.Complete>(response.content!!)
            assertEquals(formTemplateRep, actual)
        }
    }
}
