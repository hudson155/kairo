package limber.endpoint

import io.kotest.matchers.shouldBe
import limber.api.OrganizationApi
import limber.api.OrganizationHostnameApi
import limber.rep.OrganizationHostnameRep
import limber.rep.OrganizationRep
import limber.testing.IntegrationTest
import limber.testing.should.shouldNotBeFound
import limber.testing.test
import limber.testing.testSetup
import org.junit.jupiter.api.Test

internal class GetOrganizationByHostnameTest : IntegrationTest() {
  @Test
  fun `organization does not exist`() {
    val hostname = "foo.bar.baz"

    test {
      shouldNotBeFound {
        organizationClient(OrganizationApi.GetByHostname(hostname))
      }
    }
  }

  @Test
  fun `organization exists`() {
    val organization = testSetup("Create organization") {
      val creator = OrganizationRep.Creator(name = "Limber")
      organizationClient(OrganizationApi.Create(creator))
      return@testSetup OrganizationRep(guid = guidGenerator[0], name = "Limber")
    }

    testSetup("Create hostname") {
      val creator = OrganizationHostnameRep.Creator(hostname = "foo.bar.baz")
      hostnameClient.invoke(OrganizationHostnameApi.Create(organization.guid, creator))
      return@testSetup OrganizationHostnameRep(
        guid = guidGenerator[1],
        organizationGuid = organization.guid,
        hostname = "foo.bar.baz",
      )
    }

    test {
      // Testing case-insensitivity.
      organizationClient(OrganizationApi.GetByHostname("FOO.BAR.BAZ"))
        .shouldBe(organization)
    }
  }
}
