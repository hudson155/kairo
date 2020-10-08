package io.limberapp.backend.module.forms.endpoint.formInstance.question

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.forms.api.formInstance.FormInstanceApi
import io.limberapp.backend.module.forms.api.formInstance.FormInstanceQuestionApi
import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateQuestionApi
import io.limberapp.backend.module.forms.exception.formInstance.CannotDeleteRequiredQuestion
import io.limberapp.backend.module.forms.exception.formInstance.FormInstanceQuestionNotFound
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import io.limberapp.backend.module.forms.testing.IntegrationTest
import io.limberapp.backend.module.forms.testing.fixtures.formInstance.FormInstanceQuestionRepFixtures
import io.limberapp.backend.module.forms.testing.fixtures.formInstance.FormInstanceRepFixtures
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateQuestionRepFixtures
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import io.limberapp.common.LimberApplication
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

internal class DeleteFormInstanceQuestionTest(
  engine: TestApplicationEngine,
  limberServer: LimberApplication<*>,
) : IntegrationTest(engine, limberServer) {
  @Test
  fun formInstanceDoesNotExist() {
    val featureGuid = UUID.randomUUID()
    val formInstanceGuid = UUID.randomUUID()

    setup {
      formTemplateClient(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))
    }

    test(
      endpoint = FormInstanceQuestionApi.Delete(featureGuid, formInstanceGuid, UUID.randomUUID()),
      expectedException = FormInstanceQuestionNotFound()
    )
  }

  @Test
  fun formInstanceQuestionDoesNotExist() {
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

    test(
      endpoint = FormInstanceQuestionApi.Delete(featureGuid, formInstanceRep.guid, UUID.randomUUID()),
      expectedException = FormInstanceQuestionNotFound()
    )

    test(expectResult = formInstanceRep) {
      formInstanceClient(FormInstanceApi.Get(featureGuid, formInstanceRep.guid))
    }
  }

  @Test
  fun incorrectFeatureGuid() {
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
    setup(
      endpoint = FormInstanceQuestionApi.Put(
        featureGuid = featureGuid,
        formInstanceGuid = formInstanceRep.guid,
        questionGuid = formTemplateQuestionRep.guid,
        rep = FormInstanceQuestionRepFixtures.textFixture.creation(this)
      )
    )

    test(
      endpoint = FormInstanceQuestionApi.Delete(
        featureGuid = UUID.randomUUID(),
        formInstanceGuid = formInstanceRep.guid,
        questionGuid = formTemplateQuestionRep.guid
      ),
      expectedException = FormInstanceQuestionNotFound()
    )

    test(expectResult = formInstanceRep) {
      formInstanceClient(FormInstanceApi.Get(featureGuid, formInstanceRep.guid))
    }
  }

  @Test
  fun incorrectFormInstanceGuid() {
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
    setup(
      endpoint = FormInstanceQuestionApi.Put(
        featureGuid = featureGuid,
        formInstanceGuid = formInstanceRep.guid,
        questionGuid = formTemplateQuestionRep.guid,
        rep = FormInstanceQuestionRepFixtures.textFixture.creation(this)
      )
    )

    test(
      endpoint = FormInstanceQuestionApi.Delete(
        featureGuid = featureGuid,
        formInstanceGuid = UUID.randomUUID(),
        questionGuid = formTemplateQuestionRep.guid
      ),
      expectedException = FormInstanceQuestionNotFound()
    )

    test(expectResult = formInstanceRep) {
      formInstanceClient(FormInstanceApi.Get(featureGuid, formInstanceRep.guid))
    }
  }

  @Test
  fun questionIsRequiredFormIsSubmitted() {
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
    setup(
      endpoint = FormInstanceQuestionApi.Put(
        featureGuid = featureGuid,
        formInstanceGuid = formInstanceRep.guid,
        questionGuid = formTemplateQuestionRep.guid,
        rep = FormInstanceQuestionRepFixtures.textFixture.creation(this)
      )
    )

    formInstanceRep = formInstanceRep.copy(number = 1, submittedDate = LocalDateTime.now(clock))
    setup {
      formInstanceClient(FormInstanceApi.Patch(
        featureGuid = featureGuid,
        formInstanceGuid = formInstanceRep.guid,
        rep = FormInstanceRep.Update(submitted = true),
      ))
    }

    test(
      endpoint = FormInstanceQuestionApi.Delete(
        featureGuid = featureGuid,
        formInstanceGuid = formInstanceRep.guid,
        questionGuid = formTemplateQuestionRep.guid
      ),
      expectedException = CannotDeleteRequiredQuestion()
    )

    test(expectResult = formInstanceRep) {
      formInstanceClient(FormInstanceApi.Get(featureGuid, formInstanceRep.guid))
    }
  }

  @Test
  fun questionIsRequiredFormIsNotSubmitted() {
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
    setup(
      endpoint = FormInstanceQuestionApi.Put(
        featureGuid = featureGuid,
        formInstanceGuid = formInstanceRep.guid,
        questionGuid = formTemplateQuestionRep.guid,
        rep = FormInstanceQuestionRepFixtures.textFixture.creation(this)
      )
    )

    formInstanceRep = formInstanceRep.copy(questions = formInstanceRep.questions - formInstanceQuestionRep)
    test(
      endpoint = FormInstanceQuestionApi.Delete(
        featureGuid = featureGuid,
        formInstanceGuid = formInstanceRep.guid,
        questionGuid = formTemplateQuestionRep.guid
      )
    ) {}

    test(expectResult = formInstanceRep) {
      formInstanceClient(FormInstanceApi.Get(featureGuid, formInstanceRep.guid))
    }
  }

  @Test
  fun happyPath() {
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
      FormInstanceQuestionRepFixtures.textFixture.complete(this, formTemplateQuestionRep.guid)
    formInstanceRep = formInstanceRep.copy(questions = formInstanceRep.questions + formInstanceQuestionRep)
    setup(
      endpoint = FormInstanceQuestionApi.Put(
        featureGuid = featureGuid,
        formInstanceGuid = formInstanceRep.guid,
        questionGuid = formTemplateQuestionRep.guid,
        rep = FormInstanceQuestionRepFixtures.textFixture.creation(this)
      )
    )

    formInstanceRep = formInstanceRep.copy(questions = formInstanceRep.questions - formInstanceQuestionRep)
    test(
      endpoint = FormInstanceQuestionApi.Delete(
        featureGuid = featureGuid,
        formInstanceGuid = formInstanceRep.guid,
        questionGuid = formTemplateQuestionRep.guid
      )
    ) {}

    test(expectResult = formInstanceRep) {
      formInstanceClient(FormInstanceApi.Get(featureGuid, formInstanceRep.guid))
    }
  }
}
