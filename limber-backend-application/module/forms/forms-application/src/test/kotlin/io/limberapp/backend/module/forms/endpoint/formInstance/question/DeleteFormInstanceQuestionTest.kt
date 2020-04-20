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

        // Setup
        val formInstanceId = UUID.randomUUID()
        val questionId = UUID.randomUUID()

        // DeleteFormInstanceQuestion
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

        // Setup
        val featureId = UUID.randomUUID()
        val questionId = UUID.randomUUID()

        // PostFormTemplate
        val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, featureId, 0)
        piperTest.setup(
            endpointConfig = PostFormTemplate.endpointConfig,
            body = FormTemplateRepFixtures.exampleFormFixture.creation(featureId)
        )

        // PostFormInstance
        var formInstanceRep = FormInstanceRepFixtures.fixture.complete(this, featureId, formTemplateRep.id, 4)
        piperTest.setup(
            endpointConfig = PostFormInstance.endpointConfig,
            body = FormInstanceRepFixtures.fixture.creation(featureId, formTemplateRep.id)
        )

        // PutFormInstanceQuestion
        val formInstanceQuestionRep =
            FormInstanceQuestionRepFixtures.textFixture.complete(this, formTemplateRep.questions.first().id)
        formInstanceRep = formInstanceRep.copy(questions = formInstanceRep.questions.plus(formInstanceQuestionRep))
        piperTest.setup(
            endpointConfig = PutFormInstanceQuestion.endpointConfig,
            pathParams = mapOf(
                PutFormInstanceQuestion.formInstanceId to formInstanceRep.id,
                PutFormInstanceQuestion.formTemplateQuestionId to formTemplateRep.questions.first().id
            ),
            body = FormInstanceQuestionRepFixtures.textFixture.creation()
        )

        // DeleteFormInstanceQuestion
        piperTest.test(
            endpointConfig = DeleteFormInstanceQuestion.endpointConfig,
            pathParams = mapOf(
                DeleteFormInstanceQuestion.formInstanceId to formInstanceRep.id,
                DeleteFormInstanceQuestion.questionId to questionId
            ),
            expectedException = FormInstanceQuestionNotFound()
        )

        // GetFormInstance
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

        // Setup
        val featureId = UUID.randomUUID()

        // PostFormTemplate
        val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, featureId, 0)
        piperTest.setup(
            endpointConfig = PostFormTemplate.endpointConfig,
            body = FormTemplateRepFixtures.exampleFormFixture.creation(featureId)
        )

        // PostFormInstance
        var formInstanceRep = FormInstanceRepFixtures.fixture.complete(this, featureId, formTemplateRep.id, 4)
        piperTest.setup(
            endpointConfig = PostFormInstance.endpointConfig,
            body = FormInstanceRepFixtures.fixture.creation(featureId, formTemplateRep.id)
        )

        // PutFormInstanceQuestion
        val formInstanceQuestionRep =
            FormInstanceQuestionRepFixtures.textFixture.complete(this, formTemplateRep.questions.first().id)
        formInstanceRep = formInstanceRep.copy(questions = formInstanceRep.questions.plus(formInstanceQuestionRep))
        piperTest.setup(
            endpointConfig = PutFormInstanceQuestion.endpointConfig,
            pathParams = mapOf(
                PutFormInstanceQuestion.formInstanceId to formInstanceRep.id,
                PutFormInstanceQuestion.formTemplateQuestionId to formTemplateRep.questions.first().id
            ),
            body = FormInstanceQuestionRepFixtures.textFixture.creation()
        )

        // DeleteFormInstanceQuestion
        formInstanceRep = formInstanceRep.copy(questions = formInstanceRep.questions.minus(formInstanceQuestionRep))
        piperTest.test(
            endpointConfig = DeleteFormInstanceQuestion.endpointConfig,
            pathParams = mapOf(
                DeleteFormInstanceQuestion.formInstanceId to formInstanceRep.id,
                DeleteFormInstanceQuestion.questionId to formInstanceQuestionRep.formTemplateQuestionId!!
            )
        ) {}

        // GetFormInstance
        piperTest.test(
            endpointConfig = GetFormInstance.endpointConfig,
            pathParams = mapOf(GetFormInstance.formInstanceId to formInstanceRep.id)
        ) {
            val actual = json.parse<FormInstanceRep.Complete>(response.content!!)
            assertEquals(formInstanceRep, actual)
        }
    }
}
