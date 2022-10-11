package limber.endpoint

import io.kotest.matchers.shouldBe
import limber.api.OrganizationApi
import limber.api.OrganizationHostnameApi
import limber.rep.OrganizationHostnameRep
import limber.rep.OrganizationRep
import limber.testing.IntegrationTest
import limber.testing.should.shouldBeConflict
import limber.testing.should.shouldBeUnprocessable
import limber.testing.should.shouldHaveValidationErrors
import limber.testing.test
import limber.testing.testSetup
import org.junit.jupiter.api.Test
import java.util.UUID

internal class CreateOrganizationHostnameTest : IntegrationTest() {
  @Test
  fun `organization does not exist`() {
    val organizationGuid = UUID.randomUUID()

    test {
      shouldBeUnprocessable("Organization does not exist.") {
        val creator = OrganizationHostnameRep.Creator(hostname = "foo.bar.baz")
        hostnameClient(OrganizationHostnameApi.Create(organizationGuid, creator))
      }
    }
  }

  @Test
  fun `hostname, malformed`() {
    val organizationGuid = UUID.randomUUID()

    test {
      shouldHaveValidationErrors("body.hostname" to "must be a valid hostname") {
        val creator = OrganizationHostnameRep.Creator(hostname = "foo~bar~baz")
        hostnameClient(OrganizationHostnameApi.Create(organizationGuid, creator))
      }
    }
  }

  @Test
  fun `hostname, duplicate`() {
    val organizationGuid = testSetup("Create organization") {
      val creator = OrganizationRep.Creator(name = "Limber")
      organizationClient(OrganizationApi.Create(creator))
      return@testSetup guidGenerator[0]
    }

    testSetup("Create hostname") {
      val creator = OrganizationHostnameRep.Creator(hostname = "foo.bar.baz")
      hostnameClient(OrganizationHostnameApi.Create(organizationGuid, creator))
    }

    test {
      shouldBeConflict("Hostname already taken.") {
        val creator = OrganizationHostnameRep.Creator(hostname = "foo.bar.baz")
        hostnameClient(OrganizationHostnameApi.Create(organizationGuid, creator))
      }
    }
  }

  @Test
  fun happy() {
    val organizationGuid = testSetup("Create organization") {
      val creator = OrganizationRep.Creator(name = "Limber")
      organizationClient(OrganizationApi.Create(creator))
      return@testSetup guidGenerator[0]
    }

    test {
      val creator = OrganizationHostnameRep.Creator(hostname = " FOO.BAR.BAZ ")
      val hostname = OrganizationHostnameRep(
        organizationGuid = organizationGuid,
        guid = guidGenerator[1],
        hostname = "foo.bar.baz", // Hostname should be trimmed and lowercased.
      )
      hostnameClient(OrganizationHostnameApi.Create(organizationGuid, creator))
        .shouldBe(hostname)
      hostnameClient(OrganizationHostnameApi.Get(organizationGuid, hostname.guid))
        .shouldBe(hostname)
    }
  }
}
