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

internal class FeatureAuthTest {
  private val objectMapper: ObjectMapper = ObjectMapperFactory.builder(ObjectMapperFactory.Format.Json).build()

  @Test
  fun `no token`() {
    val featureGuid = UUID.randomUUID()
    val context = context(null)
    val e = shouldThrow<AuthException> {
      test(context, FeatureAuth(featureGuid))
    }
    e.status.shouldBe(AuthException.Status.Unauthorized)
    e.userMessage.shouldBe("No token provided.")
  }

  @Test
  fun `null features`() {
    val featureGuid = UUID.randomUUID()
    val context = context(principal(null))
    val e = shouldThrow<AuthException> {
      test(context, FeatureAuth(featureGuid))
    }
    e.status.shouldBe(AuthException.Status.Unauthorized)
    e.userMessage.shouldBe("No features claim on the provided token.")
  }

  @Test
  fun `non-overlapping features`() {
    val featureGuid = UUID.randomUUID()
    val context = context(principal(mapOf(featureGuid to FeatureClaim)))
    test(context, FeatureAuth(UUID.randomUUID())).shouldBeFalse()
  }

  @Test
  fun `overlapping features`() {
    val featureGuid = UUID.randomUUID()
    val context = context(principal(mapOf(featureGuid to FeatureClaim)))
    test(context, FeatureAuth(featureGuid)).shouldBeTrue()
  }

  private fun context(principal: JWTPrincipal?): RestContext =
    RestContext(authorize = true, claimPrefix = "", principal = principal)

  private fun principal(features: Map<UUID, FeatureClaim>?): JWTPrincipal =
    mockk {
      every { payload } returns mockk {
        every { getClaim("features") } returns mockk mockkClaim@{
          every { isMissing } returns (features == null)
          every { isNull } returns (features == null)
          every { this@mockkClaim.toString() } returns objectMapper.writeValueAsString(features)
        }
      }
    }

  private fun test(context: RestContext, auth: FeatureAuth): Boolean =
    runBlocking {
      withContext(context) {
        auth(auth) { return@withContext false }
        return@withContext true
      }
    }
}
