package io.limberapp.api.user

import io.ktor.http.HttpMethod
import io.limberapp.rep.user.UserRep
import io.limberapp.restInterface.Endpoint
import java.util.UUID

object UserApi {
  data class Post(val rep: UserRep.Creation?) : Endpoint(
      httpMethod = HttpMethod.Post,
      rawPath = "/users",
      body = rep,
  )

  data class Get(val userGuid: UUID) : Endpoint(
      httpMethod = HttpMethod.Get,
      rawPath = "/users/$userGuid",
  )

  data class GetByOrgGuidAndEmailAddress(val orgGuid: UUID, val emailAddress: String) : Endpoint(
      httpMethod = HttpMethod.Get,
      rawPath = "/users",
      qp = listOf("orgGuid" to orgGuid.toString(), "emailAddress" to emailAddress),
  )

  data class GetByOrgGuid(val orgGuid: UUID) : Endpoint(
      httpMethod = HttpMethod.Get,
      rawPath = "/users",
      qp = listOf("orgGuid" to orgGuid.toString()),
  )

  data class Patch(val userGuid: UUID, val rep: UserRep.Update?) : Endpoint(
      httpMethod = HttpMethod.Patch,
      rawPath = "/users/$userGuid",
      body = rep,
  )

  data class Delete(val userGuid: UUID) : Endpoint(
      httpMethod = HttpMethod.Delete,
      rawPath = "/users/$userGuid",
  )
}
