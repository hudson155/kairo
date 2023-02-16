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
import java.util.UUID

internal class CreateOrganizationAuthTest : IntegrationTest() {
  @Test
  fun `organization does not exist`() {
    val organizationGuid = UUID.randomUUID()

    test {
      shouldBeUnprocessable("Organization does not exist.") {
        val creator = OrganizationAuthFixture.acmeCo.creator
        authClient(OrganizationAuthApi.Create(organizationGuid, creator))
      }
    }
  }

  @Test
  fun `organization guid, duplicate`() {
    val organization = testSetup("Create organization") {
      create(OrganizationFixture.acmeCo)
    }

    testSetup("Create auth") {
      create(organization.guid, OrganizationAuthFixture.acmeCo)
    }

    test {
      shouldBeConflict("Organization already has auth.") {
        authClient(OrganizationAuthApi.Create(organization.guid, OrganizationAuthFixture.universalExports.creator))
      }
    }
  }

  @Test
  fun `auth0 organization name, duplicate`() {
    val (acmeCo, universalExports) = testSetup("Create organizations") {
      listOf(OrganizationFixture.acmeCo, OrganizationFixture.universalExports).map { create(it) }
    }

    testSetup("Create auth") {
      create(acmeCo.guid, OrganizationAuthFixture.acmeCo)
    }

    test {
      shouldBeConflict("Auth0 organization name already taken.") {
        authClient(OrganizationAuthApi.Create(universalExports.guid, OrganizationAuthFixture.acmeCo.creator))
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
      authClient(OrganizationAuthApi.Create(organization.guid, creator))
        .shouldBe(OrganizationAuthFixture.acmeCo(organization.guid, guidGenerator[1], guidGenerator[2]))
    }
  }
}
