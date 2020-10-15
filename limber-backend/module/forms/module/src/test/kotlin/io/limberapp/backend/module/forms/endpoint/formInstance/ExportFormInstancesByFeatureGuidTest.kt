package io.limberapp.backend.module.forms.endpoint.formInstance

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.backend.module.forms.api.formInstance.FormInstanceApi
import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import io.limberapp.backend.module.forms.testing.IntegrationTest
import io.limberapp.backend.module.forms.testing.fixtures.formInstance.FormInstanceRepFixtures
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import io.limberapp.backend.module.orgs.model.feature.FeatureModel
import io.limberapp.backend.module.orgs.service.feature.FeatureService
import io.limberapp.backend.module.users.model.account.UserModel
import io.limberapp.backend.module.users.service.account.UserService
import io.limberapp.common.LimberApplication
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

private const val FIXED_CLOCK_FORMATTED_VALUE = "Sun, Dec 2, 2007 at 22:15 MST"

object ExportFormInstancesByFeatureGuidTest {
  internal class Default(
      engine: TestApplicationEngine,
      limberServer: LimberApplication<*>,
  ) : IntegrationTest(engine, limberServer) {
    @Test
    fun happyPathNoFormInstances() {
      val orgGuid = UUID.randomUUID()
      val featureGuid = UUID.randomUUID()

      val existingFeature = mockk<FeatureModel>().apply { every { this@apply.orgGuid } returns orgGuid }
      every { mocks[FeatureService::class].findOnlyOrThrow(any()) } returns existingFeature

      val existingUser0 = mockk<UserModel>().apply {
        every { this@apply.guid } returns UUID.randomUUID()
        every { this@apply.firstName } returns "Jeff"
        every { this@apply.lastName } returns "Hudson"
      }
      val existingUser1 = mockk<UserModel>().apply {
        every { this@apply.guid } returns UUID.randomUUID()
        every { this@apply.firstName } returns "Bill"
        every { this@apply.lastName } returns "Gates"
      }
      every { mocks[UserService::class].getByOrgGuid(orgGuid) } returns setOf(existingUser0, existingUser1)

      test(expectResult = "Number,Submitted date,Creator name\n") {
        formInstanceClient(FormInstanceApi.ExportByFeatureGuid(
            featureGuid = featureGuid,
            creatorAccountGuid = null,
            timeZone = ZoneId.of("America/Edmonton")
        ))
      }
    }

    @Test
    fun happyPathMultipleFormInstances() {
      val orgGuid = UUID.randomUUID()
      val featureGuid = UUID.randomUUID()

      val existingFeature = mockk<FeatureModel>().apply { every { this@apply.orgGuid } returns orgGuid }
      every { mocks[FeatureService::class].findOnlyOrThrow(any()) } returns existingFeature

      val existingUser0 = mockk<UserModel>().apply {
        every { this@apply.guid } returns UUID.randomUUID()
        every { this@apply.firstName } returns "Jeff"
        every { this@apply.lastName } returns "Hudson"
      }
      val existingUser1 = mockk<UserModel>().apply {
        every { this@apply.guid } returns UUID.randomUUID()
        every { this@apply.firstName } returns "Bill"
        every { this@apply.lastName } returns "Gates"
      }
      every { mocks[UserService::class].getByOrgGuid(orgGuid) } returns setOf(existingUser0, existingUser1)

      val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
      setup {
        formTemplateClient(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))
      }

      var formInstance0Rep = FormInstanceRepFixtures.fixture.complete(this, formTemplateRep.guid, existingUser0.guid, 1)
      setup {
        formInstanceClient(FormInstanceApi.Post(
            featureGuid = featureGuid,
            rep = FormInstanceRepFixtures.fixture.creation(formTemplateRep.guid, existingUser0.guid)
        ))
      }

      formInstance0Rep = formInstance0Rep.copy(number = 1, submittedDate = LocalDateTime.now(clock))
      setup {
        formInstanceClient(FormInstanceApi.Patch(
            featureGuid = featureGuid,
            formInstanceGuid = formInstance0Rep.guid,
            rep = FormInstanceRep.Update(submitted = true)
        ))
      }

      val formInstance1Rep = FormInstanceRepFixtures.fixture.complete(this, formTemplateRep.guid, existingUser1.guid, 6)
      setup {
        formInstanceClient(FormInstanceApi.Post(
            featureGuid = featureGuid,
            rep = FormInstanceRepFixtures.fixture.creation(formTemplateRep.guid, existingUser1.guid)
        ))
      }

      test(expectResult = "Number,Submitted date,Creator name\n"
          + "${formInstance0Rep.number},\"$FIXED_CLOCK_FORMATTED_VALUE\",Jeff Hudson\n"
          + "${formInstance1Rep.number},,Bill Gates\n"
      ) {
        formInstanceClient(FormInstanceApi.ExportByFeatureGuid(
            featureGuid = featureGuid,
            creatorAccountGuid = null,
            timeZone = ZoneId.of("America/Edmonton")
        ))
      }
    }
  }

  internal class CreatorAccountGuid(
      engine: TestApplicationEngine,
      limberServer: LimberApplication<*>,
  ) : IntegrationTest(engine, limberServer) {
    @Test
    fun happyPathNoFormInstancesForCreator() {
      val orgGuid = UUID.randomUUID()
      val featureGuid = UUID.randomUUID()

      val existingFeature = mockk<FeatureModel>().apply { every { this@apply.orgGuid } returns orgGuid }
      every { mocks[FeatureService::class].findOnlyOrThrow(any()) } returns existingFeature

      val existingUser0 = mockk<UserModel>().apply {
        every { this@apply.guid } returns UUID.randomUUID()
        every { this@apply.firstName } returns "Jeff"
        every { this@apply.lastName } returns "Hudson"
      }
      val existingUser1 = mockk<UserModel>().apply {
        every { this@apply.guid } returns UUID.randomUUID()
        every { this@apply.firstName } returns "Bill"
        every { this@apply.lastName } returns "Gates"
      }
      every { mocks[UserService::class].getByOrgGuid(orgGuid) } returns setOf(existingUser0, existingUser1)

      val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
      setup {
        formTemplateClient(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))
      }

      setup {
        formInstanceClient(FormInstanceApi.Post(
            featureGuid = featureGuid,
            rep = FormInstanceRepFixtures.fixture.creation(formTemplateRep.guid, existingUser0.guid)
        ))
      }

      setup {
        formInstanceClient(FormInstanceApi.Post(
            featureGuid = featureGuid,
            rep = FormInstanceRepFixtures.fixture.creation(formTemplateRep.guid, existingUser1.guid)
        ))
      }

      test(expectResult = "Number,Submitted date,Creator name\n") {
        formInstanceClient(FormInstanceApi.ExportByFeatureGuid(
            featureGuid = featureGuid,
            creatorAccountGuid = UUID.randomUUID(),
            timeZone = ZoneId.of("America/Edmonton")
        ))
      }
    }

    @Test
    fun happyPathMultipleFormInstancesForCreator() {
      val orgGuid = UUID.randomUUID()
      val featureGuid = UUID.randomUUID()

      val existingFeature = mockk<FeatureModel>().apply { every { this@apply.orgGuid } returns orgGuid }
      every { mocks[FeatureService::class].findOnlyOrThrow(any()) } returns existingFeature

      val existingUser0 = mockk<UserModel>().apply {
        every { this@apply.guid } returns UUID.randomUUID()
        every { this@apply.firstName } returns "Jeff"
        every { this@apply.lastName } returns "Hudson"
      }
      val existingUser1 = mockk<UserModel>().apply {
        every { this@apply.guid } returns UUID.randomUUID()
        every { this@apply.firstName } returns "Bill"
        every { this@apply.lastName } returns "Gates"
      }
      every { mocks[UserService::class].getByOrgGuid(orgGuid) } returns setOf(existingUser0, existingUser1)

      val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
      setup {
        formTemplateClient(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))
      }

      var formInstance0Rep = FormInstanceRepFixtures.fixture.complete(this, formTemplateRep.guid, existingUser0.guid, 1)
      setup {
        formInstanceClient(FormInstanceApi.Post(
            featureGuid = featureGuid,
            rep = FormInstanceRepFixtures.fixture.creation(formTemplateRep.guid, existingUser0.guid)
        ))
      }

      formInstance0Rep = formInstance0Rep.copy(number = 1, submittedDate = LocalDateTime.now(clock))
      setup {
        formInstanceClient(FormInstanceApi.Patch(
            featureGuid = featureGuid,
            formInstanceGuid = formInstance0Rep.guid,
            rep = FormInstanceRep.Update(submitted = true)
        ))
      }

      setup {
        formInstanceClient(FormInstanceApi.Post(
            featureGuid = featureGuid,
            rep = FormInstanceRepFixtures.fixture.creation(formTemplateRep.guid, existingUser1.guid)
        ))
      }

      test(expectResult = "Number,Submitted date,Creator name\n"
          + "${formInstance0Rep.number},\"$FIXED_CLOCK_FORMATTED_VALUE\",Jeff Hudson\n"
      ) {
        formInstanceClient(FormInstanceApi.ExportByFeatureGuid(
            featureGuid = featureGuid,
            creatorAccountGuid = existingUser0.guid,
            timeZone = ZoneId.of("America/Edmonton")
        ))
      }
    }
  }
}
