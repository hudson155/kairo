package limber.endpoint.organization

import io.kotest.matchers.shouldBe
import limber.api.organization.OrganizationApi
import limber.fixture.organization.OrganizationFixture
import limber.fixture.organization.create
import limber.fixture.organizationHostname.OrganizationHostnameFixture
import limber.fixture.organizationHostname.create
import limber.testing.IntegrationTest
import limber.testing.should.shouldNotBeFound
import limber.testing.test
import limber.testing.testSetup
import org.junit.jupiter.api.Test

internal class GetOrganizationByHostnameTest : IntegrationTest() {
  @Test
  fun `organization does not exist`() {
    val hostname = "foo.bar.baz"

    test {
      shouldNotBeFound {
        organizationClient(OrganizationApi.GetByHostname(hostname))
      }
    }
  }

  @Test
  fun `organization exists`() {
    val organization = testSetup("Create organization") {
      create(OrganizationFixture.acmeCo)
    }

    testSetup("Create hostname") {
      create(organization.guid, OrganizationHostnameFixture.fooBarBaz)
    }

    test {
      organizationClient(OrganizationApi.GetByHostname("foo.bar.baz"))
        .shouldBe(organization)
    }
  }
}
