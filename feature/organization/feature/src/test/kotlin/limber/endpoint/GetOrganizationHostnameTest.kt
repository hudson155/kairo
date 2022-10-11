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
import java.util.UUID

internal class GetOrganizationHostnameTest : IntegrationTest() {
  @Test
  fun `hostname does not exist`() {
    val organizationGuid = UUID.randomUUID()

    val hostnameGuid = UUID.randomUUID()

    test {
      shouldNotBeFound {
        hostnameClient(OrganizationHostnameApi.Get(organizationGuid, hostnameGuid))
      }
    }
  }

  @Test
  fun `hostname exists`() {
    val organizationGuid = testSetup("Create organization") {
      val creator = OrganizationRep.Creator(name = "Limber")
      organizationClient.invoke(OrganizationApi.Create(creator))
      return@testSetup guidGenerator[0]
    }

    val hostname = testSetup("Create hostname") {
      val creator = OrganizationHostnameRep.Creator(hostname = "foo.bar.baz")
      hostnameClient.invoke(OrganizationHostnameApi.Create(organizationGuid, creator))
      return@testSetup OrganizationHostnameRep(
        organizationGuid = organizationGuid,
        guid = guidGenerator[1],
        hostname = "foo.bar.baz",
      )
    }

    test {
      hostnameClient(OrganizationHostnameApi.Get(organizationGuid, hostname.guid))
        .shouldBe(hostname)
    }
  }
}
