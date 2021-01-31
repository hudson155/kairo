package io.limberapp.common.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import io.limberapp.common.auth.jwt.Jwt
import io.limberapp.common.auth.jwt.JwtClaims
import io.limberapp.common.auth.principal.JwtPrincipal
import io.limberapp.common.config.AuthenticationConfig
import io.limberapp.common.config.AuthenticationMechanism
import io.limberapp.common.permissions.limberPermissions.LimberPermissions
import io.limberapp.common.serialization.LimberObjectMapper
import io.limberapp.common.typeConversion.typeConverter.LimberPermissionsTypeConverter
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class AuthTest {
  private val objectMapper: LimberObjectMapper =
      LimberObjectMapper(typeConverters = setOf(LimberPermissionsTypeConverter))

  private val path: String = "/test"

  private val bearerScheme: String = JwtAuthVerifier.scheme
  private val bearer2Scheme: String = JwtAuthVerifier.scheme + "2"
  private val unknownScheme: String = "Unknown"
  private val limberIssuer: String = "https://limberapp.io/"
  private val unknownIssuer: String = "https://unknown.com/"
  private val hmac256Secret: String = "HMAC256 secret"

  @Test
  fun `no authorization header`() {
    test(null) { statusCode, responseBody ->
      assertEquals(HttpStatusCode.Unauthorized, statusCode)
      assertEquals("Missing authorization header.", responseBody)
    }
    test("") { statusCode, responseBody ->
      assertEquals(HttpStatusCode.Unauthorized, statusCode)
      assertEquals("Missing authorization header.", responseBody)
    }
    test("   ") { statusCode, responseBody ->
      assertEquals(HttpStatusCode.Unauthorized, statusCode)
      assertEquals("Missing authorization header.", responseBody)
    }
  }

  @Test
  fun `Bearer scheme, null issuer`() {
    val authHeaderSettings = AuthHeaderSettings(
        scheme = bearerScheme,
        issuer = null,
        algorithm = Algorithm.none(),
    )
    test(authHeaderSettings,
        expectation = Expectation.Jwt)
    test(authHeaderSettings.copy(algorithm = Algorithm.HMAC256(hmac256Secret)),
        expectation = Expectation.InvalidJwt)
    test(authHeaderSettings.copy(scheme = unknownScheme),
        expectation = Expectation.UnknownScheme)
    test(authHeaderSettings.copy(issuer = unknownIssuer),
        expectation = Expectation.UnknownIssuer)
  }

  @Test
  fun `Bearer scheme, limber issuer`() {
    val authHeaderSettings = AuthHeaderSettings(
        scheme = bearerScheme,
        issuer = limberIssuer,
        algorithm = Algorithm.HMAC256(hmac256Secret),
    )
    test(authHeaderSettings,
        expectation = Expectation.Jwt)
    test(authHeaderSettings.copy(algorithm = Algorithm.none()),
        expectation = Expectation.InvalidJwt)
    test(authHeaderSettings.copy(scheme = unknownScheme),
        expectation = Expectation.UnknownScheme)
    test(authHeaderSettings.copy(issuer = unknownIssuer),
        expectation = Expectation.UnknownIssuer)
  }

  @Test
  fun `Bearer2 scheme, null issuer`() {
    val authHeaderSettings = AuthHeaderSettings(
        scheme = bearer2Scheme,
        issuer = null,
        algorithm = Algorithm.none(),
    )
    test(authHeaderSettings,
        expectation = Expectation.Jwt)
    test(authHeaderSettings.copy(algorithm = Algorithm.HMAC256(hmac256Secret)),
        expectation = Expectation.InvalidJwt)
    test(authHeaderSettings.copy(scheme = unknownScheme),
        expectation = Expectation.UnknownScheme)
    test(authHeaderSettings.copy(issuer = unknownIssuer),
        expectation = Expectation.UnknownIssuer)
  }

  @Test
  fun `Bearer2 scheme, limber issuer`() {
    val authHeaderSettings = AuthHeaderSettings(
        scheme = bearer2Scheme,
        issuer = limberIssuer,
        algorithm = Algorithm.HMAC256(hmac256Secret),
    )
    test(authHeaderSettings,
        expectation = Expectation.Jwt)
    test(authHeaderSettings.copy(algorithm = Algorithm.none()),
        expectation = Expectation.InvalidJwt)
    test(authHeaderSettings.copy(scheme = unknownScheme),
        expectation = Expectation.UnknownScheme)
    test(authHeaderSettings.copy(issuer = unknownIssuer),
        expectation = Expectation.UnknownIssuer)
  }

  @Test
  fun `Exceptional cases`() {
    // Invalid scheme.
    test("Star*scheme jwt.jwt.jwt.jwt.jwt") { statusCode, responseBody ->
      assertEquals(HttpStatusCode.Forbidden, statusCode)
      assertEquals("Malformed authorization header.", responseBody)
    }
    // Three parts.
    test("Bearer foo bar") { statusCode, responseBody ->
      assertEquals(HttpStatusCode.Forbidden, statusCode)
      assertEquals("Unsupported authorization header type.", responseBody)
    }
    // Too many segments to the JWT.
    test("Bearer jwt.jwt.jwt.jwt.jwt") { statusCode, responseBody ->
      assertEquals(HttpStatusCode.Forbidden, statusCode)
      assertEquals("Invalid JWT.", responseBody)
    }
    // JWT is missing Limber permissions.
    test("Bearer ${JWT.create().sign(Algorithm.none())}") { statusCode, responseBody ->
      assertEquals(HttpStatusCode.Forbidden, statusCode)
      assertEquals("Invalid JWT.", responseBody)
    }
  }

  private data class AuthHeaderSettings(
      val scheme: String,
      val issuer: String?,
      val algorithm: Algorithm,
  )

  private sealed class Expectation {
    object Jwt : Expectation()
    object UnknownScheme : Expectation()
    object UnknownIssuer : Expectation()
    object InvalidJwt : Expectation()
  }

  private fun test(authHeaderSettings: AuthHeaderSettings, expectation: Expectation) {
    val jwt = JWT.create().apply {
      authHeaderSettings.issuer?.let { issuer -> withIssuer(issuer) }
      withClaim(JwtClaims.permissions, LimberPermissions.none().asDarb())
    }.sign(authHeaderSettings.algorithm)
    val authorizationHeader = "${authHeaderSettings.scheme} $jwt"

    test(authorizationHeader) { statusCode, responseBody ->
      when (expectation) {
        is Expectation.Jwt -> {
          assertEquals(HttpStatusCode.OK, statusCode)
          assertEquals(Jwt(
              permissions = LimberPermissions.none(),
              org = null,
              features = null,
              user = null,
          ), objectMapper.readValue(responseBody))
        }
        is Expectation.UnknownScheme -> {
          assertEquals(HttpStatusCode.Forbidden, statusCode)
          assertEquals("Unknown authorization header scheme.", responseBody)
        }
        is Expectation.UnknownIssuer -> {
          assertEquals(HttpStatusCode.Forbidden, statusCode)
          assertEquals("Unknown JWT issuer.", responseBody)
        }
        is Expectation.InvalidJwt -> {
          assertEquals(HttpStatusCode.Forbidden, statusCode)
          assertEquals("Invalid JWT.", responseBody)
        }
      }
    }
  }

  private fun test(
      authorizationHeader: String?,
      block: (statusCode: HttpStatusCode, responseBody: String) -> Unit,
  ) {
    val (statusCode, responseBody) = withAuthorizationHeader(authorizationHeader)
    block(statusCode, responseBody)
  }

  private fun withAuthorizationHeader(authorizationHeader: String?): Pair<HttpStatusCode, String> =
      withApplication {
        val response = handleRequest(HttpMethod.Get, path) {
          authorizationHeader?.let { addHeader(HttpHeaders.Authorization, it) }
        }.response
        return@withApplication Pair(checkNotNull(response.status()), checkNotNull(response.content))
      }

  private fun <R> withApplication(function: TestApplicationEngine.() -> R): R {
    val authenticationConfig = AuthenticationConfig(listOf(
        AuthenticationMechanism.Jwt.Unsigned(leeway = 0),
        AuthenticationMechanism.Jwt.Signed(
            issuer = limberIssuer,
            leeway = 0,
            algorithm = AuthenticationMechanism.Jwt.Signed.Algorithm.HMAC256,
            secret = hmac256Secret,
        ),
    ))
    return withTestApplication(
        moduleFunction = {
          install(Authentication) {
            auth {
              verifier(
                  scheme = JwtAuthVerifier.scheme,
                  verifier = JwtAuthVerifier(authenticationConfig, objectMapper),
              )
              verifier(
                  scheme = "Bearer2",
                  verifier = JwtAuthVerifier(authenticationConfig, objectMapper),
              )
            }
          }
          install(Routing) {
            authenticate(optional = true) {
              route(path) {
                get {
                  val principal = call.authentication.principal as JwtPrincipal?
                  if (principal == null) {
                    val authenticationErrors = call.authentication.allErrors
                    if (authenticationErrors.isEmpty()) {
                      call.respond(HttpStatusCode.Unauthorized, "Missing authorization header.")
                      return@get
                    }
                    call.respond(
                        status = HttpStatusCode.Forbidden,
                        message = authenticationErrors.single().message,
                    )
                    return@get
                  }
                  @Suppress("BlockingMethodInNonBlockingContext")
                  call.respond(HttpStatusCode.OK, objectMapper.writeValueAsString(principal))
                }
              }
            }
          }
        },
        test = function,
    )
  }
}
