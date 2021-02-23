package io.limberapp.auth.auth

import io.limberapp.auth.Auth
import io.limberapp.auth.jwt.Jwt
import io.limberapp.permissions.limber.LimberPermission
import java.util.UUID

class AuthUser(private val userGuid: UUID) : Auth() {
  override fun authorizeJwt(jwt: Jwt?): Boolean {
    jwt ?: return false
    val user = jwt.user ?: return false
    return user.guid == userGuid
  }

  override fun authorizeOverride(jwt: Jwt): Boolean {
    if (LimberPermission.SUPERUSER in jwt.permissions) return true
    return super.authorizeOverride(jwt)
  }
}
