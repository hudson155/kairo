package limber.endpoint.organizationHostname

import io.kotest.matchers.shouldBe
import limber.api.organizationHostname.OrganizationHostnameApi
import limber.fixture.organization.OrganizationFixture
import limber.fixture.organization.create
import limber.fixture.organizationHostname.OrganizationHostnameFixture
import limber.fixture.organizationHostname.create
import limber.testing.IntegrationTest
import limber.testing.should.shouldNotBeFound
import limber.testing.test
import limber.testing.testSetup
import org.junit.jupiter.api.Test

internal class GetOrganizationHostnameTest : IntegrationTest() {
  @Test
  fun `hostname does not exist`() {
    val hostnameId = "host_0"

    test {
      shouldNotBeFound {
        hostnameClient(OrganizationHostnameApi.Get(hostnameId))
      }
    }
  }

  @Test
  fun `hostname exists`() {
    val organization = testSetup("Create organization") {
      create(OrganizationFixture.acmeCo)
    }

    val hostname = testSetup("Create hostname") {
      create(organization.id, OrganizationHostnameFixture.fooBarBaz)
    }

    test {
      hostnameClient(OrganizationHostnameApi.Get(hostname.id))
        .shouldBe(hostname)
    }
  }
}
