package limber.endpoint.organizationAuth

import io.kotest.matchers.shouldBe
import limber.api.organizationAuth.OrganizationAuthApi
import limber.fixture.organization.OrganizationFixture
import limber.fixture.organization.create
import limber.fixture.organizationAuth.OrganizationAuthFixture
import limber.fixture.organizationAuth.create
import limber.testing.IntegrationTest
import limber.testing.should.shouldBeUnprocessable
import limber.testing.should.shouldNotBeFound
import limber.testing.test
import limber.testing.testSetup
import org.junit.jupiter.api.Test

internal class DeleteOrganizationAuthTest : IntegrationTest() {
  @Test
  fun `auth does not exist`() {
    val authId = "auth_0"

    test {
      shouldBeUnprocessable("Organization auth does not exist.") {
        authClient(OrganizationAuthApi.Delete(authId))
      }
    }
  }

  @Test
  fun `auth exists`() {
    val organization = testSetup("Create organization") {
      create(OrganizationFixture.acmeCo)
    }

    val auth = testSetup("Create auth") {
      create(organization.guid, OrganizationAuthFixture.acmeCo)
    }

    test {
      authClient(OrganizationAuthApi.Delete(auth.id))
        .shouldBe(auth)
      shouldNotBeFound {
        authClient(OrganizationAuthApi.GetByOrganization(organization.guid))
      }
    }
  }
}
