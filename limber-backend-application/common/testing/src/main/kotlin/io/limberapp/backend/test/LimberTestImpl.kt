package io.limberapp.backend.test

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTCreator
import com.auth0.jwt.algorithms.Algorithm
import io.limberapp.backend.authorization.principal.Claims
import io.limberapp.backend.authorization.principal.Jwt
import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.backend.authorization.principal.JwtUser
import io.limberapp.framework.testing.LimberTest
import io.limberapp.framework.testing.TestLimberApp
import java.util.UUID

class LimberTestImpl(limberApp: TestLimberApp) : LimberTest(limberApp) {

    override fun createJwt(): String {
        return JWT.create().withJwt(
            jwt = Jwt(
                orgs = emptyMap(),
                roles = setOf(JwtRole.SUPERUSER),
                user = JwtUser(UUID.randomUUID(), "Jeff", "Hudson")
            )
        ).sign(Algorithm.none())
    }

    private fun JWTCreator.Builder.withJwt(jwt: Jwt): JWTCreator.Builder {
        withClaim(Claims.orgs, objectMapper.writeValueAsString(jwt.orgs))
        withClaim(Claims.roles, objectMapper.writeValueAsString(jwt.roles))
        withClaim(Claims.user, objectMapper.writeValueAsString(jwt.user))
        return this
    }
}
