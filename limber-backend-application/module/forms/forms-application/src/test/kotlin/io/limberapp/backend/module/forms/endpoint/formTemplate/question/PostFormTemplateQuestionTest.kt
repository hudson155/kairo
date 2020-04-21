package io.limberapp.backend.module.forms.endpoint.formTemplate.question

import com.piperframework.exception.exception.badRequest.RankOutOfBounds
import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.backend.module.forms.api.formTemplate.question.FormTemplateQuestionApi
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

        val formTemplateId = UUID.randomUUID()

        piperTest.test(
            endpoint = FormTemplateQuestionApi.Post(
                formTemplateId = formTemplateId,
                rep = FormTemplateQuestionRepFixtures.textFixture.creation()
            ),
            expectedException = FormTemplateNotFound()
        )
    }

    @Test
    fun rankOutOfBoundsLow() {

        val featureId = UUID.randomUUID()

        val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, featureId, 0)
        piperTest.setup(FormTemplateApi.Post(FormTemplateRepFixtures.exampleFormFixture.creation(featureId)))

        piperTest.test(
            endpoint = FormTemplateQuestionApi.Post(
                formTemplateId = formTemplateRep.id,
                rank = -1,
                rep = FormTemplateQuestionRepFixtures.textFixture.creation()
            ),
            expectedException = RankOutOfBounds(-1)
        )
    }

    @Test
    fun rankOutOfBoundsHigh() {

        val featureId = UUID.randomUUID()

        val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, featureId, 0)
        piperTest.setup(FormTemplateApi.Post(FormTemplateRepFixtures.exampleFormFixture.creation(featureId)))

        piperTest.test(
            endpoint = FormTemplateQuestionApi.Post(
                formTemplateId = formTemplateRep.id,
                rank = FormTemplateQuestionRepFixtures.defaults.size + 1,
                rep = FormTemplateQuestionRepFixtures.textFixture.creation()
            ),
            expectedException = RankOutOfBounds((FormTemplateQuestionRepFixtures.defaults.size + 1))
        )
    }

    @Test
    fun happyPathFirstRank() {

        val featureId = UUID.randomUUID()

        var formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, featureId, 0)
        piperTest.setup(FormTemplateApi.Post(FormTemplateRepFixtures.exampleFormFixture.creation(featureId)))

        val formTemplateQuestionRep = FormTemplateQuestionRepFixtures.textFixture.complete(this, 5)
        formTemplateRep = formTemplateRep.copy(
            questions = listOf(formTemplateQuestionRep).plus(formTemplateRep.questions)
        )
        piperTest.test(
            endpoint = FormTemplateQuestionApi.Post(
                formTemplateId = formTemplateRep.id,
                rank = 0,
                rep = FormTemplateQuestionRepFixtures.textFixture.creation()
            )
        ) {
            val actual = json.parse<FormTemplateQuestionRep.Complete>(response.content!!)
            assertEquals(formTemplateQuestionRep, actual)
        }

        piperTest.test(FormTemplateApi.Get(formTemplateRep.id)) {
            val actual = json.parse<FormTemplateRep.Complete>(response.content!!)
            assertEquals(formTemplateRep, actual)
        }
    }

    @Test
    fun happyPathLastRank() {

        val featureId = UUID.randomUUID()

        var formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, featureId, 0)
        piperTest.setup(FormTemplateApi.Post(FormTemplateRepFixtures.exampleFormFixture.creation(featureId)))

        val formTemplateQuestionRep = FormTemplateQuestionRepFixtures.textFixture.complete(this, 5)
        formTemplateRep = formTemplateRep.copy(
            questions = formTemplateRep.questions.plus(formTemplateQuestionRep)
        )
        piperTest.test(
            endpoint = FormTemplateQuestionApi.Post(
                formTemplateId = formTemplateRep.id,
                rep = FormTemplateQuestionRepFixtures.textFixture.creation()
            )
        ) {
            val actual = json.parse<FormTemplateQuestionRep.Complete>(response.content!!)
            assertEquals(formTemplateQuestionRep, actual)
        }

        piperTest.test(FormTemplateApi.Get(formTemplateRep.id)) {
            val actual = json.parse<FormTemplateRep.Complete>(response.content!!)
            assertEquals(formTemplateRep, actual)
        }
    }
}
