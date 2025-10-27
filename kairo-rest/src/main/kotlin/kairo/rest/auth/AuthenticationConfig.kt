package kairo.rest.auth

import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.server.auth.AuthenticationConfig
import io.ktor.server.auth.BearerTokenCredential
import io.ktor.server.auth.bearer
import io.ktor.server.auth.jwt.JWTPrincipal
import kairo.rest.exception.ExpiredJwt
import kairo.rest.exception.JwtVerificationFailed

private val logger: KLogger = KotlinLogging.logger {}

public fun AuthenticationConfig.jwt(createVerifier: (credential: BearerTokenCredential) -> JWTVerifier) {
  bearer {
    authenticate { credential ->
      val verifier = createVerifier(credential)
      val payload = try {
        verifier.verify(credential.token)
      } catch (e: TokenExpiredException) {
        throw ExpiredJwt(e)
      } catch (e: JWTVerificationException) {
        throw JwtVerificationFailed(e)
      }
      return@authenticate JWTPrincipal(payload)
    }
  }
}
