package io.limberapp.backend.module.auth.model.jwtClaimsRequest

object JwtClaimsRequestModel {

    data class Creation(
        val firstName: String?,
        val lastName: String?,
        val emailAddress: String,
        val profilePhotoUrl: String?
    )

    data class Complete(
        val orgs: String,
        val roles: String,
        val user: String
    )
}
