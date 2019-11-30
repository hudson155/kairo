package io.limberapp.backend.module.auth.model.jwtClaimsRequest

data class JwtClaimsModel(
    val org: String,
    val roles: String,
    val user: String
)
