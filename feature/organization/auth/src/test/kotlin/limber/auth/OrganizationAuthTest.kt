package limber.auth

import com.fasterxml.jackson.core.type.TypeReference
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import java.util.UUID

internal class OrganizationAuthTest {
  @Test
  fun `null organization`() {
    val organizationGuid = UUID.randomUUID()
    val context = context(null)
    OrganizationAuth(organizationGuid).authorize(context)
      .shouldBeFalse()
  }

  @Test
  fun `different organization guid`() {
    val organizationGuid = UUID.randomUUID()
    val context = context(OrganizationClaim(UUID.randomUUID()))
    OrganizationAuth(organizationGuid).authorize(context)
      .shouldBeFalse()
  }

  @Test
  fun `same organization guid`() {
    val organizationGuid = UUID.randomUUID()
    val context = context(OrganizationClaim(organizationGuid))
    OrganizationAuth(organizationGuid).authorize(context)
      .shouldBeTrue()
  }

  private fun context(organization: OrganizationClaim?): RestContext =
    mockk {
      every { getClaim("organization", any<TypeReference<*>>()) } returns organization
    }
}
