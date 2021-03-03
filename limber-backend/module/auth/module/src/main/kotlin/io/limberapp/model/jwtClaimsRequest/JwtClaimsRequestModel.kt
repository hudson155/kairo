package io.limberapp.model.jwtClaimsRequest

data class JwtClaimsRequestModel(
    val auth0ClientId: String,
    val firstName: String,
    val lastName: String,
    val emailAddress: String,
    val profilePhotoUrl: String?,
)
