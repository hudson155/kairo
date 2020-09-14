package io.limberapp.backend.module.forms.endpoint.formInstance

import io.limberapp.backend.module.forms.api.formInstance.FormInstanceApi
import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.backend.module.forms.api.formTemplate.question.FormTemplateQuestionApi
import io.limberapp.backend.module.forms.exception.formInstance.CannotReSubmitFormInstance
import io.limberapp.backend.module.forms.exception.formInstance.CannotSubmitFormBeforeAnsweringAllRequiredQuestions
import io.limberapp.backend.module.forms.exception.formInstance.FormInstanceNotFound
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import io.limberapp.backend.module.forms.testing.ResourceTest
import io.limberapp.backend.module.forms.testing.fixtures.formInstance.FormInstanceRepFixtures
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateQuestionRepFixtures
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import io.limberapp.common.endpoint.exception.ValidationException
import io.limberapp.common.testing.responseContent
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*
import kotlin.test.assertEquals

internal class PatchFormInstanceTest : ResourceTest() {
  @Test
  fun doesNotExist() {
    val featureGuid = UUID.randomUUID()
    val formInstanceGuid = UUID.randomUUID()

    limberTest.test(
      endpoint = FormInstanceApi.Patch(featureGuid, formInstanceGuid, FormInstanceRep.Update(submitted = true)),
      expectedException = FormInstanceNotFound()
    )
  }

  @Test
  fun existsInDifferentFeature() {
    val creatorAccountGuid = UUID.randomUUID()
    val feature0Guid = UUID.randomUUID()
    val feature1Guid = UUID.randomUUID()

    val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
    limberTest.setup(FormTemplateApi.Post(feature0Guid, FormTemplateRepFixtures.exampleFormFixture.creation()))

    val formInstanceRep = FormInstanceRepFixtures.fixture.complete(this, formTemplateRep.guid, 1, creatorAccountGuid, 1)
    limberTest.setup(
      endpoint = FormInstanceApi.Post(
        featureGuid = feature0Guid,
        rep = FormInstanceRepFixtures.fixture.creation(formTemplateRep.guid, creatorAccountGuid)
      )
    )

    limberTest.test(
      endpoint = FormInstanceApi.Patch(feature1Guid, formInstanceRep.guid, FormInstanceRep.Update(submitted = true)),
      expectedException = FormInstanceNotFound()
    )

    limberTest.test(FormInstanceApi.Get(feature0Guid, formInstanceRep.guid)) {
      val actual = json.parse<FormInstanceRep.Complete>(responseContent)
      assertEquals(formInstanceRep, actual)
    }
  }

  @Test
  fun cannotUnsubmitForm() {
    val featureGuid = UUID.randomUUID()
    val formInstanceGuid = UUID.randomUUID()

    limberTest.test(
      endpoint = FormInstanceApi.Patch(featureGuid, formInstanceGuid, FormInstanceRep.Update(submitted = false)),
      expectedException = ValidationException(FormInstanceRep.Update::submitted.name)
    )
  }

  @Test
  fun notAllRequiredQuestionsAnswered() {
    val creatorAccountGuid = UUID.randomUUID()
    val featureGuid = UUID.randomUUID()

    var formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
    limberTest.setup(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))

    val formTemplateQuestionRep = FormTemplateQuestionRepFixtures.textFixture.complete(this, 1)
    formTemplateRep = formTemplateRep.copy(questions = formTemplateRep.questions + formTemplateQuestionRep)
    limberTest.setup(
      endpoint = FormTemplateQuestionApi.Post(
        featureGuid = featureGuid,
        formTemplateGuid = formTemplateRep.guid,
        rep = FormTemplateQuestionRepFixtures.textFixture.creation(),
      )
    )

    val formInstanceRep = FormInstanceRepFixtures.fixture.complete(this, formTemplateRep.guid, 1, creatorAccountGuid, 2)
    limberTest.setup(
      endpoint = FormInstanceApi.Post(
        featureGuid = featureGuid,
        rep = FormInstanceRepFixtures.fixture.creation(formTemplateRep.guid, creatorAccountGuid)
      )
    )

    limberTest.test(
      endpoint = FormInstanceApi.Patch(featureGuid, formInstanceRep.guid, FormInstanceRep.Update(submitted = true)),
      expectedException = CannotSubmitFormBeforeAnsweringAllRequiredQuestions()
    )

    limberTest.test(FormInstanceApi.Get(featureGuid, formInstanceRep.guid)) {
      val actual = json.parse<FormInstanceRep.Complete>(responseContent)
      assertEquals(formInstanceRep, actual)
    }
  }

  @Test
  fun alreadySubmitted() {
    val creatorAccountGuid = UUID.randomUUID()
    val featureGuid = UUID.randomUUID()

    val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
    limberTest.setup(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))

    var formInstanceRep = FormInstanceRepFixtures.fixture.complete(this, formTemplateRep.guid, 1, creatorAccountGuid, 1)
    limberTest.setup(
      endpoint = FormInstanceApi.Post(
        featureGuid = featureGuid,
        rep = FormInstanceRepFixtures.fixture.creation(formTemplateRep.guid, creatorAccountGuid)
      )
    )

    formInstanceRep = formInstanceRep.copy(submittedDate = LocalDateTime.now(fixedClock))
    limberTest.setup(FormInstanceApi.Patch(featureGuid, formInstanceRep.guid, FormInstanceRep.Update(submitted = true)))

    limberTest.test(
      endpoint = FormInstanceApi.Patch(featureGuid, formInstanceRep.guid, FormInstanceRep.Update(submitted = true)),
      expectedException = CannotReSubmitFormInstance()
    )

    limberTest.test(FormInstanceApi.Get(featureGuid, formInstanceRep.guid)) {
      val actual = json.parse<FormInstanceRep.Complete>(responseContent)
      assertEquals(formInstanceRep, actual)
    }
  }

  @Test
  fun happyPath() {
    val creatorAccountGuid = UUID.randomUUID()
    val featureGuid = UUID.randomUUID()

    val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
    limberTest.setup(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))

    var formInstanceRep = FormInstanceRepFixtures.fixture.complete(this, formTemplateRep.guid, 1, creatorAccountGuid, 1)
    limberTest.setup(
      endpoint = FormInstanceApi.Post(
        featureGuid = featureGuid,
        rep = FormInstanceRepFixtures.fixture.creation(formTemplateRep.guid, creatorAccountGuid)
      )
    )

    formInstanceRep = formInstanceRep.copy(submittedDate = LocalDateTime.now(fixedClock))
    limberTest.test(
      endpoint = FormInstanceApi.Patch(
        featureGuid = featureGuid,
        formInstanceGuid = formInstanceRep.guid,
        rep = FormInstanceRep.Update(submitted = true)
      ),
    ) {
      val actual = json.parse<FormInstanceRep.Complete>(responseContent)
      assertEquals(formInstanceRep, actual)
    }

    limberTest.test(FormInstanceApi.Get(featureGuid, formInstanceRep.guid)) {
      val actual = json.parse<FormInstanceRep.Complete>(responseContent)
      assertEquals(formInstanceRep, actual)
    }
  }
}
