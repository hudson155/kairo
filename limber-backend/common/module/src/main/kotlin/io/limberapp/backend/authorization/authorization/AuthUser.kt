package io.limberapp.backend.authorization.authorization

import io.limberapp.backend.authorization.Auth
import io.limberapp.common.auth.jwt.Jwt
import java.util.*

class AuthUser(private val userGuid: UUID) : Auth() {
  override fun authorizeInternal(jwt: Jwt?): Boolean {
    jwt ?: return false
    val user = jwt.user ?: return false
    return user.guid == userGuid
  }
}
