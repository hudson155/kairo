package io.limberapp.backend.module.forms.endpoint.formTemplate

import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.backend.module.forms.testing.ResourceTest
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

internal class PostFormTemplateTest : ResourceTest() {
  @Test
  fun happyPath() {
    val featureGuid = UUID.randomUUID()

    val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
    piperTest.test(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation())) {
      val actual = json.parse<FormTemplateRep.Complete>(response.content!!)
      assertEquals(formTemplateRep, actual)
    }

    piperTest.test(FormTemplateApi.Get(featureGuid, formTemplateRep.guid)) {
      val actual = json.parse<FormTemplateRep.Complete>(response.content!!)
      assertEquals(formTemplateRep, actual)
    }
  }
}
