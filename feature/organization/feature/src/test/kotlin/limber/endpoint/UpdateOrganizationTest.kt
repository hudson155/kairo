package limber.endpoint

import io.kotest.matchers.shouldBe
import limber.api.OrganizationApi
import limber.rep.OrganizationRep
import limber.testing.IntegrationTest
import limber.testing.should.shouldBeUnprocessable
import limber.testing.should.shouldHaveValidationErrors
import limber.testing.test
import limber.testing.testSetup
import org.junit.jupiter.api.Test
import java.util.UUID

internal class UpdateOrganizationTest : IntegrationTest() {
  @Test
  fun `organization does not exist`() {
    val organizationGuid = UUID.randomUUID()

    test {
      shouldBeUnprocessable("Organization does not exist.") {
        val updater = OrganizationRep.Updater(name = "Hotel")
        organizationClient(OrganizationApi.Update(organizationGuid, updater))
      }
    }
  }

  @Test
  fun `no updates`() {
    val organization = testSetup("Create organization") {
      val creator = OrganizationRep.Creator(name = "Limber")
      organizationClient(OrganizationApi.Create(creator))
      return@testSetup OrganizationRep(guid = guidGenerator[0], name = "Limber")
    }

    test {
      val updater = OrganizationRep.Updater()
      organizationClient(OrganizationApi.Update(organization.guid, updater))
        .shouldBe(organization)
      organizationClient(OrganizationApi.Get(organization.guid))
        .shouldBe(organization)
    }
  }

  @Test
  fun `name, too short`() {
    val organizationGuid = testSetup("Create organization") {
      val creator = OrganizationRep.Creator(name = "Limber")
      organizationClient(OrganizationApi.Create(creator))
      return@testSetup guidGenerator[0]
    }

    test {
      shouldHaveValidationErrors("body.name" to "size must be between 3 and 255") {
        val updater = OrganizationRep.Updater(name = " Ho ")
        organizationClient(OrganizationApi.Update(organizationGuid, updater))
      }
    }
  }

  @Test
  fun `name, too long`() {
    val organizationGuid = testSetup("Create organization") {
      val creator = OrganizationRep.Creator(name = "Limber")
      organizationClient(OrganizationApi.Create(creator))
      return@testSetup guidGenerator[0]
    }

    test {
      shouldHaveValidationErrors("body.name" to "size must be between 3 and 255") {
        val updater = OrganizationRep.Updater(name = "A".repeat(256))
        organizationClient(OrganizationApi.Update(organizationGuid, updater))
      }
    }
  }

  @Test
  fun `name, happy path`() {
    var organization = testSetup("Create organization") {
      val creator = OrganizationRep.Creator(name = "Limber")
      organizationClient(OrganizationApi.Create(creator))
      return@testSetup OrganizationRep(guid = guidGenerator[0], name = "Hotel")
    }

    test {
      val updater = OrganizationRep.Updater(name = " Hotel ")
      organization = organization.copy(name = "Hotel")
      organizationClient(OrganizationApi.Update(organization.guid, updater))
        .shouldBe(organization)
      organizationClient(OrganizationApi.Get(organization.guid))
        .shouldBe(organization)
    }
  }
}
