package io.limberapp.backend.module.users.api.user.role

import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.common.restInterface.HttpMethod
import io.limberapp.common.restInterface.LimberEndpoint
import io.limberapp.common.util.url.enc

@Suppress("StringLiteralDuplication")
object UserRoleApi {
  data class Put(val userGuid: String, val role: JwtRole) : LimberEndpoint(
    httpMethod = HttpMethod.PUT,
    path = "/users/${enc(userGuid)}/roles/${enc(role)}"
  )

  data class Delete(val userGuid: String, val role: JwtRole) : LimberEndpoint(
    httpMethod = HttpMethod.DELETE,
    path = "/users/${enc(userGuid)}/roles/${enc(role)}"
  )
}
