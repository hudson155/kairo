package io.limberapp.common.auth.principal

import io.ktor.auth.Principal
import io.limberapp.common.auth.jwt.Jwt

data class JwtPrincipal(
    internal val jwt: Jwt,
) : Principal
