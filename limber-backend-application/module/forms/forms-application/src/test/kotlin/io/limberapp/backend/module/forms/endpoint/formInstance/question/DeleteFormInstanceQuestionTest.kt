package io.limberapp.backend.module.forms.endpoint.formInstance.question

import io.limberapp.backend.module.forms.api.formInstance.FormInstanceApi
import io.limberapp.backend.module.forms.api.formInstance.question.FormInstanceQuestionApi
import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.backend.module.forms.exception.formInstance.FormInstanceQuestionNotFound
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import io.limberapp.backend.module.forms.testing.ResourceTest
import io.limberapp.backend.module.forms.testing.fixtures.formInstance.FormInstanceQuestionRepFixtures
import io.limberapp.backend.module.forms.testing.fixtures.formInstance.FormInstanceRepFixtures
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

internal class DeleteFormInstanceQuestionTest : ResourceTest() {
  @Test
  fun formInstanceDoesNotExist() {
    val featureGuid = UUID.randomUUID()
    val formInstanceGuid = UUID.randomUUID()

    val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
    piperTest.setup(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))

    piperTest.test(
      endpoint = FormInstanceQuestionApi.Delete(featureGuid, formInstanceGuid, formTemplateRep.questions.first().guid),
      expectedException = FormInstanceQuestionNotFound()
    )
  }

  @Test
  fun formInstanceQuestionDoesNotExist() {
    val creatorAccountGuid = UUID.randomUUID()
    val featureGuid = UUID.randomUUID()
    val questionGuid = UUID.randomUUID()

    val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
    piperTest.setup(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))

    var formInstanceRep = FormInstanceRepFixtures.fixture.complete(this, formTemplateRep.guid, 1, creatorAccountGuid, 5)
    piperTest.setup(
      endpoint = FormInstanceApi.Post(
        featureGuid = featureGuid,
        rep = FormInstanceRepFixtures.fixture.creation(formTemplateRep.guid, creatorAccountGuid)
      )
    )

    val formInstanceQuestionRep =
      FormInstanceQuestionRepFixtures.textFixture.complete(this, formTemplateRep.questions.first().guid)
    formInstanceRep = formInstanceRep.copy(questions = formInstanceRep.questions.plus(formInstanceQuestionRep))
    piperTest.setup(
      endpoint = FormInstanceQuestionApi.Put(
        featureGuid = featureGuid,
        formInstanceGuid = formInstanceRep.guid,
        questionGuid = formTemplateRep.questions.first().guid,
        rep = FormInstanceQuestionRepFixtures.textFixture.creation()
      )
    )

    piperTest.test(
      endpoint = FormInstanceQuestionApi.Delete(featureGuid, formInstanceRep.guid, questionGuid),
      expectedException = FormInstanceQuestionNotFound()
    )

    piperTest.test(FormInstanceApi.Get(featureGuid, formInstanceRep.guid)) {
      val actual = json.parse<FormInstanceRep.Complete>(response.content!!)
      assertEquals(formInstanceRep, actual)
    }
  }

  @Test
  fun incorrectFeatureGuid() {
    val creatorAccountGuid = UUID.randomUUID()
    val featureGuid = UUID.randomUUID()

    val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
    piperTest.setup(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))

    var formInstanceRep = FormInstanceRepFixtures.fixture.complete(this, formTemplateRep.guid, 1, creatorAccountGuid, 5)
    piperTest.setup(
      endpoint = FormInstanceApi.Post(
        featureGuid = featureGuid,
        rep = FormInstanceRepFixtures.fixture.creation(formTemplateRep.guid, creatorAccountGuid)
      )
    )

    val formInstanceQuestionRep =
      FormInstanceQuestionRepFixtures.textFixture.complete(this, formTemplateRep.questions.first().guid)
    formInstanceRep = formInstanceRep.copy(questions = formInstanceRep.questions.plus(formInstanceQuestionRep))
    piperTest.setup(
      endpoint = FormInstanceQuestionApi.Put(
        featureGuid = featureGuid,
        formInstanceGuid = formInstanceRep.guid,
        questionGuid = formTemplateRep.questions.first().guid,
        rep = FormInstanceQuestionRepFixtures.textFixture.creation()
      )
    )

    piperTest.test(
      endpoint = FormInstanceQuestionApi.Delete(
        featureGuid = UUID.randomUUID(),
        formInstanceGuid = formInstanceRep.guid,
        questionGuid = formInstanceQuestionRep.questionGuid!!
      ),
      expectedException = FormInstanceQuestionNotFound()
    )

    piperTest.test(FormInstanceApi.Get(featureGuid, formInstanceRep.guid)) {
      val actual = json.parse<FormInstanceRep.Complete>(response.content!!)
      assertEquals(formInstanceRep, actual)
    }
  }

  @Test
  fun incorrectFormInstanceGuid() {
    val creatorAccountGuid = UUID.randomUUID()
    val featureGuid = UUID.randomUUID()

    val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
    piperTest.setup(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))

    var formInstanceRep = FormInstanceRepFixtures.fixture.complete(this, formTemplateRep.guid, 1, creatorAccountGuid, 5)
    piperTest.setup(
      endpoint = FormInstanceApi.Post(
        featureGuid = featureGuid,
        rep = FormInstanceRepFixtures.fixture.creation(formTemplateRep.guid, creatorAccountGuid)
      )
    )

    val formInstanceQuestionRep =
      FormInstanceQuestionRepFixtures.textFixture.complete(this, formTemplateRep.questions.first().guid)
    formInstanceRep = formInstanceRep.copy(questions = formInstanceRep.questions.plus(formInstanceQuestionRep))
    piperTest.setup(
      endpoint = FormInstanceQuestionApi.Put(
        featureGuid = featureGuid,
        formInstanceGuid = formInstanceRep.guid,
        questionGuid = formTemplateRep.questions.first().guid,
        rep = FormInstanceQuestionRepFixtures.textFixture.creation()
      )
    )

    piperTest.test(
      endpoint = FormInstanceQuestionApi.Delete(
        featureGuid = featureGuid,
        formInstanceGuid = UUID.randomUUID(),
        questionGuid = formInstanceQuestionRep.questionGuid!!
      ),
      expectedException = FormInstanceQuestionNotFound()
    )

    piperTest.test(FormInstanceApi.Get(featureGuid, formInstanceRep.guid)) {
      val actual = json.parse<FormInstanceRep.Complete>(response.content!!)
      assertEquals(formInstanceRep, actual)
    }
  }

  @Test
  fun happyPath() {
    val creatorAccountGuid = UUID.randomUUID()
    val featureGuid = UUID.randomUUID()

    val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
    piperTest.setup(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))

    var formInstanceRep = FormInstanceRepFixtures.fixture.complete(this, formTemplateRep.guid, 1, creatorAccountGuid, 5)
    piperTest.setup(
      endpoint = FormInstanceApi.Post(
        featureGuid = featureGuid,
        rep = FormInstanceRepFixtures.fixture.creation(formTemplateRep.guid, creatorAccountGuid)
      )
    )

    val formInstanceQuestionRep =
      FormInstanceQuestionRepFixtures.textFixture.complete(this, formTemplateRep.questions.first().guid)
    formInstanceRep = formInstanceRep.copy(questions = formInstanceRep.questions.plus(formInstanceQuestionRep))
    piperTest.setup(
      endpoint = FormInstanceQuestionApi.Put(
        featureGuid = featureGuid,
        formInstanceGuid = formInstanceRep.guid,
        questionGuid = formTemplateRep.questions.first().guid,
        rep = FormInstanceQuestionRepFixtures.textFixture.creation()
      )
    )

    formInstanceRep = formInstanceRep.copy(questions = formInstanceRep.questions.minus(formInstanceQuestionRep))
    piperTest.test(
      endpoint = FormInstanceQuestionApi.Delete(
        featureGuid = featureGuid,
        formInstanceGuid = formInstanceRep.guid,
        questionGuid = formInstanceQuestionRep.questionGuid!!
      )
    ) {}

    piperTest.test(FormInstanceApi.Get(featureGuid, formInstanceRep.guid)) {
      val actual = json.parse<FormInstanceRep.Complete>(response.content!!)
      assertEquals(formInstanceRep, actual)
    }
  }
}
