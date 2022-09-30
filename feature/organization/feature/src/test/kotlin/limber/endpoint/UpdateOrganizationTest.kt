package limber.endpoint

import io.kotest.matchers.shouldBe
import limber.api.OrganizationApi
import limber.rep.OrganizationRep
import limber.testing.IntegrationTest
import limber.testing.integrationTest
import limber.testing.should.shouldBeUnprocessable
import limber.testing.should.shouldHaveValidationErrors
import org.junit.jupiter.api.Test
import java.util.UUID

internal class UpdateOrganizationTest : IntegrationTest() {
  @Test
  fun `organization does not exist`() {
    integrationTest {
      shouldBeUnprocessable {
        organizationClient(OrganizationApi.Update(UUID.randomUUID(), OrganizationRep.Updater(name = "Hotel")))
      }
    }
  }

  @Test
  fun `no updates`() {
    integrationTest {
      val organizationGuid = guidGenerator[0]
      organizationClient(OrganizationApi.Create(OrganizationRep.Creator(name = "Limber")))
      val organization = OrganizationRep(guid = organizationGuid, name = "Limber")
      organizationClient(OrganizationApi.Update(organizationGuid, OrganizationRep.Updater()))
        .shouldBe(organization)
      organizationClient(OrganizationApi.Get(organizationGuid))
        .shouldBe(organization)
    }
  }

  @Test
  fun `name updated, too short`() {
    integrationTest {
      val organizationGuid = guidGenerator[0]
      organizationClient(OrganizationApi.Create(OrganizationRep.Creator(name = "Limber")))
      shouldHaveValidationErrors("body.name" to "size must be between 3 and 255") {
        organizationClient(OrganizationApi.Update(organizationGuid, OrganizationRep.Updater(name = " Ho ")))
      }
    }
  }

  @Test
  fun `name updated, too long`() {
    integrationTest {
      val organizationGuid = guidGenerator[0]
      organizationClient(OrganizationApi.Create(OrganizationRep.Creator(name = "Limber")))
      shouldHaveValidationErrors("body.name" to "size must be between 3 and 255") {
        organizationClient(OrganizationApi.Update(organizationGuid, OrganizationRep.Updater(name = "A".repeat(256))))
      }
    }
  }

  @Test
  fun `name updated, happy path`() {
    integrationTest {
      val organizationGuid = guidGenerator[0]
      organizationClient(OrganizationApi.Create(OrganizationRep.Creator(name = "Limber")))
      val organization = OrganizationRep(guid = organizationGuid, name = "Hotel")
      organizationClient(OrganizationApi.Update(organizationGuid, OrganizationRep.Updater(name = " Hotel ")))
        .shouldBe(organization)
      organizationClient(OrganizationApi.Get(organizationGuid))
        .shouldBe(organization)
    }
  }
}
