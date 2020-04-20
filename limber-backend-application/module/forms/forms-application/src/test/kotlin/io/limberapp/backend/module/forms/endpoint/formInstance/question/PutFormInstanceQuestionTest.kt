package io.limberapp.backend.module.forms.endpoint.formInstance.question

import io.limberapp.backend.module.forms.endpoint.formInstance.GetFormInstance
import io.limberapp.backend.module.forms.endpoint.formInstance.PostFormInstance
import io.limberapp.backend.module.forms.endpoint.formTemplate.PostFormTemplate
import io.limberapp.backend.module.forms.exception.formInstance.FormInstanceNotFound
import io.limberapp.backend.module.forms.exception.formTemplate.FormTemplateQuestionNotFound
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceQuestionRep
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion.FormInstanceTextQuestionRep
import io.limberapp.backend.module.forms.testing.ResourceTest
import io.limberapp.backend.module.forms.testing.fixtures.formInstance.FormInstanceQuestionRepFixtures
import io.limberapp.backend.module.forms.testing.fixtures.formInstance.FormInstanceRepFixtures
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class PutFormInstanceQuestionTest : ResourceTest() {

    @Test
    fun formInstanceDoesNotExist() {

        val questionId = UUID.randomUUID()
        val formInstanceId = UUID.randomUUID()

        piperTest.test(
            endpointConfig = PutFormInstanceQuestion.endpointConfig,
            pathParams = mapOf(
                PutFormInstanceQuestion.formInstanceId to formInstanceId,
                PutFormInstanceQuestion.questionId to questionId
            ),
            body = FormInstanceQuestionRepFixtures.textFixture.creation(),
            expectedException = FormInstanceNotFound()
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

        val formInstanceRep = FormInstanceRepFixtures.fixture.complete(this, featureId, formTemplateRep.id, 5)
        piperTest.setup(
            endpointConfig = PostFormInstance.endpointConfig,
            body = FormInstanceRepFixtures.fixture.creation(featureId, formTemplateRep.id)
        )

        piperTest.test(
            endpointConfig = PutFormInstanceQuestion.endpointConfig,
            pathParams = mapOf(
                PutFormInstanceQuestion.formInstanceId to formInstanceRep.id,
                PutFormInstanceQuestion.questionId to questionId
            ),
            body = FormInstanceQuestionRepFixtures.textFixture.creation(),
            expectedException = FormTemplateQuestionNotFound()
        )
    }

    @Test
    fun happyPath() {

        val featureId = UUID.randomUUID()

        val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, featureId, 0)
        piperTest.setup(
            endpointConfig = PostFormTemplate.endpointConfig,
            body = FormTemplateRepFixtures.exampleFormFixture.creation(featureId)
        )

        var formInstanceRep = FormInstanceRepFixtures.fixture.complete(this, featureId, formTemplateRep.id, 5)
        piperTest.setup(
            endpointConfig = PostFormInstance.endpointConfig,
            body = FormInstanceRepFixtures.fixture.creation(featureId, formTemplateRep.id)
        )

        val formInstanceQuestionRep =
            FormInstanceQuestionRepFixtures.textFixture.complete(this, formTemplateRep.questions.first().id)
        formInstanceRep = formInstanceRep.copy(questions = formInstanceRep.questions.plus(formInstanceQuestionRep))
        piperTest.test(
            endpointConfig = PutFormInstanceQuestion.endpointConfig,
            pathParams = mapOf(
                PutFormInstanceQuestion.formInstanceId to formInstanceRep.id,
                PutFormInstanceQuestion.questionId to formTemplateRep.questions.first().id
            ),
            body = FormInstanceQuestionRepFixtures.textFixture.creation()
        ) {
            val actual = json.parse<FormInstanceQuestionRep.Complete>(response.content!!)
            assertEquals(formInstanceQuestionRep, actual)
        }

        piperTest.test(
            endpointConfig = GetFormInstance.endpointConfig,
            pathParams = mapOf(GetFormInstance.formInstanceId to formInstanceRep.id)
        ) {
            val actual = json.parse<FormInstanceRep.Complete>(response.content!!)
            assertEquals(formInstanceRep, actual)
        }
    }

    @Test
    fun happyPathIdempotent() {

        val featureId = UUID.randomUUID()

        val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, featureId, 0)
        piperTest.setup(
            endpointConfig = PostFormTemplate.endpointConfig,
            body = FormTemplateRepFixtures.exampleFormFixture.creation(featureId)
        )

        var formInstanceRep = FormInstanceRepFixtures.fixture.complete(this, featureId, formTemplateRep.id, 5)
        piperTest.setup(
            endpointConfig = PostFormInstance.endpointConfig,
            body = FormInstanceRepFixtures.fixture.creation(featureId, formTemplateRep.id)
        )

        val formInstanceQuestion0Rep =
            FormInstanceQuestionRepFixtures.textFixture.complete(this, formTemplateRep.questions.first().id)
        formInstanceRep = formInstanceRep.copy(questions = formInstanceRep.questions.plus(formInstanceQuestion0Rep))
        piperTest.setup(
            endpointConfig = PutFormInstanceQuestion.endpointConfig,
            pathParams = mapOf(
                PutFormInstanceQuestion.formInstanceId to formInstanceRep.id,
                PutFormInstanceQuestion.questionId to formTemplateRep.questions.first().id
            ),
            body = FormInstanceQuestionRepFixtures.textFixture.creation()
        )

        val formInstanceQuestion1Rep =
            (FormInstanceQuestionRepFixtures.textFixture.complete(this, formTemplateRep.questions.first().id)
                    as FormInstanceTextQuestionRep.Complete)
                .copy(text = "completely new text")
        formInstanceRep = formInstanceRep.copy(questions = formInstanceRep.questions.minus(formInstanceQuestion0Rep))
        formInstanceRep = formInstanceRep.copy(questions = formInstanceRep.questions.plus(formInstanceQuestion1Rep))
        piperTest.test(
            endpointConfig = PutFormInstanceQuestion.endpointConfig,
            pathParams = mapOf(
                PutFormInstanceQuestion.formInstanceId to formInstanceRep.id,
                PutFormInstanceQuestion.questionId to formTemplateRep.questions.first().id
            ),
            body = (FormInstanceQuestionRepFixtures.textFixture.creation() as FormInstanceTextQuestionRep.Creation)
                .copy(text = "completely new text")
        ) {
            val actual = json.parse<FormInstanceQuestionRep.Complete>(response.content!!)
            assertEquals(formInstanceQuestion1Rep, actual)
        }

        piperTest.test(
            endpointConfig = GetFormInstance.endpointConfig,
            pathParams = mapOf(GetFormInstance.formInstanceId to formInstanceRep.id)
        ) {
            val actual = json.parse<FormInstanceRep.Complete>(response.content!!)
            assertEquals(formInstanceRep, actual)
        }
    }
}
