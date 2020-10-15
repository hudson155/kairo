package io.limberapp.backend.module.users.api.account

import io.ktor.http.HttpMethod
import io.limberapp.common.restInterface.LimberEndpoint
import io.limberapp.permissions.AccountRole
import io.limberapp.util.url.enc
import java.util.*

@Suppress("StringLiteralDuplication")
object UserRoleApi {
  data class Put(val userGuid: UUID, val role: AccountRole) : LimberEndpoint(
      httpMethod = HttpMethod.Put,
      path = "/users/${enc(userGuid)}/roles/${enc(role)}"
  )

  data class Delete(val userGuid: UUID, val role: AccountRole) : LimberEndpoint(
      httpMethod = HttpMethod.Delete,
      path = "/users/${enc(userGuid)}/roles/${enc(role)}"
  )
}
