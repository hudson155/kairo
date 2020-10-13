package io.limberapp.backend.module.auth.api.jwtClaimsRequest

import io.ktor.http.HttpMethod
import io.limberapp.backend.module.auth.rep.jwtClaimsRequest.JwtClaimsRequestRep
import io.limberapp.common.restInterface.LimberEndpoint

@Suppress("StringLiteralDuplication")
object JwtClaimsRequestApi {
  data class Post(val rep: JwtClaimsRequestRep.Creation?) : LimberEndpoint(
      httpMethod = HttpMethod.Post,
      path = "/jwt-claims-request",
      body = rep
  )
}
