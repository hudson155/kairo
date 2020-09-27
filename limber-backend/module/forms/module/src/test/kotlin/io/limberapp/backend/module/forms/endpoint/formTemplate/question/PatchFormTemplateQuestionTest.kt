package io.limberapp.backend.module.forms.endpoint.formTemplate.question

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.backend.module.forms.api.formTemplate.question.FormTemplateQuestionApi
import io.limberapp.backend.module.forms.exception.formTemplate.FormTemplateQuestionNotFound
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion.FormTemplateTextQuestionRep
import io.limberapp.backend.module.forms.testing.IntegrationTest
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateQuestionRepFixtures
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import io.limberapp.common.LimberApplication
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

internal class PatchFormTemplateQuestionTest(
  engine: TestApplicationEngine,
  limberServer: LimberApplication<*>,
) : IntegrationTest(engine, limberServer) {
  @Test
  fun formTemplateDoesNotExist() {
    val featureGuid = UUID.randomUUID()
    val formTemplateGuid = UUID.randomUUID()
    val questionGuid = UUID.randomUUID()

    test(
      endpoint = FormTemplateQuestionApi.Patch(
        featureGuid = featureGuid,
        formTemplateGuid = formTemplateGuid,
        questionGuid = questionGuid,
        rep = FormTemplateTextQuestionRep.Update("Renamed Question")
      ),
      expectedException = FormTemplateQuestionNotFound()
    )
  }

  @Test
  fun formTemplateQuestionDoesNotExist() {
    val featureGuid = UUID.randomUUID()
    val questionGuid = UUID.randomUUID()

    val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
    setup(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))

    test(
      endpoint = FormTemplateQuestionApi.Patch(
        featureGuid = featureGuid,
        formTemplateGuid = formTemplateRep.guid,
        questionGuid = questionGuid,
        rep = FormTemplateTextQuestionRep.Update("Renamed Question")
      ),
      expectedException = FormTemplateQuestionNotFound()
    )
  }

  @Test
  fun incorrectFeatureGuid() {
    val featureGuid = UUID.randomUUID()

    var formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
    setup(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))

    val formTemplateQuestionRep = FormTemplateQuestionRepFixtures.textFixture.complete(this, 1)
      as FormTemplateTextQuestionRep.Complete
    formTemplateRep = formTemplateRep.copy(
      questions = listOf(formTemplateQuestionRep) + formTemplateRep.questions
    )
    setup(
      endpoint = FormTemplateQuestionApi.Post(
        featureGuid = featureGuid,
        formTemplateGuid = formTemplateRep.guid,
        rank = 0,
        rep = FormTemplateQuestionRepFixtures.textFixture.creation()
      )
    )

    test(
      endpoint = FormTemplateQuestionApi.Patch(
        featureGuid = UUID.randomUUID(),
        formTemplateGuid = formTemplateRep.guid,
        questionGuid = formTemplateQuestionRep.guid,
        rep = FormTemplateTextQuestionRep.Update("Renamed Question")
      ),
      expectedException = FormTemplateQuestionNotFound()
    )

    test(FormTemplateApi.Get(featureGuid, formTemplateRep.guid)) {
      val actual = json.parse<FormTemplateRep.Complete>(responseContent)
      assertEquals(formTemplateRep, actual)
    }
  }

  @Test
  fun incorrectFormTemplateGuid() {
    val featureGuid = UUID.randomUUID()

    var formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
    setup(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))

    val formTemplateQuestionRep = FormTemplateQuestionRepFixtures.textFixture.complete(this, 1)
      as FormTemplateTextQuestionRep.Complete
    formTemplateRep = formTemplateRep.copy(
      questions = listOf(formTemplateQuestionRep) + formTemplateRep.questions
    )
    setup(
      endpoint = FormTemplateQuestionApi.Post(
        featureGuid = featureGuid,
        formTemplateGuid = formTemplateRep.guid,
        rank = 0,
        rep = FormTemplateQuestionRepFixtures.textFixture.creation()
      )
    )

    test(
      endpoint = FormTemplateQuestionApi.Patch(
        featureGuid = featureGuid,
        formTemplateGuid = UUID.randomUUID(),
        questionGuid = formTemplateQuestionRep.guid,
        rep = FormTemplateTextQuestionRep.Update("Renamed Question")
      ),
      expectedException = FormTemplateQuestionNotFound()
    )

    test(FormTemplateApi.Get(featureGuid, formTemplateRep.guid)) {
      val actual = json.parse<FormTemplateRep.Complete>(responseContent)
      assertEquals(formTemplateRep, actual)
    }
  }

  @Test
  fun happyPath() {
    val featureGuid = UUID.randomUUID()

    var formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
    setup(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))

    var formTemplateQuestionRep = FormTemplateQuestionRepFixtures.textFixture.complete(this, 1)
      as FormTemplateTextQuestionRep.Complete
    formTemplateRep = formTemplateRep.copy(
      questions = listOf(formTemplateQuestionRep) + formTemplateRep.questions
    )
    setup(
      endpoint = FormTemplateQuestionApi.Post(
        featureGuid = featureGuid,
        formTemplateGuid = formTemplateRep.guid,
        rank = 0,
        rep = FormTemplateQuestionRepFixtures.textFixture.creation()
      )
    )

    formTemplateQuestionRep = formTemplateQuestionRep.copy(label = "Renamed Question")
    formTemplateRep = formTemplateRep.copy(
      questions = formTemplateRep.questions.map {
        if (it.guid == formTemplateQuestionRep.guid) formTemplateQuestionRep else it
      }
    )
    test(
      endpoint = FormTemplateQuestionApi.Patch(
        featureGuid = featureGuid,
        formTemplateGuid = formTemplateRep.guid,
        questionGuid = formTemplateQuestionRep.guid,
        rep = FormTemplateTextQuestionRep.Update("Renamed Question")
      )
    ) {}

    test(FormTemplateApi.Get(featureGuid, formTemplateRep.guid)) {
      val actual = json.parse<FormTemplateRep.Complete>(responseContent)
      assertEquals(formTemplateRep, actual)
    }
  }
}
