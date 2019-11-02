package io.limberapp.framework.endpoint.authorization.jwt

import com.auth0.jwt.JWTCreator
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.interfaces.Payload
import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.framework.jackson.objectMapper.LimberObjectMapper
import java.util.UUID

private object Claims {
    const val orgs = "https://limberapp.io/orgs"
    const val roles = "https://limberapp.io/roles"
    const val user = "https://limberapp.io/user"
}

data class Jwt(
    val orgs: Map<UUID, JwtOrg>,
    val roles: Set<JwtRole>,
    val user: JwtUser
)

private val objectMapper = LimberObjectMapper(prettyPrint = false)

internal fun jwtFromPayload(jwtPayload: Payload?): Jwt? {
    jwtPayload ?: return null
    return try {
        Jwt(
            orgs = objectMapper.readValue(jwtPayload.getClaim(Claims.orgs).asString()),
            roles = objectMapper.readValue(jwtPayload.getClaim(Claims.roles).asString()),
            user = objectMapper.readValue(jwtPayload.getClaim(Claims.user).asString())
        )
    } catch (_: JWTDecodeException) {
        null
    }
}

fun JWTCreator.Builder.withJwt(jwt: Jwt): JWTCreator.Builder {
    withClaim(Claims.orgs, objectMapper.writeValueAsString(jwt.orgs))
    withClaim(Claims.roles, objectMapper.writeValueAsString(jwt.roles))
    withClaim(Claims.user, objectMapper.writeValueAsString(jwt.user))
    return this
}
