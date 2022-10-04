package limber.endpoint

import io.kotest.matchers.shouldBe
import limber.api.OrganizationApi
import limber.rep.OrganizationRep
import limber.testing.IntegrationTest
import limber.testing.should.shouldHaveValidationErrors
import limber.testing.test
import org.junit.jupiter.api.Test

internal class CreateOrganizationTest : IntegrationTest() {
  @Test
  fun `name, too short`() {
    test {
      shouldHaveValidationErrors("body.name" to "size must be between 3 and 255") {
        val creator = OrganizationRep.Creator(name = " Li ")
        organizationClient(OrganizationApi.Create(creator))
      }
    }
  }

  @Test
  fun `name, too long`() {
    test {
      shouldHaveValidationErrors("body.name" to "size must be between 3 and 255") {
        val creator = OrganizationRep.Creator(name = "A".repeat(256))
        organizationClient(OrganizationApi.Create(creator))
      }
    }
  }

  @Test
  fun happy() {
    test {
      val creator = OrganizationRep.Creator(name = " Limber ")
      val organization = OrganizationRep(guid = guidGenerator[0], name = "Limber")
      organizationClient(OrganizationApi.Create(creator))
        .shouldBe(organization)
      organizationClient(OrganizationApi.Get(organization.guid))
        .shouldBe(organization)
    }
  }
}
