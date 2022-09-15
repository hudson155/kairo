package limber.endpoint

import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import limber.api.OrganizationApi
import limber.rep.OrganizationRep
import limber.testing.IntegrationTest
import limber.testing.integrationTest
import org.junit.jupiter.api.Test
import java.util.UUID

internal class GetOrganizationTest : IntegrationTest() {
  @Test
  fun `organization does not exist`() {
    integrationTest {
      organizationClient(OrganizationApi.Get(UUID.randomUUID()))
        .shouldBeNull()
    }
  }

  @Test
  fun `organization exists`() {
    integrationTest {
      val organizationGuid = guidGenerator[0]
      organizationClient(OrganizationApi.Create(OrganizationRep.Creator(name = "Limber")))
      organizationClient(OrganizationApi.Get(organizationGuid))
        .shouldBe(OrganizationRep(guid = organizationGuid, name = "Limber"))
    }
  }
}
