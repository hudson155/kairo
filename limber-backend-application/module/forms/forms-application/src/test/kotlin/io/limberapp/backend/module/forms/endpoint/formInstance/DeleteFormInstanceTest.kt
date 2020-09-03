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
    val featureGuid = UUID.randomUUID()
    val formInstanceGuid = UUID.randomUUID()

    piperTest.test(
      endpoint = FormInstanceApi.Delete(featureGuid, formInstanceGuid),
      expectedException = FormInstanceNotFound()
    )
  }

  @Test
  fun existsInDifferentFeature() {
    val creatorAccountGuid = UUID.randomUUID()
    val feature0Guid = UUID.randomUUID()
    val feature1Guid = UUID.randomUUID()

    val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
    piperTest.setup(FormTemplateApi.Post(feature0Guid, FormTemplateRepFixtures.exampleFormFixture.creation()))

    val formInstanceRep = FormInstanceRepFixtures.fixture.complete(this, formTemplateRep.guid, 1, creatorAccountGuid, 1)
    piperTest.setup(
      endpoint = FormInstanceApi.Post(
        featureGuid = feature0Guid,
        rep = FormInstanceRepFixtures.fixture.creation(formTemplateRep.guid, creatorAccountGuid)
      )
    )

    piperTest.test(
      endpoint = FormInstanceApi.Delete(feature1Guid, formInstanceRep.guid),
      expectedException = FormInstanceNotFound()
    )

    piperTest.test(FormInstanceApi.Get(feature0Guid, formInstanceRep.guid)) {}
  }

  @Test
  fun happyPath() {
    val creatorAccountGuid = UUID.randomUUID()
    val featureGuid = UUID.randomUUID()

    val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
    piperTest.setup(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))

    val formInstanceRep = FormInstanceRepFixtures.fixture.complete(this, formTemplateRep.guid, 1, creatorAccountGuid, 1)
    piperTest.setup(
      endpoint = FormInstanceApi.Post(
        featureGuid = featureGuid,
        rep = FormInstanceRepFixtures.fixture.creation(formTemplateRep.guid, creatorAccountGuid)
      )
    )

    piperTest.test(FormInstanceApi.Delete(featureGuid, formInstanceRep.guid)) {}

    piperTest.test(
      endpoint = FormInstanceApi.Get(featureGuid, formInstanceRep.guid),
      expectedException = FormInstanceNotFound()
    )
  }
}
