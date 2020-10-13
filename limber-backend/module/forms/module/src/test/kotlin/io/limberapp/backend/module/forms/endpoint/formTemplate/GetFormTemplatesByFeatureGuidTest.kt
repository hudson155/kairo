package io.limberapp.backend.module.forms.endpoint.formTemplate

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.backend.module.forms.testing.IntegrationTest
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.summary
import io.limberapp.common.LimberApplication
import org.junit.jupiter.api.Test
import java.util.*

internal class GetFormTemplatesByFeatureGuidTest(
    engine: TestApplicationEngine,
    limberServer: LimberApplication<*>,
) : IntegrationTest(engine, limberServer) {
  @Test
  fun happyPathNoFormTemplates() {
    val featureGuid = UUID.randomUUID()

    test(expectResult = emptySet()) {
      formTemplateClient(FormTemplateApi.GetByFeatureGuid(featureGuid))
    }
  }

  @Test
  fun happyPathMultipleFormTemplates() {
    val featureGuid = UUID.randomUUID()

    val formTemplate0Rep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
    setup {
      formTemplateClient(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))
    }

    val formTemplate1Rep = FormTemplateRepFixtures.vehicleInspectionFixture.complete(this, 1)
    setup {
      formTemplateClient(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.vehicleInspectionFixture.creation()))
    }

    test(expectResult = setOf(formTemplate1Rep.summary(), formTemplate0Rep.summary())) {
      formTemplateClient(FormTemplateApi.GetByFeatureGuid(featureGuid))
    }
  }
}
