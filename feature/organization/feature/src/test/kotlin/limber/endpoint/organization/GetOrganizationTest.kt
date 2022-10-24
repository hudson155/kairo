package limber.endpoint.organization

import io.kotest.matchers.shouldBe
import limber.api.organization.OrganizationApi
import limber.fixture.organization.OrganizationFixture
import limber.fixture.organization.create
import limber.testing.IntegrationTest
import limber.testing.should.shouldNotBeFound
import limber.testing.test
import limber.testing.testSetup
import org.junit.jupiter.api.Test
import java.util.UUID

internal class GetOrganizationTest : IntegrationTest() {
  @Test
  fun `organization does not exist`() {
    val organizationGuid = UUID.randomUUID()

    test {
      shouldNotBeFound {
        organizationClient(OrganizationApi.Get(organizationGuid))
      }
    }
  }

  @Test
  fun `organization exists`() {
    val organization = testSetup("Create organization") {
      create(OrganizationFixture.acmeCo)
    }

    test {
      organizationClient(OrganizationApi.Get(organization.guid))
        .shouldBe(organization)
    }
  }
}
