package io.limberapp.backend.module.auth.api.accessToken

import com.piperframework.restInterface.HttpMethod
import com.piperframework.restInterface.PiperEndpoint
import com.piperframework.types.UUID
import com.piperframework.util.enc

@Suppress("StringLiteralDuplication")
object AccessTokenApi {
    data class Post(val accountGuid: UUID) : PiperEndpoint(
        httpMethod = HttpMethod.POST,
        path = "/accounts/${enc(accountGuid)}/access-tokens"
    )

    data class GetByAccountGuid(val accountGuid: UUID) : PiperEndpoint(
        httpMethod = HttpMethod.GET,
        path = "/accounts/${enc(accountGuid)}/access-tokens"
    )

    data class Delete(val accountGuid: UUID, val accessTokenGuid: UUID) : PiperEndpoint(
        httpMethod = HttpMethod.DELETE,
        path = "/accounts/${enc(accountGuid)}/access-tokens/${enc(accessTokenGuid)}"
    )
}
