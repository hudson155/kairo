package io.limberapp.backend.module.forms.endpoint.formTemplate.question

import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.backend.module.forms.api.formTemplate.question.FormTemplateQuestionApi
import io.limberapp.backend.module.forms.exception.formTemplate.FormTemplateNotFound
import io.limberapp.backend.module.forms.exception.formTemplate.FormTemplateQuestionNotFound
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.backend.module.forms.testing.ResourceTest
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateQuestionRepFixtures
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

internal class DeleteFormTemplateQuestionTest : ResourceTest() {
  @Test
  fun formTemplateDoesNotExist() {
    val formTemplateGuid = UUID.randomUUID()
    val questionGuid = UUID.randomUUID()

    piperTest.test(
      endpoint = FormTemplateQuestionApi.Delete(formTemplateGuid, questionGuid),
      expectedException = FormTemplateNotFound()
    )
  }

  @Test
  fun formTemplateQuestionDoesNotExist() {
    val featureGuid = UUID.randomUUID()
    val questionGuid = UUID.randomUUID()

    val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, featureGuid, 0)
    piperTest.setup(FormTemplateApi.Post(FormTemplateRepFixtures.exampleFormFixture.creation(featureGuid)))

    piperTest.test(
      endpoint = FormTemplateQuestionApi.Delete(formTemplateRep.guid, questionGuid),
      expectedException = FormTemplateQuestionNotFound()
    )
  }

  @Test
  fun happyPath() {
    val featureGuid = UUID.randomUUID()

    var formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, featureGuid, 0)
    piperTest.setup(FormTemplateApi.Post(FormTemplateRepFixtures.exampleFormFixture.creation(featureGuid)))

    val formTemplateQuestionRep = FormTemplateQuestionRepFixtures.textFixture.complete(this, 5)
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

    formTemplateRep = formTemplateRep.copy(
      questions = formTemplateRep.questions.filter { it.guid != formTemplateQuestionRep.guid }
    )
    piperTest.test(FormTemplateQuestionApi.Delete(formTemplateRep.guid, formTemplateQuestionRep.guid)) {}

    piperTest.test(FormTemplateApi.Get(formTemplateRep.guid)) {
      val actual = json.parse<FormTemplateRep.Complete>(response.content!!)
      assertEquals(formTemplateRep, actual)
    }
  }
}
