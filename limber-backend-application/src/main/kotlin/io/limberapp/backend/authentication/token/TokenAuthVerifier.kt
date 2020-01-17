package io.limberapp.backend.authentication.token

import com.fasterxml.jackson.module.kotlin.readValue
import com.piperframework.jackson.objectMapper.PiperObjectMapper
import com.piperframework.ktorAuth.PiperAuthVerifier
import io.limberapp.backend.authorization.principal.Jwt
import io.limberapp.backend.module.auth.service.jwtClaimsRequest.JwtClaimsRequestService
import io.limberapp.backend.module.auth.service.accessToken.AccessTokenService

class TokenAuthVerifier(
    private val jwtClaimsRequestService: JwtClaimsRequestService,
    private val accessTokenService: AccessTokenService
) : PiperAuthVerifier<Jwt> {

    private val objectMapper = PiperObjectMapper()

    override fun verify(blob: String): Jwt? {
        val accessToken = accessTokenService.getByToken(blob) ?: return null
        val claims = jwtClaimsRequestService.requestJwtClaimsForExistingUser(accessToken.userId) ?: return null
        return Jwt(
            org = objectMapper.readValue(claims.org),
            roles = objectMapper.readValue(claims.roles),
            user = objectMapper.readValue(claims.user)
        )
    }

    companion object {
        const val scheme = "Token"
    }
}
