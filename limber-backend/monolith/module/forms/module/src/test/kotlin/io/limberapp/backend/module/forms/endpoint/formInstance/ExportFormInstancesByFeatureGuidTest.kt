package io.limberapp.backend.module.forms.endpoint.formInstance

import io.limberapp.backend.module.forms.api.formInstance.FormInstanceApi
import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import io.limberapp.backend.module.forms.testing.ResourceTest
import io.limberapp.backend.module.forms.testing.fixtures.formInstance.FormInstanceRepFixtures
import io.limberapp.backend.module.forms.testing.fixtures.formTemplate.FormTemplateRepFixtures
import io.limberapp.backend.module.orgs.model.org.FeatureModel
import io.limberapp.backend.module.orgs.service.org.FeatureService
import io.limberapp.backend.module.users.model.account.UserModel
import io.limberapp.backend.module.users.service.account.UserService
import io.limberapp.common.testing.responseContent
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import kotlin.test.assertEquals

private const val FIXED_CLOCK_FORMATTED_VALUE = "Sun, Dec 2, 2007 at 22:15 MST"

object ExportFormInstancesByFeatureGuidTest {
  internal class Default : ResourceTest() {
    @Test
    fun happyPathNoFormInstances() {
      val orgGuid = UUID.randomUUID()
      val featureGuid = UUID.randomUUID()

      val existingFeature = mockk<FeatureModel>().apply { every { this@apply.orgGuid } returns orgGuid }
      every { mockedServices[FeatureService::class].findOnlyOrThrow(any()) } returns existingFeature

      val existingUser0 = mockk<UserModel>().apply {
        every { this@apply.guid } returns UUID.randomUUID()
        every { this@apply.firstName } returns "Jeff"
        every { this@apply.lastName } returns "Hudson"
        every { this@apply.emailAddress } returns "jhudson@jhudson.ca"
      }
      val existingUser1 = mockk<UserModel>().apply {
        every { this@apply.guid } returns UUID.randomUUID()
        every { this@apply.firstName } returns "Bill"
        every { this@apply.lastName } returns "Gates"
        every { this@apply.emailAddress } returns "bill.gates@microsoft.com"
      }
      every { mockedServices[UserService::class].findAsSet(any()) } returns setOf(existingUser0, existingUser1)

      limberTest.test(
        endpoint = FormInstanceApi.ExportByFeatureGuid(
          featureGuid = featureGuid,
          creatorAccountGuid = null,
          timeZone = ZoneId.of("America/Edmonton")
        )
      ) {
        assertEquals(
          """
          Number,Submitted date,Creator name,Creator email address
          """.trimIndent() + '\n',
          responseContent
        )
      }
    }

    @Test
    fun happyPathMultipleFormInstances() {
      val orgGuid = UUID.randomUUID()
      val featureGuid = UUID.randomUUID()

      val existingFeature = mockk<FeatureModel>().apply { every { this@apply.orgGuid } returns orgGuid }
      every { mockedServices[FeatureService::class].findOnlyOrThrow(any()) } returns existingFeature

      val existingUser0 = mockk<UserModel>().apply {
        every { this@apply.guid } returns UUID.randomUUID()
        every { this@apply.firstName } returns "Jeff"
        every { this@apply.lastName } returns "Hudson"
        every { this@apply.emailAddress } returns "jhudson@jhudson.ca"
      }
      val existingUser1 = mockk<UserModel>().apply {
        every { this@apply.guid } returns UUID.randomUUID()
        every { this@apply.firstName } returns "Bill"
        every { this@apply.lastName } returns "Gates"
        every { this@apply.emailAddress } returns "bill.gates@microsoft.com"
      }
      every { mockedServices[UserService::class].findAsSet(any()) } returns setOf(existingUser0, existingUser1)

      val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
      limberTest.setup(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))

      var formInstance0Rep = FormInstanceRepFixtures.fixture.complete(this, formTemplateRep.guid, existingUser0.guid, 1)
      limberTest.setup(
        endpoint = FormInstanceApi.Post(
          featureGuid = featureGuid,
          rep = FormInstanceRepFixtures.fixture.creation(formTemplateRep.guid, existingUser0.guid)
        )
      )

      formInstance0Rep = formInstance0Rep.copy(number = 1, submittedDate = LocalDateTime.now(fixedClock))
      limberTest.setup(
        endpoint = FormInstanceApi.Patch(
          featureGuid = featureGuid,
          formInstanceGuid = formInstance0Rep.guid,
          rep = FormInstanceRep.Update(submitted = true)
        )
      )

      val formInstance1Rep = FormInstanceRepFixtures.fixture.complete(this, formTemplateRep.guid, existingUser1.guid, 6)
      limberTest.setup(
        endpoint = FormInstanceApi.Post(
          featureGuid = featureGuid,
          rep = FormInstanceRepFixtures.fixture.creation(formTemplateRep.guid, existingUser1.guid)
        )
      )

      limberTest.test(
        endpoint = FormInstanceApi.ExportByFeatureGuid(
          featureGuid = featureGuid,
          creatorAccountGuid = null,
          timeZone = ZoneId.of("America/Edmonton")
        )
      ) {
        assertEquals(
          """
          Number,Submitted date,Creator name,Creator email address
          ${formInstance0Rep.number},"$FIXED_CLOCK_FORMATTED_VALUE",Jeff Hudson,jhudson@jhudson.ca
          ${formInstance1Rep.number},,Bill Gates,bill.gates@microsoft.com
          """.trimIndent() + '\n',
          responseContent
        )
      }
    }
  }

  internal class CreatorAccountGuid : ResourceTest() {
    @Test
    fun happyPathNoFormInstancesForCreator() {
      val orgGuid = UUID.randomUUID()
      val featureGuid = UUID.randomUUID()

      val existingFeature = mockk<FeatureModel>().apply { every { this@apply.orgGuid } returns orgGuid }
      every { mockedServices[FeatureService::class].findOnlyOrThrow(any()) } returns existingFeature

      val existingUser0 = mockk<UserModel>().apply {
        every { this@apply.guid } returns UUID.randomUUID()
        every { this@apply.firstName } returns "Jeff"
        every { this@apply.lastName } returns "Hudson"
        every { this@apply.emailAddress } returns "jhudson@jhudson.ca"
      }
      val existingUser1 = mockk<UserModel>().apply {
        every { this@apply.guid } returns UUID.randomUUID()
        every { this@apply.firstName } returns "Bill"
        every { this@apply.lastName } returns "Gates"
        every { this@apply.emailAddress } returns "bill.gates@microsoft.com"
      }
      every { mockedServices[UserService::class].findAsSet(any()) } returns setOf(existingUser0, existingUser1)

      val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
      limberTest.setup(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))

      limberTest.setup(
        endpoint = FormInstanceApi.Post(
          featureGuid = featureGuid,
          rep = FormInstanceRepFixtures.fixture.creation(formTemplateRep.guid, existingUser0.guid)
        )
      )

      limberTest.setup(
        endpoint = FormInstanceApi.Post(
          featureGuid = featureGuid,
          rep = FormInstanceRepFixtures.fixture.creation(formTemplateRep.guid, existingUser1.guid)
        )
      )

      limberTest.test(
        endpoint = FormInstanceApi.ExportByFeatureGuid(
          featureGuid = featureGuid,
          creatorAccountGuid = UUID.randomUUID(),
          timeZone = ZoneId.of("America/Edmonton")
        )
      ) {
        assertEquals(
          """
          Number,Submitted date,Creator name,Creator email address
          """.trimIndent() + '\n',
          responseContent
        )
      }
    }

    @Test
    fun happyPathMultipleFormInstancesForCreator() {
      val orgGuid = UUID.randomUUID()
      val featureGuid = UUID.randomUUID()

      val existingFeature = mockk<FeatureModel>().apply { every { this@apply.orgGuid } returns orgGuid }
      every { mockedServices[FeatureService::class].findOnlyOrThrow(any()) } returns existingFeature

      val existingUser0 = mockk<UserModel>().apply {
        every { this@apply.guid } returns UUID.randomUUID()
        every { this@apply.firstName } returns "Jeff"
        every { this@apply.lastName } returns "Hudson"
        every { this@apply.emailAddress } returns "jhudson@jhudson.ca"
      }
      val existingUser1 = mockk<UserModel>().apply {
        every { this@apply.guid } returns UUID.randomUUID()
        every { this@apply.firstName } returns "Bill"
        every { this@apply.lastName } returns "Gates"
        every { this@apply.emailAddress } returns "bill.gates@microsoft.com"
      }
      every { mockedServices[UserService::class].findAsSet(any()) } returns setOf(existingUser0, existingUser1)

      val formTemplateRep = FormTemplateRepFixtures.exampleFormFixture.complete(this, 0)
      limberTest.setup(FormTemplateApi.Post(featureGuid, FormTemplateRepFixtures.exampleFormFixture.creation()))

      var formInstance0Rep = FormInstanceRepFixtures.fixture.complete(this, formTemplateRep.guid, existingUser0.guid, 1)
      limberTest.setup(
        endpoint = FormInstanceApi.Post(
          featureGuid = featureGuid,
          rep = FormInstanceRepFixtures.fixture.creation(formTemplateRep.guid, existingUser0.guid)
        )
      )

      formInstance0Rep = formInstance0Rep.copy(number = 1, submittedDate = LocalDateTime.now(fixedClock))
      limberTest.setup(
        endpoint = FormInstanceApi.Patch(
          featureGuid = featureGuid,
          formInstanceGuid = formInstance0Rep.guid,
          rep = FormInstanceRep.Update(submitted = true)
        )
      )

      limberTest.setup(
        endpoint = FormInstanceApi.Post(
          featureGuid = featureGuid,
          rep = FormInstanceRepFixtures.fixture.creation(formTemplateRep.guid, existingUser1.guid)
        )
      )

      limberTest.test(
        endpoint = FormInstanceApi.ExportByFeatureGuid(
          featureGuid = featureGuid,
          creatorAccountGuid = existingUser0.guid,
          timeZone = ZoneId.of("America/Edmonton")
        )
      ) {
        assertEquals(
          """
          Number,Submitted date,Creator name,Creator email address
          ${formInstance0Rep.number},"$FIXED_CLOCK_FORMATTED_VALUE",Jeff Hudson,jhudson@jhudson.ca
          """.trimIndent() + '\n',
          responseContent
        )
      }
    }
  }
}
