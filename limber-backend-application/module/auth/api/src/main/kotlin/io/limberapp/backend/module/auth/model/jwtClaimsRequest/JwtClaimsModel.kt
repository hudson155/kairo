package io.limberapp.backend.module.auth.model.jwtClaimsRequest

data class JwtClaimsModel(
    val orgs: String,
    val roles: String,
    val user: String
)
