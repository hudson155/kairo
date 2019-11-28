package io.limberapp.backend.authorization.principal

import io.ktor.auth.Principal
import java.util.UUID

data class Jwt(
    val orgs: Map<UUID, JwtOrg>,
    val roles: Set<JwtRole>,
    val user: JwtUser
) : Principal
