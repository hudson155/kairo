package io.limberapp.backend.module.forms.endpoint.formTemplate.question

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateQuestionApi
import io.limberapp.backend.module.forms.exception.formTemplate.FormTemplateNotFound
import io.limberapp.backend.module.forms.testing.IntegrationTest
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateQuestionRepFixtures
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import io.limberapp.common.LimberApplication
import io.limberapp.exception.badRequest.RankOutOfBounds
import io.limberapp.exception.unprocessableEntity.unprocessable
import org.junit.jupiter.api.Test
import java.util.*

internal class PostFormTemplateQuestionTest(
    engine: TestApplicationEngine,
    limberServer: LimberApplication<*>,
) : IntegrationTest(engine, limberServer) {
  @Test
  fun formTemplateDoesNotExist() {
    val featureGuid = UUID.randomUUID()
    val formTemplateGuid = UUID.randomUUID()

    test(expectError = FormTemplateNotFound().unprocessable()) {
      formTemplateQuestionClient(FormTemplateQuestionApi.Post(
          featureGuid = featureGuid,
          formTemplateGuid = formTemplateGuid,
          rep = FormTemplateQuestionRepFixtures.textFixture.creation()
      ))
    }
  }

  @Test
  fun incorrectFeatureGuid() {
    val featureGuid = UUID.randomUUID()

    val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
    setup {
      formTemplateClient(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))
    }

    test(expectError = FormTemplateNotFound().unprocessable()) {
      formTemplateQuestionClient(FormTemplateQuestionApi.Post(
          featureGuid = UUID.randomUUID(),
          formTemplateGuid = formTemplateRep.guid,
          rep = FormTemplateQuestionRepFixtures.textFixture.creation()
      ))
    }

    test(expectResult = formTemplateRep) {
      formTemplateClient(FormTemplateApi.Get(featureGuid, formTemplateRep.guid))
    }
  }

  @Test
  fun rankOutOfBoundsLow() {
    val featureGuid = UUID.randomUUID()

    val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
    setup {
      formTemplateClient(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))
    }

    test(expectError = RankOutOfBounds(-1)) {
      formTemplateQuestionClient(FormTemplateQuestionApi.Post(
          featureGuid = featureGuid,
          formTemplateGuid = formTemplateRep.guid,
          rank = -1,
          rep = FormTemplateQuestionRepFixtures.textFixture.creation()
      ))
    }
  }

  @Test
  fun rankOutOfBoundsHigh() {
    val featureGuid = UUID.randomUUID()

    val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
    setup {
      formTemplateClient(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))
    }

    test(expectError = RankOutOfBounds(1)) {
      formTemplateQuestionClient(FormTemplateQuestionApi.Post(
          featureGuid = featureGuid,
          formTemplateGuid = formTemplateRep.guid,
          rank = 1,
          rep = FormTemplateQuestionRepFixtures.textFixture.creation()
      ))
    }
  }

  @Test
  fun happyPathFirstRank() {
    val featureGuid = UUID.randomUUID()

    var formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
    setup {
      formTemplateClient(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))
    }

    val formTemplateQuestionRep = FormTemplateQuestionRepFixtures.textFixture.complete(this, 1)
    formTemplateRep = formTemplateRep.copy(
        questions = listOf(formTemplateQuestionRep) + formTemplateRep.questions
    )
    test(expectResult = formTemplateQuestionRep) {
      formTemplateQuestionClient(FormTemplateQuestionApi.Post(
          featureGuid = featureGuid,
          formTemplateGuid = formTemplateRep.guid,
          rank = 0,
          rep = FormTemplateQuestionRepFixtures.textFixture.creation()
      ))
    }

    test(expectResult = formTemplateRep) {
      formTemplateClient(FormTemplateApi.Get(featureGuid, formTemplateRep.guid))
    }
  }

  @Test
  fun happyPathLastRank() {
    val featureGuid = UUID.randomUUID()

    var formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
    setup {
      formTemplateClient(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))
    }

    val formTemplateQuestionRep = FormTemplateQuestionRepFixtures.textFixture.complete(this, 1)
    formTemplateRep = formTemplateRep.copy(
        questions = formTemplateRep.questions + formTemplateQuestionRep
    )
    test(expectResult = formTemplateQuestionRep) {
      formTemplateQuestionClient(FormTemplateQuestionApi.Post(
          featureGuid = featureGuid,
          formTemplateGuid = formTemplateRep.guid,
          rep = FormTemplateQuestionRepFixtures.textFixture.creation()
      ))
    }

    test(expectResult = formTemplateRep) {
      formTemplateClient(FormTemplateApi.Get(featureGuid, formTemplateRep.guid))
    }
  }
}
