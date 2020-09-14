package io.limberapp.backend.module.forms.endpoint.formTemplate.question

import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.backend.module.forms.api.formTemplate.question.FormTemplateQuestionApi
import io.limberapp.backend.module.forms.exception.formTemplate.FormTemplateQuestionNotFound
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.backend.module.forms.testing.ResourceTest
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateQuestionRepFixtures
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import io.limberapp.common.testing.responseContent
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

internal class DeleteFormTemplateQuestionTest : ResourceTest() {
  @Test
  fun formTemplateDoesNotExist() {
    val featureGuid = UUID.randomUUID()
    val formTemplateGuid = UUID.randomUUID()
    val questionGuid = UUID.randomUUID()

    limberTest.test(
      endpoint = FormTemplateQuestionApi.Delete(featureGuid, formTemplateGuid, questionGuid),
      expectedException = FormTemplateQuestionNotFound()
    )
  }

  @Test
  fun formTemplateQuestionDoesNotExist() {
    val featureGuid = UUID.randomUUID()
    val questionGuid = UUID.randomUUID()

    val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
    limberTest.setup(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))

    limberTest.test(
      endpoint = FormTemplateQuestionApi.Delete(featureGuid, formTemplateRep.guid, questionGuid),
      expectedException = FormTemplateQuestionNotFound()
    )
  }

  @Test
  fun incorrectFeatureGuid() {
    val featureGuid = UUID.randomUUID()

    var formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
    limberTest.setup(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))

    val formTemplateQuestionRep = FormTemplateQuestionRepFixtures.textFixture.complete(this, 1)
    formTemplateRep = formTemplateRep.copy(questions = listOf(formTemplateQuestionRep) + formTemplateRep.questions)
    limberTest.setup(
      endpoint = FormTemplateQuestionApi.Post(
        featureGuid = featureGuid,
        formTemplateGuid = formTemplateRep.guid,
        rank = 0,
        rep = FormTemplateQuestionRepFixtures.textFixture.creation()
      )
    )

    limberTest.test(
      endpoint = FormTemplateQuestionApi.Delete(UUID.randomUUID(), formTemplateRep.guid, formTemplateQuestionRep.guid),
      expectedException = FormTemplateQuestionNotFound()
    )

    limberTest.test(FormTemplateApi.Get(featureGuid, formTemplateRep.guid)) {
      val actual = json.parse<FormTemplateRep.Complete>(responseContent)
      assertEquals(formTemplateRep, actual)
    }
  }

  @Test
  fun incorrectFormTemplateGuid() {
    val featureGuid = UUID.randomUUID()

    var formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
    limberTest.setup(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))

    val formTemplateQuestionRep = FormTemplateQuestionRepFixtures.textFixture.complete(this, 1)
    formTemplateRep = formTemplateRep.copy(questions = listOf(formTemplateQuestionRep) + formTemplateRep.questions)
    limberTest.setup(
      endpoint = FormTemplateQuestionApi.Post(
        featureGuid = featureGuid,
        formTemplateGuid = formTemplateRep.guid,
        rank = 0,
        rep = FormTemplateQuestionRepFixtures.textFixture.creation()
      )
    )

    limberTest.test(
      endpoint = FormTemplateQuestionApi.Delete(featureGuid, UUID.randomUUID(), formTemplateQuestionRep.guid),
      expectedException = FormTemplateQuestionNotFound()
    )

    limberTest.test(FormTemplateApi.Get(featureGuid, formTemplateRep.guid)) {
      val actual = json.parse<FormTemplateRep.Complete>(responseContent)
      assertEquals(formTemplateRep, actual)
    }
  }

  @Test
  fun happyPath() {
    val featureGuid = UUID.randomUUID()

    var formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
    limberTest.setup(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))

    val formTemplateQuestionRep = FormTemplateQuestionRepFixtures.textFixture.complete(this, 1)
    formTemplateRep = formTemplateRep.copy(questions = listOf(formTemplateQuestionRep) + formTemplateRep.questions)
    limberTest.setup(
      endpoint = FormTemplateQuestionApi.Post(
        featureGuid = featureGuid,
        formTemplateGuid = formTemplateRep.guid,
        rank = 0,
        rep = FormTemplateQuestionRepFixtures.textFixture.creation()
      )
    )

    formTemplateRep = formTemplateRep.copy(
      questions = formTemplateRep.questions.filter { it.guid != formTemplateQuestionRep.guid }
    )
    limberTest.test(FormTemplateQuestionApi.Delete(featureGuid, formTemplateRep.guid, formTemplateQuestionRep.guid)) {}

    limberTest.test(FormTemplateApi.Get(featureGuid, formTemplateRep.guid)) {
      val actual = json.parse<FormTemplateRep.Complete>(responseContent)
      assertEquals(formTemplateRep, actual)
    }
  }
}
