package limber.auth

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.ktor.server.auth.jwt.JWTPrincipal
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import limber.exception.AuthException
import limber.serialization.ObjectMapperFactory
import org.junit.jupiter.api.Test

internal class OrganizationAuthTest {
  private val objectMapper: ObjectMapper = ObjectMapperFactory.builder(ObjectMapperFactory.Format.Json).build()

  @Test
  fun `no token`() {
    val organizationId = "org_0"
    val context = context(null)
    val e = shouldThrow<AuthException> {
      test(context, OrganizationAuth(OrganizationPermission.Feature_Delete, organizationId))
    }
    e.status.shouldBe(AuthException.Status.Unauthorized)
    e.userMessage.shouldBe("No token provided.")
  }

  @Test
  fun `null permissions`() {
    val organizationId = "org_0"
    val context = context(principal(null))
    val e = shouldThrow<AuthException> {
      test(context, OrganizationAuth(OrganizationPermission.Feature_Delete, organizationId))
    }
    e.status.shouldBe(AuthException.Status.Unauthorized)
    e.userMessage.shouldBe("No permissions claim on the provided token.")
  }

  @Test
  fun `non-overlapping permissions`() {
    val organizationId = "org_0"
    val permissions = mapOf(
      OrganizationPermission.Feature_Create.value to PermissionValue.Some(setOf(organizationId)),
    )
    val context = context(principal(permissions))
    val e = shouldThrow<AuthException> {
      test(context, OrganizationAuth(OrganizationPermission.Feature_Delete, organizationId))
    }
    e.status.shouldBe(AuthException.Status.Forbidden)
    e.userMessage.shouldBe("Missing required permission feature:delete.")
  }

  @Test
  fun `different organization id`() {
    val organizationId = "org_0"
    val permissions = mapOf(
      OrganizationPermission.Feature_Create.value to PermissionValue.Some(setOf(organizationId)),
      OrganizationPermission.Feature_Delete.value to PermissionValue.Some(setOf(organizationId)),
    )
    val context = context(principal(permissions))
    val e = shouldThrow<AuthException> {
      test(context, OrganizationAuth(OrganizationPermission.Feature_Delete, "org_1"))
    }
    e.status.shouldBe(AuthException.Status.Forbidden)
    e.userMessage.shouldBe("Missing required permission feature:delete.")
  }

  @Test
  fun `same organization id`() {
    val organizationId = "org_0"
    val permissions = mapOf(
      OrganizationPermission.Feature_Create.value to PermissionValue.Some(setOf(organizationId)),
      OrganizationPermission.Feature_Delete.value to PermissionValue.Some(setOf(organizationId)),
    )
    val context = context(principal(permissions))
    shouldNotThrowAny {
      test(context, OrganizationAuth(OrganizationPermission.Feature_Delete, organizationId))
    }
  }

  @Test
  fun star() {
    val organizationId = "org_0"
    val permissions = mapOf(
      OrganizationPermission.Feature_Create.value to PermissionValue.Some(setOf(organizationId)),
      OrganizationPermission.Feature_Delete.value to PermissionValue.All,
    )
    val context = context(principal(permissions))
    shouldNotThrowAny {
      test(context, OrganizationAuth(OrganizationPermission.Feature_Delete, organizationId))
    }
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

  private fun test(context: RestContext, auth: OrganizationAuth) {
    runBlocking {
      withContext(context) {
        auth { auth }
      }
    }
  }
}
