package kairo.rest.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.ktor.http.Headers
import io.ktor.http.HeadersBuilder
import io.ktor.server.auth.AuthenticationContext
import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import java.time.Instant
import kairo.exception.UnauthorizedException
import kairo.rest.exception.DuplicateHeader
import kairo.rest.exception.ExpiredJwt
import kairo.rest.exception.JwtVerificationFailed
import kairo.rest.exception.MalformedJwt
import kairo.rest.exception.UnrecognizedAuthScheme
import kairo.rest.exception.UnrecognizedJwtIssuer
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * All thrown exceptions should extend [UnauthorizedException].
 *
 * Note that claim verification uses a real clock.
 */
@Suppress("ForbiddenMethodCall")
internal class KairoAuthenticationProviderTest {
  private val issuer: String = "https://example.com/"

  private val algorithm: Algorithm = Algorithm.HMAC256("Fake JWT secret.")

  private val provider: KairoAuthenticationProvider =
    KairoAuthenticationProvider(
      verifiers = listOf(
        JwtAuthVerifier(
          schemes = listOf("Bearer"),
          mechanisms = listOf(
            JwtJwtAuthMechanism(
              issuers = listOf(issuer),
              algorithm = algorithm,
              leewaySec = 0,
            ),
          ),
        ),
      ),
    )

  @Test
  fun `missing authorization header`(): Unit = runTest {
    val capturedJwt = authenticate {}
    with(capturedJwt) {
      isCaptured.shouldBeFalse()
    }
  }

  @Test
  fun `duplicate authorization header`(): Unit = runTest {
    val jwtString = createJwtString()
    shouldThrow<DuplicateHeader> {
      authenticate {
        append("Authorization", "Bearer $jwtString")
        append("Authorization", "Bearer $jwtString")
      }
    }
  }

  @Test
  fun `malformed jwt`(): Unit = runTest {
    shouldThrow<MalformedJwt> {
      authenticate {
        set("Authorization", "Bearer jwt")
      }
    }
  }

  @Test
  fun `unrecognized auth scheme`(): Unit = runTest {
    val jwtString = createJwtString()
    shouldThrow<UnrecognizedAuthScheme> {
      authenticate {
        set("Authorization", "Token $jwtString")
      }
    }
  }

  @Test
  fun `unrecognized jwt issuer`(): Unit = runTest {
    val jwtString = createJwtString(issuer = "https://unrecognized.com/")
    shouldThrow<UnrecognizedJwtIssuer> {
      authenticate {
        set("Authorization", "Bearer $jwtString")
      }
    }
  }

  @Test
  fun `unsigned jwt`(): Unit = runTest {
    val jwtString = createJwtString(algorithm = Algorithm.none())
    shouldThrow<JwtVerificationFailed> {
      authenticate {
        set("Authorization", "Bearer $jwtString")
      }
    }
  }

  @Test
  fun `invalid jwt signature`(): Unit = runTest {
    val jwtString = createJwtString(algorithm = Algorithm.HMAC256("incorrect"))
    shouldThrow<JwtVerificationFailed> {
      authenticate {
        set("Authorization", "Bearer $jwtString")
      }
    }
  }

  @Test
  fun `expired jwt`(): Unit = runTest {
    val jwtString = createJwtString(expiresAt = Instant.now())
    shouldThrow<ExpiredJwt> {
      authenticate {
        set("Authorization", "Bearer $jwtString")
      }
    }
  }

  @Test
  fun `jwt issued in future`(): Unit = runTest {
    val jwtString = createJwtString(issuedAt = Instant.now().plusSeconds(180))
    shouldThrow<JwtVerificationFailed> {
      authenticate {
        set("Authorization", "Bearer $jwtString")
      }
    }
  }

  @Test
  fun `jwt not valid yet`(): Unit = runTest {
    val jwtString = createJwtString(notBefore = Instant.now().plusSeconds(120))
    shouldThrow<JwtVerificationFailed> {
      authenticate {
        set("Authorization", "Bearer $jwtString")
      }
    }
  }

  @Test
  fun `happy path`(): Unit = runTest {
    val jwtString = createJwtString()
    val capturedJwt = authenticate {
      set("Authorization", "Bearer $jwtString")
    }
    with(capturedJwt) {
      isCaptured.shouldBeTrue()
      captured.shouldBe(JwtPrincipal(JWT.decode(jwtString)))
    }
  }

  @Suppress("LongParameterList")
  private fun createJwtString(
    issuer: String = this.issuer,
    expiresAt: Instant = Instant.now().plusSeconds(60),
    notBefore: Instant = Instant.now().minusSeconds(120),
    issuedAt: Instant = Instant.now().minusSeconds(180),
    algorithm: Algorithm = this.algorithm,
  ): String =
    JWT.create()
      .withIssuer(issuer)
      .withExpiresAt(expiresAt)
      .withNotBefore(notBefore)
      .withIssuedAt(issuedAt)
      .sign(algorithm)

  private suspend fun authenticate(builder: HeadersBuilder.() -> Unit): CapturingSlot<Principal> {
    val jwt = slot<Principal>()

    val context = mockk<AuthenticationContext> {
      every { call } returns mockk {
        every { request } returns mockk {
          every { headers } returns Headers.build(builder)
        }
      }
      every { principal(null, capture(jwt)) } returns Unit
    }
    provider.onAuthenticate(context)

    return jwt
  }
}
