package io.limberapp.common.auth

import io.limberapp.common.auth.jwt.Jwt
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class Auth {
  private val logger: Logger = LoggerFactory.getLogger(Auth::class.java)

  internal fun authorize(jwt: Jwt?): Boolean {
    if (authorizeJwt(jwt)) return true
    if (jwt == null) return false
    if (authorizeOverride(jwt)) {
      // It's safe to log the deserialized JWT because it no longer contains the signature.
      logger.info("Overriding authorization for user with JWT: $jwt.")
      return true
    }
    return false
  }

  protected abstract fun authorizeJwt(jwt: Jwt?): Boolean

  protected open fun authorizeOverride(jwt: Jwt): Boolean = false

  object Allow : Auth() {
    override fun authorizeJwt(jwt: Jwt?): Boolean = true
  }

  object Deny : Auth() {
    override fun authorizeJwt(jwt: Jwt?): Boolean = false
  }

  object AnyJwt : Auth() {
    override fun authorizeJwt(jwt: Jwt?): Boolean = jwt != null
  }

  class All(private vararg val auths: Auth) : Auth() {
    private val logger: Logger = LoggerFactory.getLogger(All::class.java)

    override fun authorizeJwt(jwt: Jwt?): Boolean {
      if (auths.isEmpty()) {
        logger.warn("Encountered an empty auth combination.")
        return false
      }
      return auths.all { it.authorize(jwt) }
    }
  }

  class Conditional(
      private val on: Boolean,
      private val ifTrue: Auth,
      private val ifFalse: Auth,
  ) : Auth() {
    override fun authorizeJwt(jwt: Jwt?): Boolean {
      val auth = if (on) ifTrue else ifFalse
      return auth.authorize(jwt)
    }
  }
}
