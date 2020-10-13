package io.limberapp.backend.module.users.api.account

import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.common.restInterface.HttpMethod
import io.limberapp.common.restInterface.LimberEndpoint
import io.limberapp.common.util.url.enc

@Suppress("StringLiteralDuplication")
object UserApi {
  data class Post(val rep: UserRep.Creation?) : LimberEndpoint(
    httpMethod = HttpMethod.POST,
    path = "/users",
    body = rep
  )

  data class Get(val userGuid: String) : LimberEndpoint(
    httpMethod = HttpMethod.GET,
    path = "/users/${enc(userGuid)}"
  )

  data class GetByOrgGuidAndEmailAddress(val orgGuid: String, val emailAddress: String) : LimberEndpoint(
    httpMethod = HttpMethod.GET,
    path = "/users",
    queryParams = listOf("orgGuid" to enc(orgGuid), "emailAddress" to enc(emailAddress))
  )

  data class GetByOrgGuid(val orgGuid: String) : LimberEndpoint(
    httpMethod = HttpMethod.GET,
    path = "/users",
    queryParams = listOf("orgGuid" to enc(orgGuid))
  )

  data class Patch(val userGuid: String, val rep: UserRep.Update?) : LimberEndpoint(
    httpMethod = HttpMethod.PATCH,
    path = "/users/${enc(userGuid)}",
    body = rep
  )

  data class Delete(val userGuid: String) : LimberEndpoint(
    httpMethod = HttpMethod.DELETE,
    path = "/users/${enc(userGuid)}"
  )
}
