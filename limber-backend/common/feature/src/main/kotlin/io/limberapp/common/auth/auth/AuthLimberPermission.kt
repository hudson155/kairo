package io.limberapp.common.auth.auth

import io.limberapp.common.auth.Auth
import io.limberapp.common.auth.jwt.Jwt
import io.limberapp.common.permissions.limberPermissions.LimberPermission

class AuthLimberPermission(private val permission: LimberPermission) : Auth() {
  override fun authorizeJwt(jwt: Jwt?): Boolean {
    jwt ?: return false
    return permission in jwt.permissions
  }
}
