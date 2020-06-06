package io.limberapp.backend.module.forms.endpoint.formInstance

import io.limberapp.backend.module.forms.api.formInstance.FormInstanceApi
import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import io.limberapp.backend.module.forms.testing.ResourceTest
import io.limberapp.backend.module.forms.testing.fixtures.formInstance.FormInstanceRepFixtures
import io.limberapp.backend.module.forms.testing.fixtures.formInstance.summary
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class GetFormInstancesByFeatureGuidTest : ResourceTest() {
  @Test
  fun happyPathNoFormInstances() {
    val featureGuid = UUID.randomUUID()

    piperTest.test(FormInstanceApi.GetByFeatureGuid(featureGuid)) {
      val actual = json.parseSet<FormInstanceRep.Summary>(response.content!!)
      assertTrue(actual.isEmpty())
    }
  }

  @Test
  fun happyPathMultipleFormInstances() {
    val creatorAccountGuid = UUID.randomUUID()
    val featureGuid = UUID.randomUUID()

    val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, featureGuid, 0)
    piperTest.setup(FormTemplateApi.Post(FormTemplateRepFixtures.exampleFormFixture.creation(featureGuid)))

    val formInstance0Rep = FormInstanceRepFixtures.fixture.complete(
      this, featureGuid, formTemplateRep.guid, 1, creatorAccountGuid, 5
    )
    piperTest.setup(
      endpoint = FormInstanceApi.Post(
        rep = FormInstanceRepFixtures.fixture.creation(featureGuid, formTemplateRep.guid, creatorAccountGuid)
      )
    )

    val formInstance1Rep = FormInstanceRepFixtures.fixture.complete(
      this, featureGuid, formTemplateRep.guid, 2, creatorAccountGuid, 6
    )
    piperTest.setup(
      endpoint = FormInstanceApi.Post(
        rep = FormInstanceRepFixtures.fixture.creation(featureGuid, formTemplateRep.guid, creatorAccountGuid)
      )
    )

    piperTest.test(FormInstanceApi.GetByFeatureGuid(featureGuid)) {
      val actual = json.parseSet<FormInstanceRep.Summary>(response.content!!)
      assertEquals(setOf(formInstance0Rep.summary(), formInstance1Rep.summary()), actual)
    }
  }
}
