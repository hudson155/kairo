package io.limberapp.backend.module.forms.endpoint.formTemplate

import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.backend.module.forms.exception.formTemplate.FormTemplateNotFound
import io.limberapp.backend.module.forms.testing.ResourceTest
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import org.junit.jupiter.api.Test
import java.util.UUID

internal class DeleteFormTemplateTest : ResourceTest() {
  @Test
  fun doesNotExist() {
    val formTemplateGuid = UUID.randomUUID()

    piperTest.test(
      endpoint = FormTemplateApi.Delete(formTemplateGuid),
      expectedException = FormTemplateNotFound()
    )
  }

  @Test
  fun happyPath() {
    val featureGuid = UUID.randomUUID()

    val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, featureGuid, 0)
    piperTest.setup(FormTemplateApi.Post(FormTemplateRepFixtures.exampleFormFixture.creation(featureGuid)))

    piperTest.test(FormTemplateApi.Delete(formTemplateRep.guid)) {}

    piperTest.test(
      endpoint = FormTemplateApi.Get(formTemplateRep.guid),
      expectedException = FormTemplateNotFound()
    )
  }
}
