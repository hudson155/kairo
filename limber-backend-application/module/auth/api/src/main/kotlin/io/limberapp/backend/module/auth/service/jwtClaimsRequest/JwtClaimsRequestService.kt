package io.limberapp.backend.module.auth.service.jwtClaimsRequest

import io.limberapp.backend.module.auth.model.jwtClaimsRequest.JwtClaimsModel
import io.limberapp.backend.module.auth.model.jwtClaimsRequest.JwtClaimsRequestModel
import java.util.UUID

interface JwtClaimsRequestService {

    /**
     * Generates JWT claims. If a user does not exist, it creates one.
     */
    fun requestJwtClaims(request: JwtClaimsRequestModel): JwtClaimsModel

    /**
     * Generates JWT claims. If a user does not exist, it returns null.
     */
    fun requestJwtClaimsForExistingUser(userId: UUID): JwtClaimsModel?
}
