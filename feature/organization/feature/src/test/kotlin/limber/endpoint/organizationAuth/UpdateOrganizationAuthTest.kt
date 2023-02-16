package limber.endpoint.organizationAuth

import io.kotest.matchers.shouldBe
import limber.api.organizationAuth.OrganizationAuthApi
import limber.fixture.organization.OrganizationFixture
import limber.fixture.organization.create
import limber.fixture.organizationAuth.OrganizationAuthFixture
import limber.fixture.organizationAuth.create
import limber.rep.organizationAuth.OrganizationAuthRep
import limber.testing.IntegrationTest
import limber.testing.should.shouldBeUnprocessable
import limber.testing.test
import limber.testing.testSetup
import org.junit.jupiter.api.Test
import java.util.UUID

internal class UpdateOrganizationAuthTest : IntegrationTest() {
  @Test
  fun `auth does not exist`() {
    val authGuid = UUID.randomUUID()

    test {
      shouldBeUnprocessable("Organization auth does not exist.") {
        val updater = OrganizationAuthRep.Updater(auth0OrganizationName = " NEW-CO ")
        authClient(OrganizationAuthApi.Update(authGuid, updater))
      }
    }
  }

  @Test
  fun `no updates`() {
    val organization = testSetup("Create organization") {
      create(OrganizationFixture.acmeCo)
    }

    val auth = testSetup("Create auth") {
      create(organization.guid, OrganizationAuthFixture.acmeCo)
    }

    test {
      val updater = OrganizationAuthRep.Updater()
      authClient(OrganizationAuthApi.Update(auth.guid, updater))
        .shouldBe(auth)
      authClient(OrganizationAuthApi.Get(auth.guid))
        .shouldBe(auth)
    }
  }

  @Test
  fun `auth0 organization name, happy`() {
    val organization = testSetup("Create organization") {
      create(OrganizationFixture.acmeCo)
    }

    var auth = testSetup("Create auth") {
      create(organization.guid, OrganizationAuthFixture.acmeCo)
    }

    test {
      val updater = OrganizationAuthRep.Updater(auth0OrganizationName = " NEW-CO ")
      auth = auth.copy(auth0OrganizationName = "new-co")
      authClient(OrganizationAuthApi.Update(auth.guid, updater))
        .shouldBe(auth)
      authClient(OrganizationAuthApi.Get(auth.guid))
        .shouldBe(auth)
    }
  }
}
