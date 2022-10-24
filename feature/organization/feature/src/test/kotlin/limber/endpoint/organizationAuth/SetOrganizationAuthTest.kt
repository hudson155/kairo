package limber.endpoint.organizationAuth

import io.kotest.matchers.shouldBe
import limber.api.organizationAuth.OrganizationAuthApi
import limber.fixture.organization.OrganizationFixture
import limber.fixture.organization.create
import limber.fixture.organizationAuth.OrganizationAuthFixture
import limber.fixture.organizationAuth.create
import limber.testing.IntegrationTest
import limber.testing.should.shouldBeUnprocessable
import limber.testing.test
import limber.testing.testSetup
import org.junit.jupiter.api.Test
import java.util.UUID

internal class SetOrganizationAuthTest : IntegrationTest() {
  @Test
  fun `organization does not exist`() {
    val organizationGuid = UUID.randomUUID()

    test {
      shouldBeUnprocessable("Organization does not exist.") {
        val creator = OrganizationAuthFixture.acmeCo.creator
        authClient(OrganizationAuthApi.Set(organizationGuid, creator))
      }
    }
  }

  @Test
  fun `happy, initial auth`() {
    val organization = testSetup("Create organization") {
      create(OrganizationFixture.acmeCo)
    }

    test {
      val creator = OrganizationAuthFixture.acmeCo.creator
      authClient(OrganizationAuthApi.Set(organization.guid, creator))
        .shouldBe(OrganizationAuthFixture.acmeCo(organization.guid, guidGenerator[1]))
    }
  }

  @Test
  fun `happy, subsequent auth`() {
    val organization = testSetup("Create organization") {
      create(OrganizationFixture.acmeCo)
    }

    testSetup("Create auth") {
      create(organization.guid, OrganizationAuthFixture.acmeCo)
    }

    test {
      val creator = OrganizationAuthFixture.acmeCo.creator.copy(auth0OrganizationId = "org_bcdefghijklmnopq")
      val auth = authClient(OrganizationAuthApi.Set(organization.guid, creator))
      auth.shouldBe(
        OrganizationAuthFixture.acmeCo(organization.guid, guidGenerator[1])
          .copy(auth0OrganizationId = "org_bcdefghijklmnopq"),
      )
      authClient(OrganizationAuthApi.GetByOrganization(organization.guid))
        .shouldBe(auth)
    }
  }
}
