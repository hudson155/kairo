package limber.endpoint.organization

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import limber.api.organization.OrganizationApi
import limber.fixture.organization.OrganizationFixture
import limber.fixture.organization.create
import limber.fixture.organizationAuth.OrganizationAuthFixture
import limber.fixture.organizationAuth.create
import limber.fixture.organizationHostname.OrganizationHostnameFixture
import limber.fixture.organizationHostname.create
import limber.rep.organization.OrganizationRep
import limber.testing.IntegrationTest
import limber.testing.test
import limber.testing.testSetup
import org.junit.jupiter.api.Test

internal class SearchOrganizationsTest : IntegrationTest() {
  @Test
  fun `no organizations`() {
    createFixtures()

    test {
      organizationClient(OrganizationApi.Search("qwerty"))
        .shouldBeEmpty()
    }
  }

  @Test
  fun `organizations exist by name`() {
    val organization = createFixtures()

    test {
      organizationClient(OrganizationApi.Search("acme"))
        .shouldContainExactlyInAnyOrder(organization)
    }
  }

  @Test
  fun `organizations exist by hostname`() {
    val organization = createFixtures()

    test {
      organizationClient(OrganizationApi.Search("BAR.BAZ"))
        .shouldContainExactlyInAnyOrder(organization)
    }
  }

  @Test
  fun `organizations exist by auth0 organization id`() {
    val organization = createFixtures()

    test {
      organizationClient(OrganizationApi.Search(guidGenerator[3].toString().takeLast(4)))
        .shouldContainExactlyInAnyOrder(organization)
    }
  }

  private fun createFixtures(): OrganizationRep {
    val organization = testSetup("Create organization") {
      create(OrganizationFixture.acmeCo)
    }

    testSetup("Create hostname") {
      create(organization.guid, OrganizationHostnameFixture.fooBarBaz)
    }

    testSetup("Create auth") {
      create(organization.guid, OrganizationAuthFixture.acmeCo)
    }

    return organization
  }
}
