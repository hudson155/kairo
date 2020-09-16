package io.limberapp.backend.module.users.api.user

import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.common.restInterface.HttpMethod
import io.limberapp.common.restInterface.LimberEndpoint
import io.limberapp.common.types.UUID
import io.limberapp.common.util.url.enc

@Suppress("StringLiteralDuplication")
object UserApi {
  data class Post(val rep: UserRep.Creation?) : LimberEndpoint(
    httpMethod = HttpMethod.POST,
    path = "/users",
    body = rep
  )

  data class Get(val userGuid: UUID) : LimberEndpoint(
    httpMethod = HttpMethod.GET,
    path = "/users/${enc(userGuid)}"
  )

  data class GetByOrgGuidAndEmailAddress(val orgGuid: UUID, val emailAddress: String) : LimberEndpoint(
    httpMethod = HttpMethod.GET,
    path = "/users",
    queryParams = listOf("orgGuid" to enc(orgGuid), "emailAddress" to enc(emailAddress))
  )

  data class GetByOrgGuid(val orgGuid: UUID) : LimberEndpoint(
    httpMethod = HttpMethod.GET,
    path = "/users",
    queryParams = listOf("orgGuid" to enc(orgGuid))
  )

  data class Patch(val userGuid: UUID, val rep: UserRep.Update?) : LimberEndpoint(
    httpMethod = HttpMethod.PATCH,
    path = "/users/${enc(userGuid)}",
    body = rep
  )

  data class Delete(val userGuid: UUID) : LimberEndpoint(
    httpMethod = HttpMethod.DELETE,
    path = "/users/${enc(userGuid)}"
  )
}
