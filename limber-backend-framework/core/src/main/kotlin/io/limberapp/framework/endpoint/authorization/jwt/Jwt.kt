package io.limberapp.framework.endpoint.authorization.jwt

import java.util.UUID

data class Jwt(
    val orgs: Map<UUID, JwtOrg>,
    val user: JwtUser
)
