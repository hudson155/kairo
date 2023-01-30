package limber.endpoint.organization

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import limber.api.organization.OrganizationApi
import limber.fixture.organization.OrganizationFixture
import limber.fixture.organization.create
import limber.testing.IntegrationTest
import limber.testing.test
import limber.testing.testSetup
import org.junit.jupiter.api.Test

internal class GetAllOrganizationsTest : IntegrationTest() {
  @Test
  fun `no organizations`() {
    test {
      organizationClient(OrganizationApi.GetAll)
        .shouldBeEmpty()
    }
  }

  @Test
  fun `organizations exist`() {
    val organization = testSetup("Create organization") {
      create(OrganizationFixture.acmeCo)
    }

    test {
      organizationClient(OrganizationApi.GetAll)
        .shouldContainExactlyInAnyOrder(organization)
    }
  }
}
