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

    val formTemplateUpdateRep = FormTemplateRep.Update("Crazy Form")
    piperTest.test(
      endpoint = FormTemplateApi.Patch(featureGuid, formTemplateGuid, formTemplateUpdateRep),
      expectedException = FormTemplateNotFound()
    )
  }

  @Test
  fun existsInDifferentFeature() {
    val feature0Guid = UUID.randomUUID()
    val feature1Guid = UUID.randomUUID()

    val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, feature0Guid, 0)
    piperTest.setup(FormTemplateApi.Post(feature0Guid, FormTemplateRepFixtures.exampleFormFixture.creation()))

    val formTemplateUpdateRep = FormTemplateRep.Update("Crazy Form")
    piperTest.test(
      endpoint = FormTemplateApi.Patch(feature1Guid, formTemplateRep.guid, formTemplateUpdateRep),
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

    var formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, featureGuid, 0)
    piperTest.setup(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))

    val formTemplateUpdateRep = FormTemplateRep.Update("Crazy Form")
    formTemplateRep = formTemplateRep.copy(title = formTemplateUpdateRep.title!!)
    piperTest.test(FormTemplateApi.Patch(featureGuid, formTemplateRep.guid, formTemplateUpdateRep)) {
      val actual = json.parse<FormTemplateRep.Summary>(response.content!!)
      assertEquals(formTemplateRep.summary(), actual)
    }

    piperTest.test(FormTemplateApi.Get(featureGuid, formTemplateRep.guid)) {
      val actual = json.parse<FormTemplateRep.Complete>(response.content!!)
      assertEquals(formTemplateRep, actual)
    }
  }
}
