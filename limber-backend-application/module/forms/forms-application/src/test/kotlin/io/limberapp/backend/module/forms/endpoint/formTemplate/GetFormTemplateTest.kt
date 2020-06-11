package io.limberapp.backend.module.forms.endpoint.formTemplate

import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.backend.module.forms.exception.formTemplate.FormTemplateNotFound
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.backend.module.forms.testing.ResourceTest
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

internal class GetFormTemplateTest : ResourceTest() {
  @Test
  fun doesNotExist() {
    val featureGuid = UUID.randomUUID()
    val formTemplateGuid = UUID.randomUUID()

    piperTest.test(
      endpoint = FormTemplateApi.Get(featureGuid, formTemplateGuid),
      expectedException = FormTemplateNotFound()
    )
  }

  @Test
  fun existsInDifferentFeature() {
    val feature0Guid = UUID.randomUUID()
    val feature1Guid = UUID.randomUUID()

    val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
    piperTest.setup(FormTemplateApi.Post(feature0Guid, FormTemplateRepFixtures.exampleFormFixture.creation()))

    piperTest.test(
      endpoint = FormTemplateApi.Get(feature1Guid, formTemplateRep.guid),
      expectedException = FormTemplateNotFound()
    )
  }

  @Test
  fun happyPath() {
    val featureGuid = UUID.randomUUID()

    val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
    piperTest.setup(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))

    piperTest.test(FormTemplateApi.Get(featureGuid, formTemplateRep.guid)) {
      val actual = json.parse<FormTemplateRep.Complete>(response.content!!)
      assertEquals(formTemplateRep, actual)
    }
  }
}
