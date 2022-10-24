package limber.endpoint.organization

import io.kotest.matchers.shouldBe
import limber.api.organization.OrganizationApi
import limber.fixture.organization.OrganizationFixture
import limber.fixture.organization.create
import limber.rep.organization.OrganizationRep
import limber.testing.IntegrationTest
import limber.testing.should.shouldBeUnprocessable
import limber.testing.should.shouldHaveValidationErrors
import limber.testing.test
import limber.testing.testSetup
import org.junit.jupiter.api.Test
import java.util.UUID

internal class UpdateOrganizationTest : IntegrationTest() {
  private val updater: OrganizationRep.Updater = OrganizationRep.Updater(name = " Hotel ")

  @Test
  fun `organization does not exist`() {
    val organizationGuid = UUID.randomUUID()

    test {
      shouldBeUnprocessable("Organization does not exist.") {
        organizationClient(OrganizationApi.Update(organizationGuid, updater))
      }
    }
  }

  @Test
  fun `no updates`() {
    val organization = testSetup("Create organization") {
      create(OrganizationFixture.acmeCo)
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
    val organization = testSetup("Create organization") {
      create(OrganizationFixture.acmeCo)
    }

    test {
      shouldHaveValidationErrors("body.name" to "size must be between 3 and 255") {
        val updater = updater.copy(name = " Li ")
        organizationClient(OrganizationApi.Update(organization.guid, updater))
      }
    }
  }

  @Test
  fun `name, too long`() {
    val organization = testSetup("Create organization") {
      create(OrganizationFixture.acmeCo)
    }

    test {
      shouldHaveValidationErrors("body.name" to "size must be between 3 and 255") {
        val updater = updater.copy(name = "A".repeat(256))
        organizationClient(OrganizationApi.Update(organization.guid, updater))
      }
    }
  }

  @Test
  fun `name, happy`() {
    var organization = testSetup("Create organization") {
      create(OrganizationFixture.acmeCo)
    }

    test {
      organization = organization.copy(name = "Hotel")
      organizationClient(OrganizationApi.Update(organization.guid, updater))
        .shouldBe(organization)
      organizationClient(OrganizationApi.Get(organization.guid))
        .shouldBe(organization)
    }
  }
}
