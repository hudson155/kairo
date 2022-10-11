package limber.endpoint

import io.kotest.matchers.shouldBe
import limber.api.OrganizationApi
import limber.api.OrganizationAuthApi
import limber.rep.OrganizationAuthRep
import limber.rep.OrganizationRep
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
        val creator = OrganizationAuthRep.Creator(auth0OrganizationId = "org_abcdefghijklmnop")
        authClient(OrganizationAuthApi.Set(organizationGuid, creator))
      }
    }
  }

  @Test
  fun `happy, initial auth`() {
    val organizationGuid = testSetup("Create organization") {
      val creator = OrganizationRep.Creator(name = "Limber")
      organizationClient(OrganizationApi.Create(creator))
      return@testSetup guidGenerator[0]
    }

    test {
      val creator = OrganizationAuthRep.Creator(auth0OrganizationId = "org_abcdefghijklmnop")
      val auth = OrganizationAuthRep(
        organizationGuid = organizationGuid,
        guid = guidGenerator[1],
        auth0OrganizationId = "org_abcdefghijklmnop",
      )
      authClient(OrganizationAuthApi.Set(organizationGuid, creator))
        .shouldBe(auth)
      authClient(OrganizationAuthApi.GetByOrganization(organizationGuid))
        .shouldBe(auth)
    }
  }

  @Test
  fun `happy, subsequent auth`() {
    val organizationGuid = testSetup("Create organization") {
      val creator = OrganizationRep.Creator(name = "Limber")
      organizationClient(OrganizationApi.Create(creator))
      return@testSetup guidGenerator[0]
    }

    testSetup("Create auth") {
      val creator = OrganizationAuthRep.Creator(auth0OrganizationId = "org_abcdefghijklmnop")
      OrganizationAuthRep(
        organizationGuid = organizationGuid,
        guid = guidGenerator[1],
        auth0OrganizationId = "org_abcdefghijklmnop",
      )
      authClient(OrganizationAuthApi.Set(organizationGuid, creator))
    }

    test {
      val creator = OrganizationAuthRep.Creator(auth0OrganizationId = "org_bcdefghijklmnopq")
      val auth = OrganizationAuthRep(
        organizationGuid = organizationGuid,
        guid = guidGenerator[1],
        auth0OrganizationId = "org_bcdefghijklmnopq",
      )
      authClient(OrganizationAuthApi.Set(organizationGuid, creator))
        .shouldBe(auth)
      authClient(OrganizationAuthApi.GetByOrganization(organizationGuid))
        .shouldBe(auth)
    }
  }
}
