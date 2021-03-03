package io.limberapp.service.jwtClaimsRequest

import io.limberapp.auth.jwt.Jwt
import io.limberapp.model.jwtClaimsRequest.JwtClaimsRequestModel

interface JwtClaimsRequestService {
  suspend fun requestJwtClaims(request: JwtClaimsRequestModel): Jwt
}
