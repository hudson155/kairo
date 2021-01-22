package io.limberapp.backend.api.typicodePost

import io.ktor.http.HttpMethod
import io.limberapp.backend.rep.typicodePost.TypicodePostRep
import io.limberapp.common.restInterface.Endpoint
import io.limberapp.common.util.url.enc

internal object TypicodePostApi {
  internal data class Post(val rep: TypicodePostRep.Creation) : Endpoint(
      httpMethod = HttpMethod.Post,
      path = "/posts",
      body = rep,
  )

  internal data class Get(val postId: Int) : Endpoint(
      httpMethod = HttpMethod.Get,
      path = "/posts/${enc(postId)}",
  )

  internal data class GetByUserId(val userId: Int) : Endpoint(
      httpMethod = HttpMethod.Get,
      path = "/posts",
      queryParams = listOf("userId" to enc(userId)),
  )

  internal object Conflict : Endpoint(
      httpMethod = HttpMethod.Get,
      path = "/conflict",
  )
}
