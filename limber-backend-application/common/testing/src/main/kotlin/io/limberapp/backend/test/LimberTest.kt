package io.limberapp.backend.test

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTCreator
import com.auth0.jwt.algorithms.Algorithm
import com.piperframework.testing.PiperTest
import com.piperframework.testing.TestPiperApp
import io.ktor.http.auth.HttpAuthHeader
import io.limberapp.backend.authorization.principal.Claims
import io.limberapp.backend.authorization.principal.Jwt
import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.backend.authorization.principal.JwtUser
import java.util.UUID

class LimberTest(piperApp: TestPiperApp) : PiperTest(piperApp) {

    override fun createAuthHeader(): HttpAuthHeader? {
        val jwt = JWT.create().withJwt(
            jwt = Jwt(
                org = null,
                roles = setOf(JwtRole.SUPERUSER),
                user = JwtUser(UUID.randomUUID(), "Jeff", "Hudson")
            )
        ).sign(Algorithm.none())
        return HttpAuthHeader.Single("Bearer", jwt)
    }

    private fun JWTCreator.Builder.withJwt(jwt: Jwt): JWTCreator.Builder {
        withClaim(Claims.org, objectMapper.writeValueAsString(jwt.org))
        withClaim(Claims.roles, objectMapper.writeValueAsString(jwt.roles))
        withClaim(Claims.user, objectMapper.writeValueAsString(jwt.user))
        return this
    }
}
