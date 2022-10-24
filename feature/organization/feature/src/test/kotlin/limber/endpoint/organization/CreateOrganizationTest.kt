package limber.endpoint.organization

import io.kotest.matchers.shouldBe
import limber.api.organization.OrganizationApi
import limber.fixture.organization.OrganizationFixture
import limber.testing.IntegrationTest
import limber.testing.should.shouldHaveValidationErrors
import limber.testing.test
import org.junit.jupiter.api.Test

internal class CreateOrganizationTest : IntegrationTest() {
  @Test
  fun `name, too short`() {
    test {
      shouldHaveValidationErrors("body.name" to "size must be between 3 and 255") {
        val creator = OrganizationFixture.acmeCo.creator.copy(name = " Li ")
        organizationClient(OrganizationApi.Create(creator))
      }
    }
  }

  @Test
  fun `name, too long`() {
    test {
      shouldHaveValidationErrors("body.name" to "size must be between 3 and 255") {
        val creator = OrganizationFixture.acmeCo.creator.copy(name = "A".repeat(256))
        organizationClient(OrganizationApi.Create(creator))
      }
    }
  }

  @Test
  fun happy() {
    test {
      val organization = organizationClient(OrganizationApi.Create(OrganizationFixture.acmeCo.creator))
      organization.shouldBe(OrganizationFixture.acmeCo(guid = guidGenerator[0]))
      organizationClient(OrganizationApi.Get(organization.guid))
        .shouldBe(organization)
    }
  }
}
