package io.limberapp.backend.module.forms.endpoint.formTemplate.question

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateQuestionApi
import io.limberapp.backend.module.forms.testing.IntegrationTest
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateQuestionRepFixtures
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import io.limberapp.common.LimberApplication
import org.junit.jupiter.api.Test
import java.util.*

internal class DeleteFormTemplateQuestionTest(
    engine: TestApplicationEngine,
    limberServer: LimberApplication<*>,
) : IntegrationTest(engine, limberServer) {
  @Test
  fun formTemplateDoesNotExist() {
    val featureGuid = UUID.randomUUID()
    val formTemplateGuid = UUID.randomUUID()
    val questionGuid = UUID.randomUUID()

    test(expectResult = null) {
      formTemplateQuestionClient(FormTemplateQuestionApi.Delete(featureGuid, formTemplateGuid, questionGuid))
    }
  }

  @Test
  fun formTemplateQuestionDoesNotExist() {
    val featureGuid = UUID.randomUUID()
    val questionGuid = UUID.randomUUID()

    val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
    setup {
      formTemplateClient(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))
    }

    test(expectResult = null) {
      formTemplateQuestionClient(FormTemplateQuestionApi.Delete(featureGuid, formTemplateRep.guid, questionGuid))
    }
  }

  @Test
  fun incorrectFeatureGuid() {
    val featureGuid = UUID.randomUUID()

    var formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
    setup {
      formTemplateClient(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))
    }

    val formTemplateQuestionRep = FormTemplateQuestionRepFixtures.textFixture.complete(this, 1)
    formTemplateRep = formTemplateRep.copy(questions = listOf(formTemplateQuestionRep) + formTemplateRep.questions)
    setup {
      formTemplateQuestionClient(FormTemplateQuestionApi.Post(
          featureGuid = featureGuid,
          formTemplateGuid = formTemplateRep.guid,
          rank = 0,
          rep = FormTemplateQuestionRepFixtures.textFixture.creation()
      ))
    }

    test(expectResult = null) {
      formTemplateQuestionClient(FormTemplateQuestionApi.Delete(
          featureGuid = UUID.randomUUID(),
          formTemplateGuid = formTemplateRep.guid,
          questionGuid = formTemplateQuestionRep.guid
      ))
    }

    test(expectResult = formTemplateRep) {
      formTemplateClient(FormTemplateApi.Get(featureGuid, formTemplateRep.guid))
    }
  }

  @Test
  fun incorrectFormTemplateGuid() {
    val featureGuid = UUID.randomUUID()

    var formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
    setup {
      formTemplateClient(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))
    }

    val formTemplateQuestionRep = FormTemplateQuestionRepFixtures.textFixture.complete(this, 1)
    formTemplateRep = formTemplateRep.copy(questions = listOf(formTemplateQuestionRep) + formTemplateRep.questions)
    setup {
      formTemplateQuestionClient(FormTemplateQuestionApi.Post(
          featureGuid = featureGuid,
          formTemplateGuid = formTemplateRep.guid,
          rank = 0,
          rep = FormTemplateQuestionRepFixtures.textFixture.creation()
      ))
    }

    test(expectResult = null) {
      formTemplateQuestionClient(FormTemplateQuestionApi.Delete(
          featureGuid = featureGuid,
          formTemplateGuid = UUID.randomUUID(),
          questionGuid = formTemplateQuestionRep.guid
      ))
    }

    test(expectResult = formTemplateRep) {
      formTemplateClient(FormTemplateApi.Get(featureGuid, formTemplateRep.guid))
    }
  }

  @Test
  fun happyPath() {
    val featureGuid = UUID.randomUUID()

    var formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
    setup {
      formTemplateClient(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))
    }

    val formTemplateQuestionRep = FormTemplateQuestionRepFixtures.textFixture.complete(this, 1)
    formTemplateRep = formTemplateRep.copy(questions = listOf(formTemplateQuestionRep) + formTemplateRep.questions)
    setup {
      formTemplateQuestionClient(FormTemplateQuestionApi.Post(
          featureGuid = featureGuid,
          formTemplateGuid = formTemplateRep.guid,
          rank = 0,
          rep = FormTemplateQuestionRepFixtures.textFixture.creation()
      ))
    }

    formTemplateRep = formTemplateRep.copy(
        questions = formTemplateRep.questions.filter { it.guid != formTemplateQuestionRep.guid }
    )
    test(expectResult = Unit) {
      formTemplateQuestionClient(FormTemplateQuestionApi.Delete(
          featureGuid = featureGuid,
          formTemplateGuid = formTemplateRep.guid,
          questionGuid = formTemplateQuestionRep.guid
      ))
    }

    test(expectResult = formTemplateRep) {
      formTemplateClient(FormTemplateApi.Get(featureGuid, formTemplateRep.guid))
    }
  }
}
