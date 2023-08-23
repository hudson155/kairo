package limber.endpoint.organizationHostname

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import limber.api.organizationHostname.OrganizationHostnameApi
import limber.fixture.organization.OrganizationFixture
import limber.fixture.organization.create
import limber.fixture.organizationHostname.OrganizationHostnameFixture
import limber.fixture.organizationHostname.create
import limber.testing.IntegrationTest
import limber.testing.test
import limber.testing.testSetup
import org.junit.jupiter.api.Test

internal class ListOrganizationHostnamesByOrganizationTest : IntegrationTest() {
  @Test
  fun `no hostnames`() {
    val organizationId = "org_0"

    test {
      hostnameClient(OrganizationHostnameApi.ListByOrganization(organizationId))
        .shouldBeEmpty()
    }
  }

  @Test
  fun `hostnames exist`() {
    val organization = testSetup("Create organization") {
      create(OrganizationFixture.acmeCo)
    }

    val fooBarBazHostname = testSetup("Create hostname") {
      create(organization.id, OrganizationHostnameFixture.fooBarBaz)
    }

    val barBazQuxHostname = testSetup("Create hostname") {
      create(organization.id, OrganizationHostnameFixture.barBazQux)
    }

    test {
      hostnameClient(OrganizationHostnameApi.ListByOrganization(organization.id))
        .shouldContainExactlyInAnyOrder(fooBarBazHostname, barBazQuxHostname)
    }
  }
}
