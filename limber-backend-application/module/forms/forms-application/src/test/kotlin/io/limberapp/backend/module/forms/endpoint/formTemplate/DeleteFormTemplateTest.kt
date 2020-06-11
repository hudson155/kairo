package io.limberapp.backend.module.forms.endpoint.formTemplate

import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.backend.module.forms.exception.formTemplate.FormTemplateNotFound
import io.limberapp.backend.module.forms.testing.ResourceTest
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import org.junit.jupiter.api.Test
import java.util.*

internal class DeleteFormTemplateTest : ResourceTest() {
  @Test
  fun doesNotExist() {
    val featureGuid = UUID.randomUUID()
    val formTemplateGuid = UUID.randomUUID()

    piperTest.test(
      endpoint = FormTemplateApi.Delete(featureGuid, formTemplateGuid),
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
      endpoint = FormTemplateApi.Delete(feature1Guid, formTemplateRep.guid),
      expectedException = FormTemplateNotFound()
    )

    piperTest.test(FormTemplateApi.Get(feature0Guid, formTemplateRep.guid)) {}
  }

  @Test
  fun happyPath() {
    val featureGuid = UUID.randomUUID()

    val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
    piperTest.setup(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))

    piperTest.test(FormTemplateApi.Delete(featureGuid, formTemplateRep.guid)) {}

    piperTest.test(
      endpoint = FormTemplateApi.Get(featureGuid, formTemplateRep.guid),
      expectedException = FormTemplateNotFound()
    )
  }
}
