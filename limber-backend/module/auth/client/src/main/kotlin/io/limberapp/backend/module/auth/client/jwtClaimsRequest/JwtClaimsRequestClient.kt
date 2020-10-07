package io.limberapp.backend.module.auth.client.jwtClaimsRequest

import io.limberapp.backend.module.auth.api.jwtClaimsRequest.JwtClaimsRequestApi
import io.limberapp.backend.module.auth.rep.jwtClaimsRequest.JwtClaimsRequestRep
import io.limberapp.client.LimberHttpClient
import io.limberapp.client.LimberHttpClientRequestBuilder

class JwtClaimsRequestClient(private val httpClient: LimberHttpClient) {
  suspend operator fun invoke(
    endpoint: JwtClaimsRequestApi.Post,
    builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ) = httpClient.request(endpoint, builder) {
    parse<JwtClaimsRequestRep.Complete>(checkNotNull(it))
  }
}
