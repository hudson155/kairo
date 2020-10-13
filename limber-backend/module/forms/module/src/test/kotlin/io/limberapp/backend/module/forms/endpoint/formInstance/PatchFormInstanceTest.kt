package io.limberapp.backend.module.forms.endpoint.formInstance

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.forms.api.formInstance.FormInstanceApi
import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateQuestionApi
import io.limberapp.backend.module.forms.exception.formInstance.CannotReSubmitFormInstance
import io.limberapp.backend.module.forms.exception.formInstance.CannotSubmitFormBeforeAnsweringAllRequiredQuestions
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import io.limberapp.backend.module.forms.rep.formInstance.summary
import io.limberapp.backend.module.forms.testing.IntegrationTest
import io.limberapp.backend.module.forms.testing.fixtures.formInstance.FormInstanceRepFixtures
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateQuestionRepFixtures
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import io.limberapp.common.LimberApplication
import io.limberapp.common.endpoint.exception.ValidationException
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

internal class PatchFormInstanceTest(
    engine: TestApplicationEngine,
    limberServer: LimberApplication<*>,
) : IntegrationTest(engine, limberServer) {
  @Test
  fun doesNotExist() {
    val featureGuid = UUID.randomUUID()
    val formInstanceGuid = UUID.randomUUID()

    test(expectResult = null) {
      formInstanceClient(FormInstanceApi.Patch(featureGuid, formInstanceGuid, FormInstanceRep.Update(submitted = true)))
    }
  }

  @Test
  fun existsInDifferentFeature() {
    val creatorAccountGuid = UUID.randomUUID()
    val feature0Guid = UUID.randomUUID()
    val feature1Guid = UUID.randomUUID()

    val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
    setup {
      formTemplateClient(FormTemplateApi.Post(feature0Guid, FormTemplateRepFixtures.exampleFormFixture.creation()))
    }

    val formInstanceRep = FormInstanceRepFixtures.fixture.complete(this, formTemplateRep.guid, creatorAccountGuid, 1)
    setup {
      formInstanceClient(FormInstanceApi.Post(
          featureGuid = feature0Guid,
          rep = FormInstanceRepFixtures.fixture.creation(formTemplateRep.guid, creatorAccountGuid)
      ))
    }

    test(expectResult = null) {
      formInstanceClient(FormInstanceApi.Patch(featureGuid = feature1Guid,
          formInstanceGuid = formInstanceRep.guid,
          rep = FormInstanceRep.Update(submitted = true)
      ))
    }

    test(expectResult = formInstanceRep) {
      formInstanceClient(FormInstanceApi.Get(feature0Guid, formInstanceRep.guid))
    }
  }

  @Test
  fun cannotUnsubmitForm() {
    val featureGuid = UUID.randomUUID()
    val formInstanceGuid = UUID.randomUUID()

    test(expectError = ValidationException(FormInstanceRep.Update::submitted.name)) {
      formInstanceClient(FormInstanceApi.Patch(
          featureGuid = featureGuid,
          formInstanceGuid = formInstanceGuid,
          rep = FormInstanceRep.Update(submitted = false)
      ))
    }
  }

  @Test
  fun notAllRequiredQuestionsAnswered() {
    val creatorAccountGuid = UUID.randomUUID()
    val featureGuid = UUID.randomUUID()

    var formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
    setup {
      formTemplateClient(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))
    }

    val formTemplateQuestionRep = FormTemplateQuestionRepFixtures.textFixture.complete(this, 1)
    formTemplateRep = formTemplateRep.copy(questions = formTemplateRep.questions + formTemplateQuestionRep)
    setup {
      formTemplateQuestionClient(FormTemplateQuestionApi.Post(
          featureGuid = featureGuid,
          formTemplateGuid = formTemplateRep.guid,
          rep = FormTemplateQuestionRepFixtures.textFixture.creation(),
      ))
    }

    val formInstanceRep = FormInstanceRepFixtures.fixture.complete(this, formTemplateRep.guid, creatorAccountGuid, 2)
    setup {
      formInstanceClient(FormInstanceApi.Post(
          featureGuid = featureGuid,
          rep = FormInstanceRepFixtures.fixture.creation(formTemplateRep.guid, creatorAccountGuid)
      ))
    }

    test(expectError = CannotSubmitFormBeforeAnsweringAllRequiredQuestions()) {
      formInstanceClient(FormInstanceApi.Patch(
          featureGuid = featureGuid,
          formInstanceGuid = formInstanceRep.guid,
          rep = FormInstanceRep.Update(submitted = true)
      ))
    }

    test(expectResult = formInstanceRep) {
      formInstanceClient(FormInstanceApi.Get(featureGuid, formInstanceRep.guid))
    }
  }

  @Test
  fun alreadySubmitted() {
    val creatorAccountGuid = UUID.randomUUID()
    val featureGuid = UUID.randomUUID()

    val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
    setup {
      formTemplateClient(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))
    }

    var formInstanceRep = FormInstanceRepFixtures.fixture.complete(this, formTemplateRep.guid, creatorAccountGuid, 1)
    setup {
      formInstanceClient(FormInstanceApi.Post(
          featureGuid = featureGuid,
          rep = FormInstanceRepFixtures.fixture.creation(formTemplateRep.guid, creatorAccountGuid)
      ))
    }

    formInstanceRep = formInstanceRep.copy(number = 1, submittedDate = LocalDateTime.now(clock))
    setup {
      formInstanceClient(FormInstanceApi.Patch(
          featureGuid = featureGuid,
          formInstanceGuid = formInstanceRep.guid,
          rep = FormInstanceRep.Update(submitted = true)
      ))
    }

    test(expectError = CannotReSubmitFormInstance()) {
      formInstanceClient(FormInstanceApi.Patch(
          featureGuid = featureGuid,
          formInstanceGuid = formInstanceRep.guid,
          rep = FormInstanceRep.Update(submitted = true)
      ))
    }

    test(expectResult = formInstanceRep) {
      formInstanceClient(FormInstanceApi.Get(featureGuid, formInstanceRep.guid))
    }
  }

  @Test
  fun happyPath() {
    val creatorAccountGuid = UUID.randomUUID()
    val featureGuid = UUID.randomUUID()

    val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
    setup {
      formTemplateClient(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))
    }

    var formInstanceRep = FormInstanceRepFixtures.fixture.complete(this, formTemplateRep.guid, creatorAccountGuid, 1)
    setup {
      formInstanceClient(FormInstanceApi.Post(
          featureGuid = featureGuid,
          rep = FormInstanceRepFixtures.fixture.creation(formTemplateRep.guid, creatorAccountGuid)
      ))
    }

    formInstanceRep = formInstanceRep.copy(number = 1, submittedDate = LocalDateTime.now(clock))
    test(expectResult = formInstanceRep.summary()) {
      formInstanceClient(FormInstanceApi.Patch(
          featureGuid = featureGuid,
          formInstanceGuid = formInstanceRep.guid,
          rep = FormInstanceRep.Update(submitted = true)
      ))
    }

    test(expectResult = formInstanceRep) {
      formInstanceClient(FormInstanceApi.Get(featureGuid, formInstanceRep.guid))
    }
  }
}
