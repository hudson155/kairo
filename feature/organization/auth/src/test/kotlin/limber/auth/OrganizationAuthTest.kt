package limber.auth

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.ktor.server.auth.jwt.JWTPrincipal
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import limber.exception.AuthException
import limber.serialization.ObjectMapperFactory
import org.junit.jupiter.api.Test
import java.util.UUID

internal class OrganizationAuthTest {
  private val objectMapper: ObjectMapper = ObjectMapperFactory.builder(ObjectMapperFactory.Format.Json).build()

  @Test
  fun `no token`() {
    val organizationGuid = UUID.randomUUID()
    val context = context(null)
    val e = shouldThrow<AuthException> {
      test(context, OrganizationAuth(organizationGuid, OrganizationPermission.FeatureDelete))
    }
    e.status.shouldBe(AuthException.Status.Unauthorized)
    e.userMessage.shouldBe("No token provided.")
  }

  @Test
  fun `null organization`() {
    val organizationGuid = UUID.randomUUID()
    val context = context(principal(null))
    val e = shouldThrow<AuthException> {
      test(context, OrganizationAuth(organizationGuid, OrganizationPermission.FeatureDelete))
    }
    e.status.shouldBe(AuthException.Status.Unauthorized)
    e.userMessage.shouldBe("No organization claim on the provided token.")
  }

  @Test
  fun `non-overlapping permissions`() {
    val organizationGuid = UUID.randomUUID()
    val permissions = listOf(OrganizationPermission.FeatureCreate)
    val context = context(principal(OrganizationClaim(organizationGuid, permissions)))
    val e = shouldThrow<AuthException> {
      test(context, OrganizationAuth(organizationGuid, OrganizationPermission.FeatureDelete))
    }
    e.status.shouldBe(AuthException.Status.Forbidden)
    e.userMessage.shouldBe("Missing required permission feature:delete.")
  }

  @Test
  fun `different organization guid`() {
    val organizationGuid = UUID.randomUUID()
    val permissions = listOf(
      OrganizationPermission.FeatureCreate,
      OrganizationPermission.FeatureDelete,
    )
    val context = context(principal(OrganizationClaim(organizationGuid, permissions)))
    test(context, OrganizationAuth(UUID.randomUUID(), OrganizationPermission.FeatureDelete))
      .shouldBeFalse()
  }

  @Test
  fun `same organization guid`() {
    val organizationGuid = UUID.randomUUID()
    val permissions = listOf(
      OrganizationPermission.FeatureCreate,
      OrganizationPermission.FeatureDelete,
    )
    val context = context(principal(OrganizationClaim(organizationGuid, permissions)))
    test(context, OrganizationAuth(organizationGuid, OrganizationPermission.FeatureDelete))
      .shouldBeTrue()
  }

  private fun context(principal: JWTPrincipal?): RestContext =
    RestContext(authorize = true, claimPrefix = "", principal = principal)

  private fun principal(organization: OrganizationClaim?): JWTPrincipal =
    mockk {
      every { payload } returns mockk {
        every { getClaim("organization") } returns mockk mockkClaim@{
          every { isMissing } returns (organization == null)
          every { isNull } returns (organization == null)
          every { this@mockkClaim.toString() } returns objectMapper.writeValueAsString(organization)
        }
      }
    }

  private fun test(context: RestContext, auth: OrganizationAuth): Boolean =
    runBlocking {
      withContext(context) {
        auth(auth) { return@withContext false }
        return@withContext true
      }
    }
}
