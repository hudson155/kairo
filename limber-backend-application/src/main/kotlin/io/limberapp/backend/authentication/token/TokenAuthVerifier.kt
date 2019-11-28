package io.limberapp.backend.authentication.token

import com.fasterxml.jackson.module.kotlin.readValue
import com.piperframework.jackson.objectMapper.PiperObjectMapper
import com.piperframework.ktorAuth.PiperAuthVerifier
import io.limberapp.backend.authorization.principal.Jwt
import io.limberapp.backend.module.auth.service.jwtClaimsRequest.JwtClaimsRequestService
import io.limberapp.backend.module.auth.service.personalAccessToken.PersonalAccessTokenService

class TokenAuthVerifier(
    private val jwtClaimsRequestService: JwtClaimsRequestService,
    private val personalAccessTokenService: PersonalAccessTokenService
) : PiperAuthVerifier<Jwt> {

    private val objectMapper = PiperObjectMapper()

    override fun verify(blob: String): Jwt? {
        val personalAccessToken = personalAccessTokenService.getByToken(blob) ?: return null
        val claims = jwtClaimsRequestService.requestJwtClaimsForExistingUser(personalAccessToken.userId) ?: return null
        return Jwt(
            orgs = objectMapper.readValue(claims.orgs),
            roles = objectMapper.readValue(claims.roles),
            user = objectMapper.readValue(claims.user)
        )
    }

    companion object {
        const val scheme = "Token"
    }
}
