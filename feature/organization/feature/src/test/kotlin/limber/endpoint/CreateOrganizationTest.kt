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
  fun `name is too short`() {
    integrationTest {
      shouldHaveValidationErrors("body.name" to "size must be between 3 and 255") {
        organizationClient(OrganizationApi.Create(OrganizationRep.Creator(name = "Li")))
      }
    }
  }

  @Test
  fun `name is too long`() {
    integrationTest {
      shouldHaveValidationErrors("body.name" to "size must be between 3 and 255") {
        organizationClient(OrganizationApi.Create(OrganizationRep.Creator(name = "A".repeat(256))))
      }
    }
  }

  @Test
  fun `happy path`() {
    integrationTest {
      val organizationGuid = guidGenerator[0]
      organizationClient(OrganizationApi.Create(OrganizationRep.Creator(name = " Limber ")))
        .shouldBe(OrganizationRep(guid = organizationGuid, name = "Limber"))
    }
  }
}
