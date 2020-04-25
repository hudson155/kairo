package io.limberapp.backend.module.forms.endpoint.formInstance.question

import io.limberapp.backend.module.forms.api.formInstance.FormInstanceApi
import io.limberapp.backend.module.forms.api.formInstance.question.FormInstanceQuestionApi
import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
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
            endpoint = FormInstanceQuestionApi.Delete(formInstanceId, questionId),
            expectedException = FormInstanceNotFound()
        )
    }

    @Test
    fun formInstanceQuestionDoesNotExist() {
        val featureId = UUID.randomUUID()
        val questionId = UUID.randomUUID()

        val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, featureId, 0)
        piperTest.setup(FormTemplateApi.Post(FormTemplateRepFixtures.exampleFormFixture.creation(featureId)))

        var formInstanceRep = FormInstanceRepFixtures.fixture.complete(this, featureId, formTemplateRep.id, 5)
        piperTest.setup(FormInstanceApi.Post(FormInstanceRepFixtures.fixture.creation(featureId, formTemplateRep.id)))

        val formInstanceQuestionRep =
            FormInstanceQuestionRepFixtures.textFixture.complete(this, formTemplateRep.questions.first().id)
        formInstanceRep = formInstanceRep.copy(questions = formInstanceRep.questions.plus(formInstanceQuestionRep))
        piperTest.setup(
            endpoint = FormInstanceQuestionApi.Put(
                formInstanceId = formInstanceRep.id,
                questionId = formTemplateRep.questions.first().id,
                rep = FormInstanceQuestionRepFixtures.textFixture.creation()
            )
        )

        piperTest.test(
            endpoint = FormInstanceQuestionApi.Delete(formInstanceRep.id, questionId),
            expectedException = FormInstanceQuestionNotFound()
        )

        piperTest.test(FormInstanceApi.Get(formInstanceRep.id)) {
            val actual = json.parse<FormInstanceRep.Complete>(response.content!!)
            assertEquals(formInstanceRep, actual)
        }
    }

    @Test
    fun happyPath() {
        val featureId = UUID.randomUUID()

        val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, featureId, 0)
        piperTest.setup(FormTemplateApi.Post(FormTemplateRepFixtures.exampleFormFixture.creation(featureId)))

        var formInstanceRep = FormInstanceRepFixtures.fixture.complete(this, featureId, formTemplateRep.id, 5)
        piperTest.setup(FormInstanceApi.Post(FormInstanceRepFixtures.fixture.creation(featureId, formTemplateRep.id)))

        val formInstanceQuestionRep =
            FormInstanceQuestionRepFixtures.textFixture.complete(this, formTemplateRep.questions.first().id)
        formInstanceRep = formInstanceRep.copy(questions = formInstanceRep.questions.plus(formInstanceQuestionRep))
        piperTest.setup(
            endpoint = FormInstanceQuestionApi.Put(
                formInstanceId = formInstanceRep.id,
                questionId = formTemplateRep.questions.first().id,
                rep = FormInstanceQuestionRepFixtures.textFixture.creation()
            )
        )

        formInstanceRep = formInstanceRep.copy(questions = formInstanceRep.questions.minus(formInstanceQuestionRep))
        piperTest.test(
            endpoint = FormInstanceQuestionApi.Delete(
                formInstanceId = formInstanceRep.id,
                questionId = formInstanceQuestionRep.questionId!!
            )
        ) {}

        piperTest.test(FormInstanceApi.Get(formInstanceRep.id)) {
            val actual = json.parse<FormInstanceRep.Complete>(response.content!!)
            assertEquals(formInstanceRep, actual)
        }
    }
}
