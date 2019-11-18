package io.limberapp.backend.module.auth.service.jwtClaimsRequest

import io.limberapp.backend.module.auth.model.jwtClaimsRequest.JwtClaimsModel
import io.limberapp.backend.module.auth.model.jwtClaimsRequest.JwtClaimsRequestModel

interface JwtClaimsRequestService {

    fun requestJwtClaims(model: JwtClaimsRequestModel): JwtClaimsModel
}
