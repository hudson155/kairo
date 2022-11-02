package limber.endpoint.organizationAuth

import io.kotest.matchers.shouldBe
import limber.api.organizationAuth.OrganizationAuthApi
import limber.fixture.organization.OrganizationFixture
import limber.fixture.organization.create
import limber.fixture.organizationAuth.OrganizationAuthFixture
import limber.fixture.organizationAuth.create
import limber.fixture.organizationHostname.OrganizationHostnameFixture
import limber.fixture.organizationHostname.create
import limber.testing.IntegrationTest
import limber.testing.should.shouldNotBeFound
import limber.testing.test
import limber.testing.testSetup
import org.junit.jupiter.api.Test

internal class GetOrganizationAuthByHostnameTest : IntegrationTest() {
  @Test
  fun `auth does not exist`() {
    val hostname = "foo.bar.baz"

    test {
      shouldNotBeFound {
        authClient(OrganizationAuthApi.GetByHostname(hostname))
      }
    }
  }

  @Test
  fun `auth exists`() {
    val organization = testSetup("Create organization") {
      create(OrganizationFixture.acmeCo)
    }

    testSetup("Create hostname") {
      create(organization.guid, OrganizationHostnameFixture.fooBarBaz)
    }

    val auth = testSetup("Create auth") {
      create(organization.guid, OrganizationAuthFixture.acmeCo)
    }

    test {
      authClient(OrganizationAuthApi.GetByHostname("foo.bar.baz"))
        .shouldBe(auth)
    }
  }
}
