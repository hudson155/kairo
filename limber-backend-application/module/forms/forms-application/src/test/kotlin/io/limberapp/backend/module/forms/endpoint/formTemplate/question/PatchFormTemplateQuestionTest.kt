package io.limberapp.backend.module.forms.endpoint.formTemplate.question

import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.backend.module.forms.api.formTemplate.question.FormTemplateQuestionApi
import io.limberapp.backend.module.forms.exception.formTemplate.FormTemplateNotFound
import io.limberapp.backend.module.forms.exception.formTemplate.FormTemplateQuestionNotFound
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion.FormTemplateTextQuestionRep
import io.limberapp.backend.module.forms.testing.ResourceTest
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateQuestionRepFixtures
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

internal class PatchFormTemplateQuestionTest : ResourceTest() {
  @Test
  fun formTemplateDoesNotExist() {
    val formTemplateGuid = UUID.randomUUID()
    val questionGuid = UUID.randomUUID()

    val formTemplateQuestionUpdateRep = FormTemplateTextQuestionRep.Update("Renamed Question")
    piperTest.test(
      endpoint = FormTemplateQuestionApi.Patch(formTemplateGuid, questionGuid, formTemplateQuestionUpdateRep),
      expectedException = FormTemplateNotFound()
    )
  }

  @Test
  fun formTemplateQuestionDoesNotExist() {
    val featureGuid = UUID.randomUUID()
    val questionGuid = UUID.randomUUID()

    val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, featureGuid, 0)
    piperTest.setup(FormTemplateApi.Post(FormTemplateRepFixtures.exampleFormFixture.creation(featureGuid)))

    val formTemplateQuestionUpdateRep = FormTemplateTextQuestionRep.Update("Renamed Question")
    piperTest.test(
      endpoint = FormTemplateQuestionApi.Patch(formTemplateRep.guid, questionGuid, formTemplateQuestionUpdateRep),
      expectedException = FormTemplateQuestionNotFound()
    )
  }

  @Test
  fun happyPath() {
    val featureGuid = UUID.randomUUID()

    var formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, featureGuid, 0)
    piperTest.setup(FormTemplateApi.Post(FormTemplateRepFixtures.exampleFormFixture.creation(featureGuid)))

    var formTemplateQuestionRep = FormTemplateQuestionRepFixtures.textFixture.complete(this, 5)
      as FormTemplateTextQuestionRep.Complete
    formTemplateRep = formTemplateRep.copy(
      questions = listOf(formTemplateQuestionRep).plus(formTemplateRep.questions)
    )
    piperTest.setup(
      endpoint = FormTemplateQuestionApi.Post(
        formTemplateGuid = formTemplateRep.guid,
        rank = 0,
        rep = FormTemplateQuestionRepFixtures.textFixture.creation()
      )
    )

    val formTemplateQuestionUpdateRep = FormTemplateTextQuestionRep.Update("Renamed Question")
    formTemplateQuestionRep = formTemplateQuestionRep.copy(label = formTemplateQuestionUpdateRep.label!!)
    formTemplateRep = formTemplateRep.copy(
      questions = formTemplateRep.questions.map {
        if (it.guid == formTemplateQuestionRep.guid) formTemplateQuestionRep else it
      }
    )
    piperTest.test(
      endpoint = FormTemplateQuestionApi.Patch(
        formTemplateGuid = formTemplateRep.guid,
        questionGuid = formTemplateQuestionRep.guid,
        rep = formTemplateQuestionUpdateRep
      )
    ) {}

    piperTest.test(FormTemplateApi.Get(formTemplateRep.guid)) {
      val actual = json.parse<FormTemplateRep.Complete>(response.content!!)
      assertEquals(formTemplateRep, actual)
    }
  }
}
