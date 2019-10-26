package io.limberapp.framework.endpoint.authorization

import com.auth0.jwt.interfaces.Payload
import io.limberapp.framework.endpoint.authorization.jwt.Jwt
import io.limberapp.framework.endpoint.authorization.jwt.JwtOrg
import io.limberapp.framework.endpoint.authorization.jwt.JwtUser
import java.util.UUID

internal fun jwtFromPayload(jwtPayload: Payload?): Jwt? {
    jwtPayload ?: return null
    return try {
        Jwt(
            orgs = jwtPayload.getClaim("orgs").asMap()
                .mapKeys { UUID.fromString(it.key) }
                .mapValues { JwtOrg(TODO()) },
            user = jwtPayload.getClaim("users").`as`(JwtUser::class.java)
        )
    } catch (e: Exception) {
        null
    }
}
