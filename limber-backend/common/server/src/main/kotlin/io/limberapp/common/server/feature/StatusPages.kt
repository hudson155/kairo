package io.limberapp.common.server.feature

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.authentication
import io.ktor.features.StatusPages
import io.ktor.response.respond
import io.limberapp.common.exception.LimberException
import io.limberapp.common.exception.forbidden.ForbiddenException
import io.limberapp.common.exception.unauthorized.UnauthorizedException
import io.limberapp.common.server.Server
import org.slf4j.Logger
import org.slf4j.LoggerFactory

private val logger: Logger = LoggerFactory.getLogger(Server::class.java)

internal fun Application.configureStatusPages() {
  install(StatusPages) {
    exception(LimberException::class.java) {
      var e = it
      logger.debug("Client error.", e)
      if (e is UnauthorizedException) {
        val authenticationErrors = call.authentication.allErrors
        if (authenticationErrors.isNotEmpty()) {
          // An UnauthorizedException should be thrown if no principal was provided but one was
          // required. However, the principal may be null because when the AuthProvider tried to
          // parse the header, something went wrong. This is done in order to allow access to public
          // endpoints even if the JWT is invalid. If this happened, however, the proper response is
          // Forbidden rather than Unauthorized.
          logger.debug("Authentication errors identified: $authenticationErrors.")
          e = ForbiddenException()
        }
      }
      call.respond(e.statusCode, e.properties)
    }
  }
}
