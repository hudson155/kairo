package limber.endpoint

import io.kotest.matchers.shouldBe
import limber.api.OrganizationApi
import limber.rep.OrganizationRep
import limber.testing.IntegrationTest
import limber.testing.integrationTest
import org.junit.jupiter.api.Test

internal class CreateOrganizationTest : IntegrationTest() {
  @Test
  fun `happy path`() {
    integrationTest {
      val organizationGuid = guidGenerator[0]
      organizationClient(OrganizationApi.Create(OrganizationRep.Creator(name = "Limber")))
        .shouldBe(OrganizationRep(guid = organizationGuid, name = "Limber"))
    }
  }
}
