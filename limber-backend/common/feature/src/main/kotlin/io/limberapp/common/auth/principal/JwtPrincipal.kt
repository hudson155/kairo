package io.limberapp.common.auth.principal

import com.fasterxml.jackson.annotation.JsonValue
import io.ktor.auth.Principal
import io.limberapp.common.auth.jwt.Jwt

data class JwtPrincipal(
    @JsonValue
    internal val jwt: Jwt,
) : Principal
