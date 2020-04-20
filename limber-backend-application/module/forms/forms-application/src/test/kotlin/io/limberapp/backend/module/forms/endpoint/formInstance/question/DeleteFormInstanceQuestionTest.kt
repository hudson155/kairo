package io.limberapp.backend.module.forms.endpoint.formInstance.question

import io.limberapp.backend.module.forms.endpoint.formInstance.GetFormInstance
import io.limberapp.backend.module.forms.endpoint.formInstance.PostFormInstance
import io.limberapp.backend.module.forms.endpoint.formTemplate.PostFormTemplate
import io.limberapp.backend.module.forms.exception.formInstance.FormInstanceNotFound
import io.limberapp.backend.module.forms.exception.formInstance.FormInstanceQuestionNotFound
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import io.limberapp.backend.module.forms.testing.ResourceTest
import io.limberapp.backend.module.forms.testing.fixtures.formInstance.FormInstanceQuestionRepFixtures
import io.limberapp.backend.module.forms.testing.fixtures.formInstance.FormInstanceRepFixtures
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class DeleteFormInstanceQuestionTest : ResourceTest() {

    @Test
    fun formInstanceDoesNotExist() {

        val formInstanceId = UUID.randomUUID()
        val questionId = UUID.randomUUID()

        piperTest.test(
            endpointConfig = DeleteFormInstanceQuestion.endpointConfig,
            pathParams = mapOf(
                DeleteFormInstanceQuestion.formInstanceId to formInstanceId,
                DeleteFormInstanceQuestion.questionId to questionId
            ),
            expectedException = FormInstanceNotFound()
        )
    }

    @Test
    fun formInstanceQuestionDoesNotExist() {

        val featureId = UUID.randomUUID()
        val questionId = UUID.randomUUID()

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
        piperTest.setup(
            endpointConfig = PutFormInstanceQuestion.endpointConfig,
            pathParams = mapOf(
                PutFormInstanceQuestion.formInstanceId to formInstanceRep.id,
                PutFormInstanceQuestion.questionId to formTemplateRep.questions.first().id
            ),
            body = FormInstanceQuestionRepFixtures.textFixture.creation()
        )

        piperTest.test(
            endpointConfig = DeleteFormInstanceQuestion.endpointConfig,
            pathParams = mapOf(
                DeleteFormInstanceQuestion.formInstanceId to formInstanceRep.id,
                DeleteFormInstanceQuestion.questionId to questionId
            ),
            expectedException = FormInstanceQuestionNotFound()
        )

        piperTest.test(
            endpointConfig = GetFormInstance.endpointConfig,
            pathParams = mapOf(GetFormInstance.formInstanceId to formInstanceRep.id)
        ) {
            val actual = json.parse<FormInstanceRep.Complete>(response.content!!)
            assertEquals(formInstanceRep, actual)
        }
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
        piperTest.setup(
            endpointConfig = PutFormInstanceQuestion.endpointConfig,
            pathParams = mapOf(
                PutFormInstanceQuestion.formInstanceId to formInstanceRep.id,
                PutFormInstanceQuestion.questionId to formTemplateRep.questions.first().id
            ),
            body = FormInstanceQuestionRepFixtures.textFixture.creation()
        )

        formInstanceRep = formInstanceRep.copy(questions = formInstanceRep.questions.minus(formInstanceQuestionRep))
        piperTest.test(
            endpointConfig = DeleteFormInstanceQuestion.endpointConfig,
            pathParams = mapOf(
                DeleteFormInstanceQuestion.formInstanceId to formInstanceRep.id,
                DeleteFormInstanceQuestion.questionId to formInstanceQuestionRep.questionId!!
            )
        ) {}

        piperTest.test(
            endpointConfig = GetFormInstance.endpointConfig,
            pathParams = mapOf(GetFormInstance.formInstanceId to formInstanceRep.id)
        ) {
            val actual = json.parse<FormInstanceRep.Complete>(response.content!!)
            assertEquals(formInstanceRep, actual)
        }
    }
}
