package io.limberapp.backend.module.auth.model.jwtClaimsRequest

import com.fasterxml.jackson.annotation.JsonProperty
import io.limberapp.framework.endpoint.authorization.jwt.Claims

object JwtClaimsRequestModel {

    data class Creation(
        val firstName: String?,
        val lastName: String?,
        val emailAddress: String,
        val profilePhotoUrl: String?
    )

    data class Complete(
        @JsonProperty(Claims.orgs)
        val orgs: String,
        @JsonProperty(Claims.roles)
        val roles: String,
        @JsonProperty(Claims.user)
        val user: String
    )
}
