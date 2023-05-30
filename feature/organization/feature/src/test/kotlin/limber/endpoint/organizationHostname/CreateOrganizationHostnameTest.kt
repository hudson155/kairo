package limber.endpoint.organizationHostname

import io.kotest.matchers.shouldBe
import limber.api.organizationHostname.OrganizationHostnameApi
import limber.fixture.organization.OrganizationFixture
import limber.fixture.organization.create
import limber.fixture.organizationHostname.OrganizationHostnameFixture
import limber.fixture.organizationHostname.create
import limber.testing.IntegrationTest
import limber.testing.should.shouldBeConflict
import limber.testing.should.shouldBeUnprocessable
import limber.testing.test
import limber.testing.testSetup
import org.junit.jupiter.api.Test

internal class CreateOrganizationHostnameTest : IntegrationTest() {
  @Test
  fun `organization does not exist`() {
    val organizationId = "org_0"

    test {
      shouldBeUnprocessable("Organization does not exist.") {
        val creator = OrganizationHostnameFixture.fooBarBaz.creator
        hostnameClient(OrganizationHostnameApi.Create(organizationId, creator))
      }
    }
  }

  @Test
  fun `hostname, duplicate`() {
    val organization = testSetup("Create organization") {
      create(OrganizationFixture.acmeCo)
    }

    val hostname = testSetup("Create hostname") {
      create(organization.id, OrganizationHostnameFixture.fooBarBaz)
    }

    test {
      shouldBeConflict("Organization hostname already taken.") {
        val creator = OrganizationHostnameFixture.barBazQux.creator.copy(hostname = hostname.hostname)
        hostnameClient(OrganizationHostnameApi.Create(organization.id, creator))
      }
    }
  }

  @Test
  fun happy() {
    val organization = testSetup("Create organization") {
      create(OrganizationFixture.acmeCo)
    }

    test {
      val creator = OrganizationHostnameFixture.fooBarBaz.creator
      val hostname = hostnameClient(OrganizationHostnameApi.Create(organization.id, creator))
      hostname.shouldBe(OrganizationHostnameFixture.fooBarBaz(organization.id, "host_0"))
      hostnameClient(OrganizationHostnameApi.Get(hostname.id))
        .shouldBe(hostname)
    }
  }
}
