package io.limberapp.backend.module.forms.endpoint.formInstance.question

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.forms.api.formInstance.FormInstanceApi
import io.limberapp.backend.module.forms.api.formInstance.FormInstanceQuestionApi
import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateQuestionApi
import io.limberapp.backend.module.forms.testing.IntegrationTest
import io.limberapp.backend.module.forms.testing.fixtures.formInstance.FormInstanceQuestionRepFixtures
import io.limberapp.backend.module.forms.testing.fixtures.formInstance.FormInstanceRepFixtures
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateQuestionRepFixtures
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import io.limberapp.common.LimberApplication
import org.junit.jupiter.api.Test
import java.util.*

internal class PutFormInstanceQuestionTypeTest(
    engine: TestApplicationEngine,
    limberServer: LimberApplication<*>,
) : IntegrationTest(engine, limberServer) {
  @Test
  fun dateQuestion() {
    val creatorAccountGuid = UUID.randomUUID()
    val featureGuid = UUID.randomUUID()

    var formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
    setup {
      formTemplateClient(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))
    }

    val formTemplateQuestionRep = FormTemplateQuestionRepFixtures.dateFixture.complete(this, 1)
    formTemplateRep = formTemplateRep.copy(questions = formTemplateRep.questions + formTemplateQuestionRep)
    setup {
      formTemplateQuestionClient(FormTemplateQuestionApi.Post(
          featureGuid = featureGuid,
          formTemplateGuid = formTemplateRep.guid,
          rep = FormTemplateQuestionRepFixtures.dateFixture.creation(),
      ))
    }

    var formInstanceRep = FormInstanceRepFixtures.fixture.complete(this, formTemplateRep.guid, creatorAccountGuid, 2)
    setup {
      formInstanceClient(FormInstanceApi.Post(
          featureGuid = featureGuid,
          rep = FormInstanceRepFixtures.fixture.creation(formTemplateRep.guid, creatorAccountGuid)
      ))
    }

    val formInstanceQuestionRep =
        FormInstanceQuestionRepFixtures.dateFixture.complete(this, formTemplateQuestionRep.guid)
    formInstanceRep = formInstanceRep.copy(questions = formInstanceRep.questions + formInstanceQuestionRep)
    test(expectResult = formInstanceQuestionRep) {
      formInstanceQuestionClient(FormInstanceQuestionApi.Put(
          featureGuid = featureGuid,
          formInstanceGuid = formInstanceRep.guid,
          questionGuid = formTemplateQuestionRep.guid,
          rep = FormInstanceQuestionRepFixtures.dateFixture.creation(this)
      ))
    }

    test(expectResult = formInstanceRep) {
      formInstanceClient(FormInstanceApi.Get(featureGuid, formInstanceRep.guid))
    }
  }

  @Test
  fun radioSelectorQuestion() {
    val creatorAccountGuid = UUID.randomUUID()
    val featureGuid = UUID.randomUUID()

    var formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
    setup {
      formTemplateClient(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))
    }

    val formTemplateQuestionRep = FormTemplateQuestionRepFixtures.radioSelectorFixture.complete(this, 1)
    formTemplateRep = formTemplateRep.copy(questions = formTemplateRep.questions + formTemplateQuestionRep)
    setup {
      formTemplateQuestionClient(FormTemplateQuestionApi.Post(
          featureGuid = featureGuid,
          formTemplateGuid = formTemplateRep.guid,
          rep = FormTemplateQuestionRepFixtures.radioSelectorFixture.creation(),
      ))
    }

    var formInstanceRep = FormInstanceRepFixtures.fixture.complete(this, formTemplateRep.guid, creatorAccountGuid, 2)
    setup {
      formInstanceClient(FormInstanceApi.Post(
          featureGuid = featureGuid,
          rep = FormInstanceRepFixtures.fixture.creation(formTemplateRep.guid, creatorAccountGuid)
      ))
    }

    val formInstanceQuestionRep =
        FormInstanceQuestionRepFixtures.radioSelectorFixture.complete(this, formTemplateQuestionRep.guid)
    formInstanceRep = formInstanceRep.copy(questions = formInstanceRep.questions + formInstanceQuestionRep)
    test(expectResult = formInstanceQuestionRep) {
      formInstanceQuestionClient(FormInstanceQuestionApi.Put(
          featureGuid = featureGuid,
          formInstanceGuid = formInstanceRep.guid,
          questionGuid = formTemplateQuestionRep.guid,
          rep = FormInstanceQuestionRepFixtures.radioSelectorFixture.creation(this)
      ))
    }

    test(expectResult = formInstanceRep) {
      formInstanceClient(FormInstanceApi.Get(featureGuid, formInstanceRep.guid))
    }
  }

  @Test
  fun textQuestion() {
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

    var formInstanceRep = FormInstanceRepFixtures.fixture.complete(this, formTemplateRep.guid, creatorAccountGuid, 2)
    setup {
      formInstanceClient(FormInstanceApi.Post(
          featureGuid = featureGuid,
          rep = FormInstanceRepFixtures.fixture.creation(formTemplateRep.guid, creatorAccountGuid)
      ))
    }

    val formInstanceQuestionRep =
        FormInstanceQuestionRepFixtures.textFixture.complete(this, formTemplateQuestionRep.guid)
    formInstanceRep = formInstanceRep.copy(questions = formInstanceRep.questions + formInstanceQuestionRep)
    test(expectResult = formInstanceQuestionRep) {
      formInstanceQuestionClient(FormInstanceQuestionApi.Put(
          featureGuid = featureGuid,
          formInstanceGuid = formInstanceRep.guid,
          questionGuid = formTemplateQuestionRep.guid,
          rep = FormInstanceQuestionRepFixtures.textFixture.creation(this)
      ))
    }

    test(expectResult = formInstanceRep) {
      formInstanceClient(FormInstanceApi.Get(featureGuid, formInstanceRep.guid))
    }
  }

  @Test
  fun yesNoQuestion() {
    val creatorAccountGuid = UUID.randomUUID()
    val featureGuid = UUID.randomUUID()

    var formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
    setup {
      formTemplateClient(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))
    }

    val formTemplateQuestionRep = FormTemplateQuestionRepFixtures.yesNoFixture.complete(this, 1)
    formTemplateRep = formTemplateRep.copy(questions = formTemplateRep.questions + formTemplateQuestionRep)
    setup {
      formTemplateQuestionClient(FormTemplateQuestionApi.Post(
          featureGuid = featureGuid,
          formTemplateGuid = formTemplateRep.guid,
          rep = FormTemplateQuestionRepFixtures.yesNoFixture.creation(),
      ))
    }

    var formInstanceRep = FormInstanceRepFixtures.fixture.complete(this, formTemplateRep.guid, creatorAccountGuid, 2)
    setup {
      formInstanceClient(FormInstanceApi.Post(
          featureGuid = featureGuid,
          rep = FormInstanceRepFixtures.fixture.creation(formTemplateRep.guid, creatorAccountGuid)
      ))
    }

    val formInstanceQuestionRep =
        FormInstanceQuestionRepFixtures.yesNoFixture.complete(this, formTemplateQuestionRep.guid)
    formInstanceRep = formInstanceRep.copy(questions = formInstanceRep.questions + formInstanceQuestionRep)
    test(expectResult = formInstanceQuestionRep) {
      formInstanceQuestionClient(FormInstanceQuestionApi.Put(
          featureGuid = featureGuid,
          formInstanceGuid = formInstanceRep.guid,
          questionGuid = formTemplateQuestionRep.guid,
          rep = FormInstanceQuestionRepFixtures.yesNoFixture.creation(this)
      ))
    }

    test(expectResult = formInstanceRep) {
      formInstanceClient(FormInstanceApi.Get(featureGuid, formInstanceRep.guid))
    }
  }
}
