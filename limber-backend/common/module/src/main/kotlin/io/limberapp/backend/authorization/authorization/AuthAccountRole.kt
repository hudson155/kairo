package io.limberapp.backend.authorization.authorization

import io.limberapp.backend.authorization.Auth
import io.limberapp.common.auth.jwt.Jwt
import io.limberapp.common.permissions.AccountRole

class AuthAccountRole(private val role: AccountRole) : Auth() {
  override fun authorizeInternal(jwt: Jwt?): Boolean {
    jwt ?: return false
    return role in jwt.roles
  }

  override val overridingRoles =
      if (role == AccountRole.SUPERUSER) setOf(AccountRole.SUPERUSER) else super.overridingRoles
}
