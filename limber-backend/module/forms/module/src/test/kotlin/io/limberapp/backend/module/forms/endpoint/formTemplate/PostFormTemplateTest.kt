package io.limberapp.backend.module.forms.endpoint.formTemplate

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.backend.module.forms.testing.IntegrationTest
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import io.limberapp.common.LimberApplication
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

internal class PostFormTemplateTest(
  engine: TestApplicationEngine,
  limberServer: LimberApplication<*>,
) : IntegrationTest(engine, limberServer) {
  @Test
  fun happyPath() {
    val featureGuid = UUID.randomUUID()

    val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
    test(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation())) {
      val actual = json.parse<FormTemplateRep.Complete>(responseContent)
      assertEquals(formTemplateRep, actual)
    }

    test(FormTemplateApi.Get(featureGuid, formTemplateRep.guid)) {
      val actual = json.parse<FormTemplateRep.Complete>(responseContent)
      assertEquals(formTemplateRep, actual)
    }
  }
}
