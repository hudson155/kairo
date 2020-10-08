package io.limberapp.backend.module.forms.endpoint.formInstance

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.forms.api.formInstance.FormInstanceApi
import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.backend.module.forms.rep.formInstance.summary
import io.limberapp.backend.module.forms.testing.IntegrationTest
import io.limberapp.backend.module.forms.testing.fixtures.formInstance.FormInstanceRepFixtures
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import io.limberapp.common.LimberApplication
import org.junit.jupiter.api.Test
import java.util.*

object GetFormInstancesByFeatureGuidTest {
  internal class Default(
    engine: TestApplicationEngine,
    limberServer: LimberApplication<*>,
  ) : IntegrationTest(engine, limberServer) {
    @Test
    fun happyPathNoFormInstances() {
      val featureGuid = UUID.randomUUID()

      test(expectResult = emptyList()) {
        formInstanceClient(FormInstanceApi.GetByFeatureGuid(featureGuid, creatorAccountGuid = null))
      }
    }

    @Test
    fun happyPathMultipleFormInstances() {
      val creatorAccountGuid = UUID.randomUUID()
      val featureGuid = UUID.randomUUID()

      val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
      setup {
        formTemplateClient(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))
      }

      val formInstance0Rep = FormInstanceRepFixtures.fixture.complete(this, formTemplateRep.guid, creatorAccountGuid, 1)
      setup {
        formInstanceClient(FormInstanceApi.Post(
          featureGuid = featureGuid,
          rep = FormInstanceRepFixtures.fixture.creation(formTemplateRep.guid, creatorAccountGuid)
        ))
      }

      val formInstance1Rep = FormInstanceRepFixtures.fixture.complete(this, formTemplateRep.guid, creatorAccountGuid, 2)
      setup {
        formInstanceClient(FormInstanceApi.Post(
          featureGuid = featureGuid,
          rep = FormInstanceRepFixtures.fixture.creation(formTemplateRep.guid, creatorAccountGuid)
        ))
      }

      // Not asserting ordering.
      test(setOf(formInstance1Rep.summary(), formInstance0Rep.summary())) {
        formInstanceClient(FormInstanceApi.GetByFeatureGuid(featureGuid, creatorAccountGuid = null)).toSet()
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
      setup {
        formTemplateClient(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))
      }

      setup {
        formInstanceClient(FormInstanceApi.Post(
          featureGuid = featureGuid,
          rep = FormInstanceRepFixtures.fixture.creation(formTemplateRep.guid, creatorAccountGuid)
        ))
      }

      setup {
        formInstanceClient(FormInstanceApi.Post(
          featureGuid = featureGuid,
          rep = FormInstanceRepFixtures.fixture.creation(formTemplateRep.guid, creatorAccountGuid)
        ))
      }

      test(expectResult = emptyList()) {
        formInstanceClient(FormInstanceApi.GetByFeatureGuid(featureGuid, creatorAccountGuid = UUID.randomUUID()))
      }
    }

    @Test
    fun happyPathMultipleFormInstancesForCreator() {
      val creatorAccountGuid = UUID.randomUUID()
      val featureGuid = UUID.randomUUID()

      val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
      setup {
        formTemplateClient(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))
      }

      val formInstance0Rep = FormInstanceRepFixtures.fixture.complete(this, formTemplateRep.guid, creatorAccountGuid, 1)
      setup {
        formInstanceClient(FormInstanceApi.Post(
          featureGuid = featureGuid,
          rep = FormInstanceRepFixtures.fixture.creation(formTemplateRep.guid, creatorAccountGuid)
        ))
      }

      val formInstance1Rep = FormInstanceRepFixtures.fixture.complete(this, formTemplateRep.guid, creatorAccountGuid, 2)
      setup {
        formInstanceClient(FormInstanceApi.Post(
          featureGuid = featureGuid,
          rep = FormInstanceRepFixtures.fixture.creation(formTemplateRep.guid, creatorAccountGuid)
        ))
      }

      // Not asserting ordering.
      test(expectResult = setOf(formInstance1Rep.summary(), formInstance0Rep.summary())) {
        formInstanceClient(FormInstanceApi.GetByFeatureGuid(
          featureGuid = featureGuid,
          creatorAccountGuid = creatorAccountGuid)
        ).toSet()
      }
    }
  }
}
