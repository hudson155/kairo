package io.limberapp.backend.module.auth.service.jwtClaimsRequest

import io.limberapp.backend.module.auth.model.jwtClaimsRequest.JwtClaimsModel
import io.limberapp.backend.module.auth.model.jwtClaimsRequest.JwtClaimsRequestModel
import java.util.UUID

interface JwtClaimsRequestService {

    fun requestJwtClaims(request: JwtClaimsRequestModel): JwtClaimsModel

    fun requestJwtClaimsForExistingUser(userId: UUID): JwtClaimsModel?
}
