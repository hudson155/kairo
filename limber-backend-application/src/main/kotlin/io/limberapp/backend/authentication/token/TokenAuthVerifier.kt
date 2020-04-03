package io.limberapp.backend.authentication.token

import com.piperframework.ktorAuth.PiperAuthVerifier
import com.piperframework.util.uuid.uuidFromBase64Encoded
import io.limberapp.backend.authorization.principal.Jwt
import io.limberapp.backend.authorization.principal.JwtOrg
import io.limberapp.backend.module.auth.service.accessToken.AccessTokenService
import io.limberapp.backend.module.auth.service.jwtClaimsRequest.JwtClaimsRequestService
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.parse

/**
 * Base64 encoded UUIDs are 24 characters in length. The last 2 characters are always '='. Access tokens are comprised
 * of back-to-back base64 encoded UUIDs, with these '=' characters removed. Therefore the length is 44 characters. The
 * first portion is the token ID, and the second portion is the token secret.
 */
private const val TOKEN_PART_LENGTH = 22

class TokenAuthVerifier(
    private val jwtClaimsRequestService: JwtClaimsRequestService,
    private val accessTokenService: AccessTokenService
) : PiperAuthVerifier<Jwt> {

    private val json = Json(JsonConfiguration.Stable)

    override fun verify(blob: String): Jwt? {
        if (blob.length != TOKEN_PART_LENGTH * 2) return null
        val accessTokenId = uuidFromBase64Encoded("${blob.substring(0, TOKEN_PART_LENGTH)}==")
        val accessTokenSecret = blob.substring(TOKEN_PART_LENGTH, TOKEN_PART_LENGTH * 2)
        val accessToken = accessTokenService.getIfValid(accessTokenId, accessTokenSecret) ?: return null
        val claims = jwtClaimsRequestService.requestJwtClaimsForExistingUser(accessToken.userId) ?: return null
        return Jwt(
            org = claims.org?.let { json.parse<JwtOrg>(it) },
            roles = json.parse(claims.roles),
            user = json.parse(claims.user)
        )
    }

    companion object {
        const val scheme = "Token"
    }
}
