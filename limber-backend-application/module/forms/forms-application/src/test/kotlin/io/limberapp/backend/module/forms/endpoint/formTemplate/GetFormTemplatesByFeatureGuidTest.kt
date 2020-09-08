package io.limberapp.backend.module.forms.endpoint.formTemplate

import com.piperframework.testing.responseContent
import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.backend.module.forms.testing.ResourceTest
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.summary
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class GetFormTemplatesByFeatureGuidTest : ResourceTest() {
  @Test
  fun happyPathNoFormTemplates() {
    val featureGuid = UUID.randomUUID()

    piperTest.test(FormTemplateApi.GetByFeatureGuid(featureGuid)) {
      val actual = json.parseSet<FormTemplateRep.Summary>(responseContent)
      assertTrue(actual.isEmpty())
    }
  }

  @Test
  fun happyPathMultipleFormTemplates() {
    val featureGuid = UUID.randomUUID()

    val formTemplate0Rep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
    piperTest.setup(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))

    val formTemplate1Rep = FormTemplateRepFixtures.vehicleInspectionFixture.complete(this, 1)
    piperTest.setup(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.vehicleInspectionFixture.creation()))

    piperTest.test(FormTemplateApi.GetByFeatureGuid(featureGuid)) {
      val actual = json.parseSet<FormTemplateRep.Summary>(responseContent)
      assertEquals(setOf(formTemplate1Rep.summary(), formTemplate0Rep.summary()), actual)
    }
  }
}
