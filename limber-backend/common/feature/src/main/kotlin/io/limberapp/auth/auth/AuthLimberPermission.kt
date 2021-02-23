package io.limberapp.auth.auth

import io.limberapp.auth.Auth
import io.limberapp.auth.jwt.Jwt
import io.limberapp.permissions.limber.LimberPermission

class AuthLimberPermission(private val permission: LimberPermission) : Auth() {
  override fun authorizeJwt(jwt: Jwt?): Boolean {
    jwt ?: return false
    return permission in jwt.permissions
  }
}
