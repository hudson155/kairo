package limber.feature.rest.ktorPlugins

import com.auth0.jwk.UrlJwkProvider
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import limber.feature.rest.applicationContext
import mu.KLogger
import mu.KotlinLogging

private val logger: KLogger = KotlinLogging.logger {}

internal fun Application.installSecurityPlugins() {
  val authConfig = applicationContext.config.auth

  if (authConfig != null) {
    install(Authentication) {
      jwt {
        verifier(UrlJwkProvider(authConfig.jwkDomain), authConfig.jwtIssuer) {
          acceptLeeway(authConfig.jwtLeeway)
        }
        validate { credential ->
          // No validation is performed here. See the [RestContext] class instead.
          JWTPrincipal(credential.payload)
        }
      }
    }
  } else {
    logger.warn { "******** WARNING: AUTH IS NOT ENABLED. NEVER DO THIS IN PRODUCTION. ********" }
  }
}
