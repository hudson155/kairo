package io.limberapp.backend.module.users.api.user

import com.piperframework.restInterface.HttpMethod
import com.piperframework.restInterface.PiperEndpoint
import com.piperframework.types.UUID
import com.piperframework.util.enc
import io.limberapp.backend.module.users.rep.account.UserRep

@Suppress("StringLiteralDuplication")
object UserApi {
  data class Post(val rep: UserRep.Creation?) : PiperEndpoint(
    httpMethod = HttpMethod.POST,
    path = "/users",
    body = rep
  )

  data class Get(val userGuid: UUID) : PiperEndpoint(
    httpMethod = HttpMethod.GET,
    path = "/users/${enc(userGuid)}"
  )

  data class GetByOrgGuidAndEmailAddress(val orgGuid: UUID, val emailAddress: String) : PiperEndpoint(
    httpMethod = HttpMethod.GET,
    path = "/users",
    queryParams = listOf("orgGuid" to enc(orgGuid), "emailAddress" to enc(emailAddress))
  )

  data class GetByOrgGuid(val orgGuid: UUID) : PiperEndpoint(
    httpMethod = HttpMethod.GET,
    path = "/users",
    queryParams = listOf("orgGuid" to enc(orgGuid))
  )

  data class Patch(val userGuid: UUID, val rep: UserRep.Update?) : PiperEndpoint(
    httpMethod = HttpMethod.PATCH,
    path = "/users/${enc(userGuid)}",
    body = rep
  )

  data class Delete(val userGuid: UUID) : PiperEndpoint(
    httpMethod = HttpMethod.DELETE,
    path = "/users/${enc(userGuid)}"
  )
}
