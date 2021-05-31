package io.limberapp.model.jwtClaimsRequest

data class JwtClaimsRequestModel(
    val auth0ClientId: String,
    val fullName: String,
    val emailAddress: String,
    val profilePhotoUrl: String?,
)
