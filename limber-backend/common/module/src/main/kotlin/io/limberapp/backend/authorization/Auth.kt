package io.limberapp.backend.authorization

import io.limberapp.backend.authorization.principal.JwtPrincipal
import io.limberapp.common.auth.jwt.Jwt
import io.limberapp.common.authorization.LimberAuthorization
import io.limberapp.common.permissions.AccountRole
import org.slf4j.LoggerFactory

abstract class Auth : LimberAuthorization<JwtPrincipal> {
  private val logger = LoggerFactory.getLogger(Auth::class.java)

  final override fun authorize(principal: JwtPrincipal?) = authorize(principal?.jwt)

  fun authorize(jwt: Jwt?): Boolean {
    val authorized = authorizeInternal(jwt)
    if (authorized) return true
    if (jwt?.canOverride() == true) {
      logger.info("Overriding Authorization access"
          + " for user with ${jwt.user?.guid?.let { "UUID $it" } ?: "no UUID"}"
          + " and account roles ${jwt.roles}."
      )
      return true
    }
    return false
  }

  protected abstract fun authorizeInternal(jwt: Jwt?): Boolean

  private fun Jwt.canOverride() = overridingRoles.intersect(roles).isNotEmpty()

  open val overridingRoles = setOf(AccountRole.LIMBER_SERVER, AccountRole.SUPERUSER)

  object Allow : Auth() {
    override fun authorizeInternal(jwt: Jwt?) = true
  }

  object Deny : Auth() {
    override fun authorizeInternal(jwt: Jwt?) = false
  }

  object AnyJwt : Auth() {
    override fun authorizeInternal(jwt: Jwt?) = jwt != null
  }

  class All(private vararg val auths: Auth?) : Auth() {
    override fun authorizeInternal(jwt: Jwt?) =
        with(auths.filterNotNull()) { isNotEmpty() && all { it.authorize(jwt) } }

    override val overridingRoles = emptySet<AccountRole>()
  }

  class Conditional(
      private val on: Boolean,
      private val ifTrue: Auth,
      private val ifFalse: Auth,
  ) : Auth() {
    override fun authorizeInternal(jwt: Jwt?) =
        (if (on) ifTrue else ifFalse).authorize(jwt)

    override val overridingRoles = emptySet<AccountRole>()
  }
}
