package io.limberapp.backend.authorization.principal

import io.ktor.auth.Principal

data class Jwt(
    val org: JwtOrg?,
    val roles: List<JwtRole>,
    val user: JwtUser
) : Principal
