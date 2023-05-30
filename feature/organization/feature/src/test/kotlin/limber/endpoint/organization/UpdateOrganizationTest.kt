package limber.endpoint.organization

import io.kotest.matchers.shouldBe
import limber.api.organization.OrganizationApi
import limber.fixture.organization.OrganizationFixture
import limber.fixture.organization.create
import limber.rep.organization.OrganizationRep
import limber.testing.IntegrationTest
import limber.testing.should.shouldBeUnprocessable
import limber.testing.test
import limber.testing.testSetup
import org.junit.jupiter.api.Test

internal class UpdateOrganizationTest : IntegrationTest() {
  @Test
  fun `organization does not exist`() {
    val organizationId = "org_0"

    test {
      shouldBeUnprocessable("Organization does not exist.") {
        val update = OrganizationRep.Update(name = " Hotel ")
        organizationClient(OrganizationApi.Update(organizationId, update))
      }
    }
  }

  @Test
  fun `no updates`() {
    val organization = testSetup("Create organization") {
      create(OrganizationFixture.acmeCo)
    }

    test {
      val update = OrganizationRep.Update()
      organizationClient(OrganizationApi.Update(organization.id, update))
        .shouldBe(organization)
      organizationClient(OrganizationApi.Get(organization.id))
        .shouldBe(organization)
    }
  }

  @Test
  fun `name, happy`() {
    var organization = testSetup("Create organization") {
      create(OrganizationFixture.acmeCo)
    }

    test {
      val update = OrganizationRep.Update(name = " Hotel ")
      organization = organization.copy(name = "Hotel")
      organizationClient(OrganizationApi.Update(organization.id, update))
        .shouldBe(organization)
      organizationClient(OrganizationApi.Get(organization.id))
        .shouldBe(organization)
    }
  }
}
