package io.limberapp.auth.jwt

import com.fasterxml.jackson.annotation.JsonValue
import io.ktor.auth.Principal

data class JwtPrincipal(
    @JsonValue
    internal val jwt: Jwt,
) : Principal
