package io.limberapp.backend.authorization.principal

import io.ktor.auth.Principal
import io.limberapp.auth.jwt.Jwt

data class JwtPrincipal(
    internal val jwt: Jwt,
) : Principal {
  val userGuid = jwt.user?.guid
}
