package io.limberapp.backend.module.forms.endpoint.formInstance

import io.limberapp.backend.module.forms.api.formInstance.FormInstanceApi
import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.backend.module.forms.exception.formInstance.FormInstanceNotFound
import io.limberapp.backend.module.forms.testing.ResourceTest
import io.limberapp.backend.module.forms.testing.fixtures.formInstance.FormInstanceRepFixtures
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import org.junit.jupiter.api.Test
import java.util.*

internal class DeleteFormInstanceTest : ResourceTest() {
  @Test
  fun doesNotExist() {
    val formInstanceGuid = UUID.randomUUID()

    piperTest.test(
      endpoint = FormInstanceApi.Delete(formInstanceGuid),
      expectedException = FormInstanceNotFound()
    )
  }

  @Test
  fun happyPath() {
    val featureGuid = UUID.randomUUID()

    val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, featureGuid, 0)
    piperTest.setup(FormTemplateApi.Post(FormTemplateRepFixtures.exampleFormFixture.creation(featureGuid)))

    val formInstanceRep = FormInstanceRepFixtures.fixture.complete(this, featureGuid, formTemplateRep.guid, 1, 5)
    piperTest.setup(
      endpoint = FormInstanceApi.Post(FormInstanceRepFixtures.fixture.creation(featureGuid, formTemplateRep.guid))
    )

    piperTest.test(FormInstanceApi.Delete(formInstanceRep.guid)) {}

    piperTest.test(
      endpoint = FormInstanceApi.Get(formTemplateRep.guid),
      expectedException = FormInstanceNotFound()
    )
  }
}
