package limber.endpoint.organizationAuth

import io.kotest.matchers.shouldBe
import limber.api.organization.OrganizationApi
import limber.api.organizationAuth.OrganizationAuthApi
import limber.rep.organization.OrganizationRep
import limber.rep.organizationAuth.OrganizationAuthRep
import limber.testing.IntegrationTest
import limber.testing.should.shouldNotBeFound
import limber.testing.test
import limber.testing.testSetup
import org.junit.jupiter.api.Test
import java.util.UUID

internal class GetOrganizationAuthByOrganizationTest : IntegrationTest() {
  @Test
  fun `auth does not exist`() {
    val organizationGuid = UUID.randomUUID()

    test {
      shouldNotBeFound {
        authClient(OrganizationAuthApi.GetByOrganization(organizationGuid))
      }
    }
  }

  @Test
  fun `auth exists`() {
    val organizationGuid = testSetup("Create organization") {
      val creator = OrganizationRep.Creator(name = "Limber")
      organizationClient(OrganizationApi.Create(creator))
      return@testSetup guidGenerator[0]
    }

    val auth = testSetup("Create auth") {
      val creator = OrganizationAuthRep.Creator(auth0OrganizationId = "org_abcdefghijklmnop")
      authClient(OrganizationAuthApi.Set(organizationGuid, creator))
      return@testSetup OrganizationAuthRep(
        organizationGuid = organizationGuid,
        guid = guidGenerator[1],
        auth0OrganizationId = "org_abcdefghijklmnop",
      )
    }

    test {
      authClient(OrganizationAuthApi.GetByOrganization(organizationGuid))
        .shouldBe(auth)
    }
  }
}
