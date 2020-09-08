package io.limberapp.backend.module.forms.endpoint.formTemplate

import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.backend.module.forms.exception.formTemplate.FormTemplateNotFound
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.backend.module.forms.testing.ResourceTest
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.summary
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

internal class PatchFormTemplateTest : ResourceTest() {
  @Test
  fun doesNotExist() {
    val featureGuid = UUID.randomUUID()
    val formTemplateGuid = UUID.randomUUID()

    piperTest.test(
      endpoint = FormTemplateApi.Patch(featureGuid, formTemplateGuid, FormTemplateRep.Update("Crazy Form")),
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
      endpoint = FormTemplateApi.Patch(feature1Guid, formTemplateRep.guid, FormTemplateRep.Update("Crazy Form")),
      expectedException = FormTemplateNotFound()
    )

    piperTest.test(FormTemplateApi.Get(feature0Guid, formTemplateRep.guid)) {
      val actual = json.parse<FormTemplateRep.Complete>(response.content!!)
      assertEquals(formTemplateRep, actual)
    }
  }

  @Test
  fun happyPath() {
    val featureGuid = UUID.randomUUID()

    var formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
    piperTest.setup(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))

    formTemplateRep = formTemplateRep.copy(title = "Crazy Form")
    piperTest.test(FormTemplateApi.Patch(featureGuid, formTemplateRep.guid, FormTemplateRep.Update("Crazy Form"))) {
      val actual = json.parse<FormTemplateRep.Summary>(response.content!!)
      assertEquals(formTemplateRep.summary(), actual)
    }

    piperTest.test(FormTemplateApi.Get(featureGuid, formTemplateRep.guid)) {
      val actual = json.parse<FormTemplateRep.Complete>(response.content!!)
      assertEquals(formTemplateRep, actual)
    }
  }
}
