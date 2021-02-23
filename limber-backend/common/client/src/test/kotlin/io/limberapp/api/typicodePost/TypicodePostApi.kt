package io.limberapp.api.typicodePost

import io.ktor.http.HttpMethod
import io.limberapp.rep.typicodePost.TypicodePostRep
import io.limberapp.restInterface.Endpoint

internal object TypicodePostApi {
  internal data class Post(
      val rep: TypicodePostRep.Creation,
  ) : Endpoint(HttpMethod.Post, "/posts", body = rep)

  internal data class Get(
      val postId: Int,
  ) : Endpoint(HttpMethod.Get, "/posts/$postId")

  internal data class GetByUserId(
      val userId: Int,
  ) : Endpoint(HttpMethod.Get, "/posts", qp = listOf("userId" to userId.toString()))

  internal object Conflict : Endpoint(HttpMethod.Get, "/conflict")
}
