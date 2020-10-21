package io.limberapp.backend.module.auth.service.jwtClaimsRequest

import io.limberapp.backend.module.auth.model.jwtClaimsRequest.JwtClaimsRequestModel
import io.limberapp.common.auth.jwt.Jwt

interface JwtClaimsRequestService {
  /**
   * Generates JWT claims. If a user with the same email address does not exist, it creates a user.
   */
  suspend fun requestJwtClaims(request: JwtClaimsRequestModel): Jwt
}
