package io.limberapp.api.jwtClaimsRequest

import io.ktor.http.HttpMethod
import io.limberapp.rep.jwtClaimsRequest.JwtClaimsRequestRep
import io.limberapp.restInterface.Endpoint

object JwtClaimsRequestApi {
  data class Post(val rep: JwtClaimsRequestRep.Creation?) : Endpoint(
      httpMethod = HttpMethod.Post,
      rawPath = "/jwt-claims-request",
      body = rep,
  )
}
