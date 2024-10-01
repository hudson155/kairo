package kairo.rest.auth

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.auth.HttpAuthHeader
import io.ktor.server.auth.AuthenticationContext
import io.ktor.server.auth.AuthenticationProvider
import io.ktor.server.auth.parseAuthorizationHeader
import kairo.exception.KairoException
import kairo.rest.exception.MalformedAuthHeader
import kairo.rest.exception.Unauthorized
import kairo.rest.exception.UnrecognizedAuthScheme

private val logger: KLogger = KotlinLogging.logger {}

/**
 * WARNING: Be careful not to log sensitive data in this class.
 */
public class KairoAuthenticationProvider(
  verifiers: List<AuthVerifier>,
) : AuthenticationProvider(Config(null)) {
  public class Config(name: Nothing?) : AuthenticationProvider.Config(name)

  private val verifiers: Map<String, AuthVerifier> =
    verifiers
      .flatMap { verifier -> verifier.schemes.map { Pair(it.lowercase(), verifier) } }
      .toMap()

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

  // TODO: Test happy path (missing)
  // TODO: Test happy path (present)
  // TODO: Test for multiple headers?
  // TODO: Test for invalid header, such as "Bearer abc def", "Bearer", "Bearer ", etc.
  // TODO: Test for malformed, invalid, expired, etc. JWT.

  private fun getAuthHeader(context: AuthenticationContext): HttpAuthHeader.Single? {
    logger.debug { "Getting auth principal." }
    val authHeader = context.call.request.parseAuthorizationHeader() ?: return null
    if (authHeader !is HttpAuthHeader.Single) throw MalformedAuthHeader()
    return authHeader
  }

  private fun getVerifier(authHeader: HttpAuthHeader.Single): AuthVerifier? {
    logger.debug { "Getting verifier." }
    val scheme = authHeader.authScheme.lowercase()
    return verifiers[scheme]
  }
}
