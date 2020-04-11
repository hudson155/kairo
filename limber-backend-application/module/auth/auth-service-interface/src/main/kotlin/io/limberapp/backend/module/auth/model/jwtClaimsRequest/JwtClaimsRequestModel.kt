package io.limberapp.backend.module.auth.model.jwtClaimsRequest

data class JwtClaimsRequestModel(
    val auth0ClientId: String,
    val firstName: String,
    val lastName: String,
    val emailAddress: String,
    val profilePhotoUrl: String?
)
