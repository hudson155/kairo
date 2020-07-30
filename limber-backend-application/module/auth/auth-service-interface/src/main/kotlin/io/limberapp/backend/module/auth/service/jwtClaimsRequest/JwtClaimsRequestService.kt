package io.limberapp.backend.module.auth.service.jwtClaimsRequest

import io.limberapp.backend.LimberModule
import io.limberapp.backend.module.auth.model.jwtClaimsRequest.JwtClaimsModel
import io.limberapp.backend.module.auth.model.jwtClaimsRequest.JwtClaimsRequestModel

@LimberModule.Auth
interface JwtClaimsRequestService {
  /**
   * Generates JWT claims. If a user with the same email address does not exist, it creates a user.
   */
  fun requestJwtClaims(request: JwtClaimsRequestModel): JwtClaimsModel
}
