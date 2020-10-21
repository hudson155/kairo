package io.limberapp.common.ktorAuth

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.auth.AuthenticationContext
import io.ktor.auth.AuthenticationFailedCause
import io.ktor.auth.AuthenticationPipeline
import io.ktor.auth.AuthenticationProvider
import io.ktor.auth.Principal
import io.ktor.auth.parseAuthorizationHeader
import io.ktor.http.auth.HttpAuthHeader
import io.ktor.request.ApplicationRequest
import io.ktor.util.pipeline.PipelineContext
import org.slf4j.LoggerFactory

/**
 * A Ktor [AuthenticationProvider] that uses multiple config-driven [LimberAuthVerifier] instances to handle
 * authentication.
 */
internal class LimberAuthProvider<P : Principal> internal constructor(
    private val config: LimberAuthConfig<P>,
) : AuthenticationProvider(config) {
  private val logger = LoggerFactory.getLogger(LimberAuthProvider::class.java)

  init {
    pipeline.intercept(AuthenticationPipeline.RequestAuthentication) { intercept(it) }
  }

  private fun PipelineContext<AuthenticationContext, ApplicationCall>.intercept(context: AuthenticationContext) {
    val token = call.request.parseAuthorizationHeaderOrNull() ?: return

    @Suppress("TooGenericExceptionCaught") // ktor-auth-jwt does this so we do it too.
    try {
      val principal = getPrincipal(token) ?: return
      context.principal(principal)
    } catch (e: Exception) {
      context.handleError(e)
    }
  }

  private fun ApplicationRequest.parseAuthorizationHeaderOrNull() = try {
    parseAuthorizationHeader()
  } catch (e: IllegalArgumentException) {
    logger.warn("Could not parse authorization header.", e)
    null
  }

  private fun getPrincipal(token: HttpAuthHeader): P? {
    if (token !is HttpAuthHeader.Single) return null
    val verifier = config.verifiers[token.authScheme] ?: return null
    return verifier.verify(token.blob)
  }

  private fun AuthenticationContext.handleError(e: Exception) {
    logger.warn("Authentication error.", e)
    val message = e.message ?: e.javaClass.simpleName
    error(config.authKey, AuthenticationFailedCause.Error(message))
  }
}
