package io.limberapp.backend.module.auth.api.accessToken

import com.piperframework.restInterface.HttpMethod
import com.piperframework.restInterface.PiperEndpoint
import com.piperframework.types.UUID
import com.piperframework.util.enc

@Suppress("StringLiteralDuplication")
object AccessTokenApi {
    data class Post(val accountId: UUID) : PiperEndpoint(
        httpMethod = HttpMethod.POST,
        path = "/accounts/${enc(accountId)}/access-tokens"
    )

    data class GetByAccountId(val accountId: UUID) : PiperEndpoint(
        httpMethod = HttpMethod.GET,
        path = "/accounts/${enc(accountId)}/access-tokens"
    )

    data class Delete(val accountId: UUID, val accessTokenId: UUID) : PiperEndpoint(
        httpMethod = HttpMethod.DELETE,
        path = "/accounts/${enc(accountId)}/access-tokens/${enc(accessTokenId)}"
    )
}
