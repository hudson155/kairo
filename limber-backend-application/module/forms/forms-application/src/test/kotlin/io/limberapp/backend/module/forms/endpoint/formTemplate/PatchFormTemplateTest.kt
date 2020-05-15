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
    val formTemplateGuid = UUID.randomUUID()

    val formTemplateUpdateRep = FormTemplateRep.Update("Crazy Form")
    piperTest.test(
      endpoint = FormTemplateApi.Patch(formTemplateGuid, formTemplateUpdateRep),
      expectedException = FormTemplateNotFound()
    )
  }

  @Test
  fun happyPath() {
    val featureGuid = UUID.randomUUID()

    var formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, featureGuid, 0)
    piperTest.setup(FormTemplateApi.Post(FormTemplateRepFixtures.exampleFormFixture.creation(featureGuid)))

    val formTemplateUpdateRep = FormTemplateRep.Update("Crazy Form")
    formTemplateRep = formTemplateRep.copy(title = formTemplateUpdateRep.title!!)
    piperTest.test(FormTemplateApi.Patch(formTemplateRep.guid, formTemplateUpdateRep)) {
      val actual = json.parse<FormTemplateRep.Summary>(response.content!!)
      assertEquals(formTemplateRep.summary(), actual)
    }

    piperTest.test(FormTemplateApi.Get(formTemplateRep.guid)) {
      val actual = json.parse<FormTemplateRep.Complete>(response.content!!)
      assertEquals(formTemplateRep, actual)
    }
  }
}
