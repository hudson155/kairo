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

internal class PlatformPermissionAuthTest {
  private val objectMapper: ObjectMapper = ObjectMapperFactory.builder(ObjectMapperFactory.Format.Json).build()

  @Test
  fun `no token`() {
    val context = context(null)
    val e = shouldThrow<AuthException> {
      test(context, PlatformPermissionAuth(PlatformPermission.OrganizationDelete))
    }
    e.status.shouldBe(AuthException.Status.Unauthorized)
    e.userMessage.shouldBe("No token provided.")
  }

  @Test
  fun `null permissions`() {
    val context = context(principal(null))
    val e = shouldThrow<AuthException> {
      test(context, PlatformPermissionAuth(PlatformPermission.OrganizationDelete))
    }
    e.status.shouldBe(AuthException.Status.Unauthorized)
    e.userMessage.shouldBe("No permissions claim on the provided token.")
  }

  @Test
  fun `non-overlapping permissions`() {
    val permissions = listOf(PlatformPermission.OrganizationCreate)
    val context = context(principal(permissions))
    val e = shouldThrow<AuthException> {
      test(context, PlatformPermissionAuth(PlatformPermission.OrganizationDelete))
    }
    e.status.shouldBe(AuthException.Status.Forbidden)
    e.userMessage.shouldBe("Missing required permission organization:delete.")
  }

  @Test
  fun `overlapping permissions`() {
    val permissions = listOf(PlatformPermission.OrganizationCreate, PlatformPermission.OrganizationDelete)
    val context = context(principal(permissions))
    shouldNotThrowAny {
      test(context, PlatformPermissionAuth(PlatformPermission.OrganizationDelete))
    }
  }

  private fun context(principal: JWTPrincipal?): RestContext =
    RestContext(authorize = true, claimPrefix = "", principal = principal)

  private fun principal(permissions: List<PlatformPermission>?): JWTPrincipal =
    mockk {
      every { payload } returns mockk {
        every { getClaim("permissions") } returns mockk mockkClaim@{
          every { isMissing } returns (permissions == null)
          every { isNull } returns (permissions == null)
          every { this@mockkClaim.toString() } returns objectMapper.writeValueAsString(permissions)
        }
      }
    }

  private fun test(context: RestContext, auth: PlatformPermissionAuth) {
    runBlocking {
      withContext(context) {
        auth(auth)
      }
    }
  }
}
