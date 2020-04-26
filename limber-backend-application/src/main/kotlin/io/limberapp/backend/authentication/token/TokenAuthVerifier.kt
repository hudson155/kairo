package io.limberapp.backend.authentication.token

import com.piperframework.ktorAuth.PiperAuthVerifier
import com.piperframework.serialization.Json
import com.piperframework.util.uuid.uuidFromBase64Encoded
import io.limberapp.backend.authorization.principal.Jwt
import io.limberapp.backend.authorization.principal.JwtOrg
import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.backend.authorization.principal.JwtUser
import io.limberapp.backend.module.auth.service.accessToken.AccessTokenService
import io.limberapp.backend.module.auth.service.jwtClaimsRequest.JwtClaimsRequestService

/**
 * Base64 encoded UUIDs are 24 characters in length. The last 2 characters are always '='. Access tokens are comprised
 * of back-to-back base64 encoded UUIDs, with these '=' characters removed. Therefore the length is 44 characters. The
 * first portion is the token UUID, and the second portion is the token secret.
 */
private const val TOKEN_PART_LENGTH = 22

class TokenAuthVerifier(
    private val jwtClaimsRequestService: JwtClaimsRequestService,
    private val accessTokenService: AccessTokenService
) : PiperAuthVerifier<Jwt> {
    private val json = Json()

    override fun verify(blob: String): Jwt? {
        if (blob.length != TOKEN_PART_LENGTH * 2) return null
        val accessTokenGuid = uuidFromBase64Encoded("${blob.substring(0, TOKEN_PART_LENGTH)}==")
        val accessTokenSecret = blob.substring(TOKEN_PART_LENGTH, TOKEN_PART_LENGTH * 2)
        val accessToken = accessTokenService.getIfValid(accessTokenGuid, accessTokenSecret) ?: return null
        val claims = jwtClaimsRequestService.requestJwtClaimsForExistingUser(accessToken.userGuid) ?: return null
        return Jwt(
            org = claims.org?.let { json.parse<JwtOrg>(it) },
            roles = json.parseList<JwtRole>(claims.roles).toSet(),
            user = json.parse<JwtUser>(claims.user)
        )
    }

    companion object {
        const val scheme = "Token"
    }
}
