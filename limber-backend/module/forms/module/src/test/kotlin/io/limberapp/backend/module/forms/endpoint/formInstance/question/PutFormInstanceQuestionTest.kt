package io.limberapp.backend.module.forms.endpoint.formInstance.question

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.forms.api.formInstance.FormInstanceApi
import io.limberapp.backend.module.forms.api.formInstance.question.FormInstanceQuestionApi
import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.backend.module.forms.api.formTemplate.question.FormTemplateQuestionApi
import io.limberapp.backend.module.forms.exception.formInstance.FormInstanceQuestionNotFound
import io.limberapp.backend.module.forms.exception.formTemplate.FormTemplateQuestionNotFound
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceQuestionRep
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion.FormInstanceTextQuestionRep
import io.limberapp.backend.module.forms.testing.IntegrationTest
import io.limberapp.backend.module.forms.testing.fixtures.formInstance.FormInstanceQuestionRepFixtures
import io.limberapp.backend.module.forms.testing.fixtures.formInstance.FormInstanceRepFixtures
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateQuestionRepFixtures
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import io.limberapp.common.LimberApplication
import io.limberapp.exception.unprocessableEntity.unprocessable
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

internal class PutFormInstanceQuestionTest(
  engine: TestApplicationEngine,
  limberServer: LimberApplication<*>,
) : IntegrationTest(engine, limberServer) {
  @Test
  fun formInstanceDoesNotExist() {
    val featureGuid = UUID.randomUUID()
    val formInstanceGuid = UUID.randomUUID()

    setup(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))

    test(
      endpoint = FormInstanceQuestionApi.Put(
        featureGuid = featureGuid,
        formInstanceGuid = formInstanceGuid,
        questionGuid = UUID.randomUUID(),
        rep = FormInstanceQuestionRepFixtures.textFixture.creation(this)
      ),
      expectedException = FormInstanceQuestionNotFound()
    )
  }

  @Test
  fun formTemplateQuestionDoesNotExist() {
    val creatorAccountGuid = UUID.randomUUID()
    val featureGuid = UUID.randomUUID()

    val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
    setup(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))

    val formInstanceRep = FormInstanceRepFixtures.fixture.complete(this, formTemplateRep.guid, creatorAccountGuid, 1)
    setup(
      endpoint = FormInstanceApi.Post(
        featureGuid = featureGuid,
        rep = FormInstanceRepFixtures.fixture.creation(formTemplateRep.guid, creatorAccountGuid)
      )
    )

    test(
      endpoint = FormInstanceQuestionApi.Put(
        featureGuid = featureGuid,
        formInstanceGuid = formInstanceRep.guid,
        questionGuid = UUID.randomUUID(),
        rep = FormInstanceQuestionRepFixtures.textFixture.creation(this)
      ),
      expectedException = FormTemplateQuestionNotFound().unprocessable(),
    )
  }

  @Test
  fun incorrectFeatureGuid() {
    val creatorAccountGuid = UUID.randomUUID()
    val featureGuid = UUID.randomUUID()

    var formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
    setup(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))

    val formTemplateQuestionRep = FormTemplateQuestionRepFixtures.textFixture.complete(this, 1)
    formTemplateRep = formTemplateRep.copy(questions = formTemplateRep.questions + formTemplateQuestionRep)
    setup(
      endpoint = FormTemplateQuestionApi.Post(
        featureGuid = featureGuid,
        formTemplateGuid = formTemplateRep.guid,
        rep = FormTemplateQuestionRepFixtures.textFixture.creation(),
      )
    )

    val formInstanceRep = FormInstanceRepFixtures.fixture.complete(this, formTemplateRep.guid, creatorAccountGuid, 2)
    setup(
      endpoint = FormInstanceApi.Post(
        featureGuid = featureGuid,
        rep = FormInstanceRepFixtures.fixture.creation(formTemplateRep.guid, creatorAccountGuid)
      )
    )

    test(
      endpoint = FormInstanceQuestionApi.Put(
        featureGuid = UUID.randomUUID(),
        formInstanceGuid = formInstanceRep.guid,
        questionGuid = formTemplateQuestionRep.guid,
        rep = FormInstanceQuestionRepFixtures.textFixture.creation(this)
      ),
      expectedException = FormInstanceQuestionNotFound()
    )

    test(FormInstanceApi.Get(featureGuid, formInstanceRep.guid)) {
      val actual = json.parse<FormInstanceRep.Complete>(responseContent)
      assertEquals(formInstanceRep, actual)
    }
  }

  @Test
  fun incorrectFormInstanceGuid() {
    val creatorAccountGuid = UUID.randomUUID()
    val featureGuid = UUID.randomUUID()

    var formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
    setup(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))

    val formTemplateQuestionRep = FormTemplateQuestionRepFixtures.textFixture.complete(this, 1)
    formTemplateRep = formTemplateRep.copy(questions = formTemplateRep.questions + formTemplateQuestionRep)
    setup(
      endpoint = FormTemplateQuestionApi.Post(
        featureGuid = featureGuid,
        formTemplateGuid = formTemplateRep.guid,
        rep = FormTemplateQuestionRepFixtures.textFixture.creation(),
      )
    )

    val formInstanceRep = FormInstanceRepFixtures.fixture.complete(this, formTemplateRep.guid, creatorAccountGuid, 2)
    setup(
      endpoint = FormInstanceApi.Post(
        featureGuid = featureGuid,
        rep = FormInstanceRepFixtures.fixture.creation(formTemplateRep.guid, creatorAccountGuid)
      )
    )

    test(
      endpoint = FormInstanceQuestionApi.Put(
        featureGuid = featureGuid,
        formInstanceGuid = UUID.randomUUID(),
        questionGuid = formTemplateQuestionRep.guid,
        rep = FormInstanceQuestionRepFixtures.textFixture.creation(this)
      ),
      expectedException = FormInstanceQuestionNotFound()
    )

    test(FormInstanceApi.Get(featureGuid, formInstanceRep.guid)) {
      val actual = json.parse<FormInstanceRep.Complete>(responseContent)
      assertEquals(formInstanceRep, actual)
    }
  }

  @Test
  fun happyPath() {
    val creatorAccountGuid = UUID.randomUUID()
    val featureGuid = UUID.randomUUID()

    var formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
    setup(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))

    val formTemplateQuestionRep = FormTemplateQuestionRepFixtures.textFixture.complete(this, 1)
    formTemplateRep = formTemplateRep.copy(questions = formTemplateRep.questions + formTemplateQuestionRep)
    setup(
      endpoint = FormTemplateQuestionApi.Post(
        featureGuid = featureGuid,
        formTemplateGuid = formTemplateRep.guid,
        rep = FormTemplateQuestionRepFixtures.textFixture.creation(),
      )
    )

    var formInstanceRep = FormInstanceRepFixtures.fixture.complete(this, formTemplateRep.guid, creatorAccountGuid, 2)
    setup(
      endpoint = FormInstanceApi.Post(
        featureGuid = featureGuid,
        rep = FormInstanceRepFixtures.fixture.creation(formTemplateRep.guid, creatorAccountGuid)
      )
    )

    val formInstanceQuestionRep =
      FormInstanceQuestionRepFixtures.textFixture.complete(this, formTemplateQuestionRep.guid)
    formInstanceRep = formInstanceRep.copy(questions = formInstanceRep.questions + formInstanceQuestionRep)
    test(
      endpoint = FormInstanceQuestionApi.Put(
        featureGuid = featureGuid,
        formInstanceGuid = formInstanceRep.guid,
        questionGuid = formTemplateQuestionRep.guid,
        rep = FormInstanceQuestionRepFixtures.textFixture.creation(this)
      )
    ) {
      val actual = json.parse<FormInstanceQuestionRep.Complete>(responseContent)
      assertEquals(formInstanceQuestionRep, actual)
    }

    test(FormInstanceApi.Get(featureGuid, formInstanceRep.guid)) {
      val actual = json.parse<FormInstanceRep.Complete>(responseContent)
      assertEquals(formInstanceRep, actual)
    }
  }

  @Test
  fun happyPathIdempotent() {
    val creatorAccountGuid = UUID.randomUUID()
    val featureGuid = UUID.randomUUID()

    var formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
    setup(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))

    val formTemplateQuestionRep = FormTemplateQuestionRepFixtures.textFixture.complete(this, 1)
    formTemplateRep = formTemplateRep.copy(questions = formTemplateRep.questions + formTemplateQuestionRep)
    setup(
      endpoint = FormTemplateQuestionApi.Post(
        featureGuid = featureGuid,
        formTemplateGuid = formTemplateRep.guid,
        rep = FormTemplateQuestionRepFixtures.textFixture.creation(),
      )
    )

    var formInstanceRep = FormInstanceRepFixtures.fixture.complete(this, formTemplateRep.guid, creatorAccountGuid, 2)
    setup(
      endpoint = FormInstanceApi.Post(
        featureGuid = featureGuid,
        rep = FormInstanceRepFixtures.fixture.creation(formTemplateRep.guid, creatorAccountGuid)
      )
    )

    val formInstanceQuestion0Rep =
      FormInstanceQuestionRepFixtures.textFixture.complete(this, formTemplateQuestionRep.guid)
    formInstanceRep = formInstanceRep.copy(questions = formInstanceRep.questions + formInstanceQuestion0Rep)
    setup(
      endpoint = FormInstanceQuestionApi.Put(
        featureGuid = featureGuid,
        formInstanceGuid = formInstanceRep.guid,
        questionGuid = formTemplateQuestionRep.guid,
        rep = FormInstanceQuestionRepFixtures.textFixture.creation(this)
      )
    )

    val formInstanceQuestion1Rep =
      (FormInstanceQuestionRepFixtures.textFixture.complete(this, formTemplateQuestionRep.guid)
        as FormInstanceTextQuestionRep.Complete)
        .copy(text = "completely new text")
    formInstanceRep = formInstanceRep.copy(questions = formInstanceRep.questions - formInstanceQuestion0Rep)
    formInstanceRep = formInstanceRep.copy(questions = formInstanceRep.questions + formInstanceQuestion1Rep)
    test(
      endpoint = FormInstanceQuestionApi.Put(
        featureGuid = featureGuid,
        formInstanceGuid = formInstanceRep.guid,
        questionGuid = formTemplateQuestionRep.guid,
        rep = (FormInstanceQuestionRepFixtures.textFixture.creation(this) as FormInstanceTextQuestionRep.Creation)
          .copy(text = "completely new text")
      )
    ) {
      val actual = json.parse<FormInstanceQuestionRep.Complete>(responseContent)
      assertEquals(formInstanceQuestion1Rep, actual)
    }

    test(FormInstanceApi.Get(featureGuid, formInstanceRep.guid)) {
      val actual = json.parse<FormInstanceRep.Complete>(responseContent)
      assertEquals(formInstanceRep, actual)
    }
  }
}
