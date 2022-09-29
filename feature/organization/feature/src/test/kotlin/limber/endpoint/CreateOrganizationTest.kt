package limber.endpoint

import io.kotest.matchers.shouldBe
import limber.api.OrganizationApi
import limber.rep.OrganizationRep
import limber.testing.IntegrationTest
import limber.testing.integrationTest
import limber.testing.should.shouldHaveValidationErrors
import org.junit.jupiter.api.Test

internal class CreateOrganizationTest : IntegrationTest() {
  @Test
  fun `name, too short`() {
    integrationTest {
      shouldHaveValidationErrors("body.name" to "size must be between 3 and 255") {
        organizationClient(OrganizationApi.Create(OrganizationRep.Creator(name = " Li ")))
      }
    }
  }

  @Test
  fun `name, too long`() {
    integrationTest {
      shouldHaveValidationErrors("body.name" to "size must be between 3 and 255") {
        organizationClient(OrganizationApi.Create(OrganizationRep.Creator(name = "A".repeat(256))))
      }
    }
  }

  @Test
  fun `hostname, malformed`() {
    integrationTest {
      shouldHaveValidationErrors("body.hostname" to "must be a valid hostname") {
        val creator = OrganizationRep.Creator(name = "Limber", hostname = "foo~bar~baz")
        organizationClient(OrganizationApi.Create(creator))
      }
    }
  }

  @Test
  fun `happy, no hostname`() {
    integrationTest {
      val organizationGuid = guidGenerator[0]
      val creator = OrganizationRep.Creator(name = " Limber ")
      organizationClient(OrganizationApi.Create(creator))
        .shouldBe(OrganizationRep(guid = organizationGuid, name = "Limber", hostnames = emptyList()))
    }
  }

  @Test
  fun `happy, with hostname`() {
    integrationTest {
      val organizationGuid = guidGenerator[0]
      val creator = OrganizationRep.Creator(name = " Limber ", hostname = "foo.bar.baz")
      organizationClient(OrganizationApi.Create(creator))
        .shouldBe(OrganizationRep(guid = organizationGuid, name = "Limber", hostnames = listOf("foo.bar.baz")))
    }
  }
}
