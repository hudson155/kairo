package limber.endpoint.organization

import io.kotest.matchers.shouldBe
import limber.api.organization.OrganizationApi
import limber.fixture.organization.OrganizationFixture
import limber.testing.IntegrationTest
import limber.testing.test
import org.junit.jupiter.api.Test

internal class CreateOrganizationTest : IntegrationTest() {
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
