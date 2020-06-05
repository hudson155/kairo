package io.limberapp.backend.module.forms.endpoint.formInstance

import io.limberapp.backend.module.forms.api.formInstance.FormInstanceApi
import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.backend.module.forms.exception.formInstance.FormTemplateCannotBeInstantiatedInAnotherFeature
import io.limberapp.backend.module.forms.exception.formTemplate.FormTemplateNotFound
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import io.limberapp.backend.module.forms.testing.ResourceTest
import io.limberapp.backend.module.forms.testing.fixtures.formInstance.FormInstanceRepFixtures
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

internal class PostFormInstanceTest : ResourceTest() {
  @Test
  fun formTemplateDoesNotExist() {
    val featureGuid = UUID.randomUUID()
    val formTemplateGuid = UUID.randomUUID()

    FormInstanceRepFixtures.fixture.complete(this, featureGuid, formTemplateGuid, 1, 5)
    piperTest.test(
      endpoint = FormInstanceApi.Post(FormInstanceRepFixtures.fixture.creation(featureGuid, formTemplateGuid)),
      expectedException = FormTemplateNotFound()
    )
  }

  @Test
  fun featureDoesNotMatchFormTemplate() {
    val feature0Guid = UUID.randomUUID()
    val feature1Guid = UUID.randomUUID()

    val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, feature0Guid, 0)
    piperTest.setup(FormTemplateApi.Post(FormTemplateRepFixtures.exampleFormFixture.creation(feature0Guid)))

    FormInstanceRepFixtures.fixture.complete(this, feature1Guid, formTemplateRep.guid, 1, 5)
    piperTest.test(
      endpoint = FormInstanceApi.Post(
        rep = FormInstanceRepFixtures.fixture.creation(feature1Guid, formTemplateRep.guid)
      ),
      expectedException = FormTemplateCannotBeInstantiatedInAnotherFeature()
    )
  }

  @Test
  fun happyPath() {
    val featureGuid = UUID.randomUUID()

    val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, featureGuid, 0)
    piperTest.setup(FormTemplateApi.Post(FormTemplateRepFixtures.exampleFormFixture.creation(featureGuid)))

    val formInstanceRep = FormInstanceRepFixtures.fixture.complete(this, featureGuid, formTemplateRep.guid, 1, 5)
    piperTest.test(
      endpoint = FormInstanceApi.Post(FormInstanceRepFixtures.fixture.creation(featureGuid, formTemplateRep.guid))
    ) {
      val actual = json.parse<FormInstanceRep.Complete>(response.content!!)
      assertEquals(formInstanceRep, actual)
    }

    piperTest.test(FormInstanceApi.Get(formInstanceRep.guid)) {
      val actual = json.parse<FormInstanceRep.Complete>(response.content!!)
      assertEquals(formInstanceRep, actual)
    }
  }
}
