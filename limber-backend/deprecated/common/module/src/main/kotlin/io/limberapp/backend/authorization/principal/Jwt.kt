package io.limberapp.backend.authorization.principal

import io.ktor.auth.Principal
import io.limberapp.auth.jwt.JwtOrg
import io.limberapp.auth.jwt.JwtUser
import io.limberapp.permissions.AccountRole

data class Jwt(
    val org: JwtOrg?,
    val roles: Set<AccountRole>,
    val user: JwtUser?,
) : Principal
