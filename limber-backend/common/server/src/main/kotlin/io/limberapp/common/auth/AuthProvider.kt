package io.limberapp.common.auth

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.auth.AuthenticationContext
import io.ktor.auth.AuthenticationFailedCause
import io.ktor.auth.AuthenticationPipeline
import io.ktor.auth.AuthenticationProvider
import io.ktor.http.auth.HttpAuthHeader
import io.ktor.http.auth.parseAuthorizationHeader
import io.ktor.http.parsing.ParseException
import io.ktor.request.authorization
import io.ktor.util.pipeline.PipelineContext
import io.limberapp.common.auth.exception.VerificationException
import io.limberapp.common.auth.principal.JwtPrincipal
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * A Ktor [AuthenticationProvider] that uses multiple [AuthVerifier] instances to handle
 * authentication. Each [AuthVerifier] will verify [JwtPrincipal]s with a certain "scheme", which is
 * the prefix in the Authorization header. For example, in "Authorization: Bearer: xxx", the scheme
 * is "Bearer".
 */
internal class AuthProvider(
    private val verifiers: Map<String, AuthVerifier>,
    private val authKey: String,
) : AuthenticationProvider(Config()) {
  private class Config : Configuration(null)

  private val logger: Logger = LoggerFactory.getLogger(AuthProvider::class.java)

  init {
    pipeline.intercept(AuthenticationPipeline.RequestAuthentication) { intercept(it) }
  }

  private fun PipelineContext<AuthenticationContext, ApplicationCall>.intercept(
      context: AuthenticationContext,
  ) {
    val authorization = call.request.authorization() ?: return
    try {
      val token = try {
        parseAuthorizationHeader(authorization)
      } catch (e: ParseException) {
        throw VerificationException.malformedAuthHeader(e)
      } ?: return
      val principal = getPrincipal(token)
      context.principal(principal)
    } catch (e: VerificationException) {
      context.handleError(e)
    }
  }

  private fun getPrincipal(token: HttpAuthHeader): JwtPrincipal {
    if (token !is HttpAuthHeader.Single) throw VerificationException.unsupportedAuthHeader()
    val verifier = verifiers[token.authScheme] ?: throw VerificationException.unknownScheme()
    return verifier.verify(token.blob)
  }

  private fun AuthenticationContext.handleError(e: VerificationException) {
    logger.warn("Authentication error.", e)
    error(authKey, AuthenticationFailedCause.Error(e.errorMessage))
  }
}
