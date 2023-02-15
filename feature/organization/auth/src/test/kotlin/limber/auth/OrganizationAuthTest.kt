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
      test(context, OrganizationAuth(OrganizationPermission.FeatureDelete, organizationGuid))
    }
    e.status.shouldBe(AuthException.Status.Unauthorized)
    e.userMessage.shouldBe("No token provided.")
  }

  @Test
  fun `null permissions`() {
    val organizationGuid = UUID.randomUUID()
    val context = context(principal(null))
    val e = shouldThrow<AuthException> {
      test(context, OrganizationAuth(OrganizationPermission.FeatureDelete, organizationGuid))
    }
    e.status.shouldBe(AuthException.Status.Unauthorized)
    e.userMessage.shouldBe("No permissions claim on the provided token.")
  }

  @Test
  fun `non-overlapping permissions`() {
    val organizationGuid = UUID.randomUUID()
    val permissions = mapOf(
      OrganizationPermission.FeatureCreate.value to PermissionValue.Some(setOf(organizationGuid)),
    )
    val context = context(principal(permissions))
    val e = shouldThrow<AuthException> {
      test(context, OrganizationAuth(OrganizationPermission.FeatureDelete, organizationGuid))
    }
    e.status.shouldBe(AuthException.Status.Forbidden)
    e.userMessage.shouldBe("Missing required permission feature:delete.")
  }

  @Test
  fun `different organization guid`() {
    val organizationGuid = UUID.randomUUID()
    val permissions = mapOf(
      OrganizationPermission.FeatureCreate.value to PermissionValue.Some(setOf(organizationGuid)),
      OrganizationPermission.FeatureDelete.value to PermissionValue.Some(setOf(organizationGuid)),
    )
    val context = context(principal(permissions))
    test(context, OrganizationAuth(OrganizationPermission.FeatureDelete, UUID.randomUUID()))
      .shouldBeFalse()
  }

  @Test
  fun `same organization guid`() {
    val organizationGuid = UUID.randomUUID()
    val permissions = mapOf(
      OrganizationPermission.FeatureCreate.value to PermissionValue.Some(setOf(organizationGuid)),
      OrganizationPermission.FeatureDelete.value to PermissionValue.Some(setOf(organizationGuid)),
    )
    val context = context(principal(permissions))
    test(context, OrganizationAuth(OrganizationPermission.FeatureDelete, organizationGuid))
      .shouldBeTrue()
  }

  @Test
  fun star() {
    val organizationGuid = UUID.randomUUID()
    val permissions = mapOf(
      OrganizationPermission.FeatureCreate.value to PermissionValue.Some(setOf(organizationGuid)),
      OrganizationPermission.FeatureDelete.value to PermissionValue.All,
    )
    val context = context(principal(permissions))
    test(context, OrganizationAuth(OrganizationPermission.FeatureDelete, organizationGuid))
      .shouldBeTrue()
  }

  private fun context(principal: JWTPrincipal?): RestContext =
    RestContext(authorize = true, claimPrefix = "", principal = principal)

  private fun principal(permissions: Permissions?): JWTPrincipal =
    mockk {
      every { payload } returns mockk {
        every { getClaim("permissions") } returns mockk mockkClaim@{
          every { isMissing } returns (permissions == null)
          every { isNull } returns (permissions == null)
          every { this@mockkClaim.toString() } returns objectMapper.writeValueAsString(permissions)
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
