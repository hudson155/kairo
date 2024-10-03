package kairo.rest.auth

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.HttpHeaders
import io.ktor.http.auth.HttpAuthHeader
import io.ktor.http.auth.parseAuthorizationHeader
import io.ktor.http.parsing.ParseException
import io.ktor.server.auth.AuthenticationContext
import io.ktor.server.auth.AuthenticationProvider
import io.ktor.server.auth.parseAuthorizationHeader
import io.ktor.server.plugins.BadRequestException
import kairo.exception.KairoException
import kairo.rest.exception.DuplicateAuthorizationHeader
import kairo.rest.exception.MalformedAuthHeader
import kairo.rest.exception.Unauthorized
import kairo.rest.exception.UnrecognizedAuthScheme

private val logger: KLogger = KotlinLogging.logger {}

/**
 * A custom authentication provider is used for multiple reasons.
 * Firstly, Ktor's native JWT authentication provider doesn't support dynamic schemes and JWT issuers.
 * Secondly, this approach allows for custom implementations for API tokens.
 *
 * WARNING: Be careful not to log sensitive data in this class.
 */
public class KairoAuthenticationProvider(
  private val verifiers: Map<String, AuthVerifier<*>>,
) : AuthenticationProvider(Config(null)) {
  public class Config(name: Nothing?) : AuthenticationProvider.Config(name)

  public constructor(verifiers: List<AuthVerifier<*>>) : this(
    verifiers = verifiers
      .flatMap { verifier -> verifier.schemes.map { Pair(it.lowercase(), verifier) } }
      .toMap(),
  )

  override suspend fun onAuthenticate(context: AuthenticationContext) {
    try {
      val authHeader = getAuthHeader(context) ?: return
      val verifier = getVerifier(authHeader) ?: throw UnrecognizedAuthScheme()
      val principal = verifier.verify(authHeader)
      context.principal(name, principal)
    } catch (e: Exception) {
      logger.warn(e) { "Authentication failed." }
      @Suppress("InstanceOfCheckForException")
      if (e is KairoException) throw e
      throw Unauthorized(e)
    }
  }

  private fun getAuthHeader(context: AuthenticationContext): HttpAuthHeader.Single? {
    logger.debug { "Getting auth principal." }
    val authorizationHeaderValues = context.call.request.headers.getAll(HttpHeaders.Authorization) ?: return null
    if (authorizationHeaderValues.size != 1) throw DuplicateAuthorizationHeader()
    val authHeader = try {
      parseAuthorizationHeader(authorizationHeaderValues.single())
    } catch (cause: ParseException) {
      throw BadRequestException("Invalid auth header", cause)
    }
    context.call.request.parseAuthorizationHeader() ?: return null
    if (authHeader !is HttpAuthHeader.Single) throw MalformedAuthHeader()
    return authHeader
  }

  private fun getVerifier(authHeader: HttpAuthHeader.Single): AuthVerifier<*>? {
    logger.debug { "Getting verifier." }
    val scheme = authHeader.authScheme.lowercase()
    return verifiers[scheme]
  }
}
