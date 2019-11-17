package io.limberapp.backend.module.auth.model.jwtClaimsRequest

import com.fasterxml.jackson.annotation.JsonProperty

object JwtClaimsRequestModel {

    data class Creation(
        val firstName: String?,
        val lastName: String?,
        val emailAddress: String,
        val profilePhotoUrl: String?
    )

    data class Complete(
        @JsonProperty("https://limberapp.io/orgs")
        val orgs: String,
        @JsonProperty("https://limberapp.io/roles")
        val roles: String,
        @JsonProperty("https://limberapp.io/user")
        val user: String
    )
}
