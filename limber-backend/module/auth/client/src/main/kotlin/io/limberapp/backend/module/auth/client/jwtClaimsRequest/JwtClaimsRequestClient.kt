package io.limberapp.backend.module.auth.client.jwtClaimsRequest

import com.fasterxml.jackson.module.kotlin.readValue
import com.google.inject.Inject
import io.limberapp.backend.module.auth.api.jwtClaimsRequest.JwtClaimsRequestApi
import io.limberapp.common.auth.jwt.Jwt
import io.limberapp.common.client.LimberHttpClient
import io.limberapp.common.client.LimberHttpClientRequestBuilder

class JwtClaimsRequestClient @Inject constructor(private val httpClient: LimberHttpClient) {
  suspend operator fun invoke(
      endpoint: JwtClaimsRequestApi.Post,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ): Jwt =
      httpClient.request(endpoint, builder) { readValue(checkNotNull(it)) }
}
