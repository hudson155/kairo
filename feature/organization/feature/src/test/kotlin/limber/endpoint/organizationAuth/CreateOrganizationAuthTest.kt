package limber.endpoint.organizationAuth

import io.kotest.matchers.shouldBe
import limber.api.organizationAuth.OrganizationAuthApi
import limber.fixture.organization.OrganizationFixture
import limber.fixture.organization.create
import limber.fixture.organizationAuth.OrganizationAuthFixture
import limber.fixture.organizationAuth.create
import limber.testing.IntegrationTest
import limber.testing.should.shouldBeConflict
import limber.testing.should.shouldBeUnprocessable
import limber.testing.test
import limber.testing.testSetup
import org.junit.jupiter.api.Test

internal class CreateOrganizationAuthTest : IntegrationTest() {
  @Test
  fun `organization does not exist`() {
    val organizationId = "org_0"

    test {
      shouldBeUnprocessable("Organization does not exist.") {
        val creator = OrganizationAuthFixture.acmeCo.creator
        authClient(OrganizationAuthApi.Create(organizationId, creator))
      }
    }
  }

  @Test
  fun `organization id, duplicate`() {
    val organization = testSetup("Create organization") {
      create(OrganizationFixture.acmeCo)
    }

    testSetup("Create auth") {
      create(organization.id, OrganizationAuthFixture.acmeCo)
    }

    test {
      shouldBeConflict("Organization already has auth.") {
        authClient(OrganizationAuthApi.Create(organization.id, OrganizationAuthFixture.universalExports.creator))
      }
    }
  }

  @Test
  fun `auth0 organization name, duplicate`() {
    val (acmeCo, universalExports) = testSetup("Create organizations") {
      listOf(OrganizationFixture.acmeCo, OrganizationFixture.universalExports).map { create(it) }
    }

    testSetup("Create auth") {
      create(acmeCo.id, OrganizationAuthFixture.acmeCo)
    }

    test {
      shouldBeConflict("Auth0 organization name already taken.") {
        authClient(OrganizationAuthApi.Create(universalExports.id, OrganizationAuthFixture.acmeCo.creator))
      }
    }
  }

  @Test
  fun happy() {
    val organization = testSetup("Create organization") {
      create(OrganizationFixture.acmeCo)
    }

    test {
      val creator = OrganizationAuthFixture.acmeCo.creator
      authClient(OrganizationAuthApi.Create(organization.id, creator))
        .shouldBe(OrganizationAuthFixture.acmeCo(organization.id, "auth_0", guidGenerator[0].toString()))
    }
  }
}
