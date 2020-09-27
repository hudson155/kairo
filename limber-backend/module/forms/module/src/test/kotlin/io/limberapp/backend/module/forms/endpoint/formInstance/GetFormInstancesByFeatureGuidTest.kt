package io.limberapp.backend.module.forms.endpoint.formInstance

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.forms.api.formInstance.FormInstanceApi
import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import io.limberapp.backend.module.forms.rep.formInstance.summary
import io.limberapp.backend.module.forms.testing.IntegrationTest
import io.limberapp.backend.module.forms.testing.fixtures.formInstance.FormInstanceRepFixtures
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import io.limberapp.common.LimberApplication
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

object GetFormInstancesByFeatureGuidTest {
  internal class Default(
    engine: TestApplicationEngine,
    limberServer: LimberApplication<*>,
  ) : IntegrationTest(engine, limberServer) {
    @Test
    fun happyPathNoFormInstances() {
      val featureGuid = UUID.randomUUID()

      test(FormInstanceApi.GetByFeatureGuid(featureGuid, creatorAccountGuid = null)) {
        val actual = json.parseList<FormInstanceRep.Summary>(responseContent)
        assertTrue(actual.isEmpty())
      }
    }

    @Test
    fun happyPathMultipleFormInstances() {
      val creatorAccountGuid = UUID.randomUUID()
      val featureGuid = UUID.randomUUID()

      val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
      setup(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))

      val formInstance0Rep = FormInstanceRepFixtures.fixture.complete(this, formTemplateRep.guid, creatorAccountGuid, 1)
      setup(
        endpoint = FormInstanceApi.Post(
          featureGuid = featureGuid,
          rep = FormInstanceRepFixtures.fixture.creation(formTemplateRep.guid, creatorAccountGuid)
        )
      )

      val formInstance1Rep = FormInstanceRepFixtures.fixture.complete(this, formTemplateRep.guid, creatorAccountGuid, 2)
      setup(
        endpoint = FormInstanceApi.Post(
          featureGuid = featureGuid,
          rep = FormInstanceRepFixtures.fixture.creation(formTemplateRep.guid, creatorAccountGuid)
        )
      )

      test(FormInstanceApi.GetByFeatureGuid(featureGuid, creatorAccountGuid = null)) {
        val actual = json.parseList<FormInstanceRep.Summary>(responseContent)
        // Not asserting ordering.
        assertEquals(listOf(formInstance1Rep.summary(), formInstance0Rep.summary()).toSet(), actual.toSet())
      }
    }
  }

  internal class CreatorAccountGuid(
    engine: TestApplicationEngine,
    limberServer: LimberApplication<*>,
  ) : IntegrationTest(engine, limberServer) {
    @Test
    fun happyPathNoFormInstancesForCreator() {
      val creatorAccountGuid = UUID.randomUUID()
      val featureGuid = UUID.randomUUID()

      val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
      setup(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))

      setup(
        endpoint = FormInstanceApi.Post(
          featureGuid = featureGuid,
          rep = FormInstanceRepFixtures.fixture.creation(formTemplateRep.guid, creatorAccountGuid)
        )
      )

      setup(
        endpoint = FormInstanceApi.Post(
          featureGuid = featureGuid,
          rep = FormInstanceRepFixtures.fixture.creation(formTemplateRep.guid, creatorAccountGuid)
        )
      )

      test(FormInstanceApi.GetByFeatureGuid(featureGuid, creatorAccountGuid = UUID.randomUUID())) {
        val actual = json.parseList<FormInstanceRep.Summary>(responseContent)
        assertTrue(actual.isEmpty())
      }
    }

    @Test
    fun happyPathMultipleFormInstancesForCreator() {
      val creatorAccountGuid = UUID.randomUUID()
      val featureGuid = UUID.randomUUID()

      val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
      setup(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))

      val formInstance0Rep = FormInstanceRepFixtures.fixture.complete(this, formTemplateRep.guid, creatorAccountGuid, 1)
      setup(
        endpoint = FormInstanceApi.Post(
          featureGuid = featureGuid,
          rep = FormInstanceRepFixtures.fixture.creation(formTemplateRep.guid, creatorAccountGuid)
        )
      )

      val formInstance1Rep = FormInstanceRepFixtures.fixture.complete(this, formTemplateRep.guid, creatorAccountGuid, 2)
      setup(
        endpoint = FormInstanceApi.Post(
          featureGuid = featureGuid,
          rep = FormInstanceRepFixtures.fixture.creation(formTemplateRep.guid, creatorAccountGuid)
        )
      )

      test(FormInstanceApi.GetByFeatureGuid(featureGuid, creatorAccountGuid = creatorAccountGuid)) {
        val actual = json.parseList<FormInstanceRep.Summary>(responseContent)
        // Not asserting ordering.
        assertEquals(listOf(formInstance1Rep.summary(), formInstance0Rep.summary()).toSet(), actual.toSet())
      }
    }
  }
}
