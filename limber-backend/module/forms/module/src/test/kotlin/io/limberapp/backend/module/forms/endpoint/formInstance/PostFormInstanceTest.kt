package io.limberapp.backend.module.forms.endpoint.formInstance

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.forms.api.formInstance.FormInstanceApi
import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.backend.module.forms.exception.formTemplate.FormTemplateNotFound
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import io.limberapp.backend.module.forms.testing.IntegrationTest
import io.limberapp.backend.module.forms.testing.fixtures.formInstance.FormInstanceRepFixtures
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import io.limberapp.common.LimberApplication
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

internal class PostFormInstanceTest(
  engine: TestApplicationEngine,
  limberServer: LimberApplication<*>,
) : IntegrationTest(engine, limberServer) {
  @Test
  fun formTemplateDoesNotExist() {
    val creatorAccountGuid = UUID.randomUUID()
    val featureGuid = UUID.randomUUID()
    val formTemplateGuid = UUID.randomUUID()

    test(
      endpoint = FormInstanceApi.Post(
        featureGuid = featureGuid,
        rep = FormInstanceRepFixtures.fixture.creation(formTemplateGuid, creatorAccountGuid)
      ),
      expectedException = FormTemplateNotFound()
    )
  }

  @Test
  fun featureDoesNotMatchFormTemplate() {
    val creatorAccountGuid = UUID.randomUUID()
    val feature0Guid = UUID.randomUUID()
    val feature1Guid = UUID.randomUUID()

    val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
    setup(FormTemplateApi.Post(feature0Guid, FormTemplateRepFixtures.exampleFormFixture.creation()))

    test(
      endpoint = FormInstanceApi.Post(
        featureGuid = feature1Guid,
        rep = FormInstanceRepFixtures.fixture.creation(formTemplateRep.guid, creatorAccountGuid)
      ),
      expectedException = FormTemplateNotFound()
    )
  }

  @Test
  fun happyPath() {
    val creatorAccountGuid = UUID.randomUUID()
    val featureGuid = UUID.randomUUID()

    val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
    setup(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))

    val formInstanceRep = FormInstanceRepFixtures.fixture.complete(this, formTemplateRep.guid, creatorAccountGuid, 1)
    test(
      endpoint = FormInstanceApi.Post(
        featureGuid = featureGuid,
        rep = FormInstanceRepFixtures.fixture.creation(formTemplateRep.guid, creatorAccountGuid)
      )
    ) {
      val actual = json.parse<FormInstanceRep.Complete>(responseContent)
      assertEquals(formInstanceRep, actual)
    }

    test(FormInstanceApi.Get(featureGuid, formInstanceRep.guid)) {
      val actual = json.parse<FormInstanceRep.Complete>(responseContent)
      assertEquals(formInstanceRep, actual)
    }
  }
}
