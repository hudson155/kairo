package io.limberapp.backend.module.forms.endpoint.formTemplate

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.backend.module.forms.testing.IntegrationTest
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.summary
import io.limberapp.common.LimberApplication
import org.junit.jupiter.api.Test
import java.util.*

internal class PatchFormTemplateTest(
  engine: TestApplicationEngine,
  limberServer: LimberApplication<*>,
) : IntegrationTest(engine, limberServer) {
  @Test
  fun doesNotExist() {
    val featureGuid = UUID.randomUUID()
    val formTemplateGuid = UUID.randomUUID()

    test(expectResult = null) {
      formTemplateClient(FormTemplateApi.Patch(featureGuid, formTemplateGuid, FormTemplateRep.Update("Crazy Form")))
    }
  }

  @Test
  fun existsInDifferentFeature() {
    val feature0Guid = UUID.randomUUID()
    val feature1Guid = UUID.randomUUID()

    val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
    setup {
      formTemplateClient(FormTemplateApi.Post(feature0Guid, FormTemplateRepFixtures.exampleFormFixture.creation()))
    }

    test(expectResult = null) {
      formTemplateClient(FormTemplateApi.Patch(
        featureGuid = feature1Guid,
        formTemplateGuid = formTemplateRep.guid,
        rep = FormTemplateRep.Update("Crazy Form")
      ))
    }

    test(expectResult = formTemplateRep) {
      formTemplateClient(FormTemplateApi.Get(feature0Guid, formTemplateRep.guid))
    }
  }

  @Test
  fun happyPath() {
    val featureGuid = UUID.randomUUID()

    var formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
    setup {
      formTemplateClient(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))
    }

    formTemplateRep = formTemplateRep.copy(title = "Crazy Form")
    test(expectResult = formTemplateRep.summary()) {
      formTemplateClient(FormTemplateApi.Patch(featureGuid, formTemplateRep.guid, FormTemplateRep.Update("Crazy Form")))
    }

    test(expectResult = formTemplateRep) {
      formTemplateClient(FormTemplateApi.Get(featureGuid, formTemplateRep.guid))
    }
  }
}
