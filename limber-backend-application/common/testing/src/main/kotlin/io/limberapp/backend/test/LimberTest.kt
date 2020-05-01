package io.limberapp.backend.test

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTCreator
import com.auth0.jwt.algorithms.Algorithm
import com.piperframework.serialization.Json
import com.piperframework.testing.PiperTest
import io.ktor.application.Application
import io.ktor.http.auth.HttpAuthHeader
import io.limberapp.backend.authorization.principal.Claims
import io.limberapp.backend.authorization.principal.Jwt
import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.backend.authorization.principal.JwtUser
import java.util.UUID

class LimberTest(json: Json, moduleFunction: Application.() -> Unit) : PiperTest(json, moduleFunction) {
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
        withClaim(Claims.org, jwt.org?.let { json.stringify(it) })
        withClaim(Claims.roles, json.stringifySet(jwt.roles))
        withClaim(Claims.user, jwt.user?.let { json.stringify(it) })
        return this
    }
}
