package io.limberapp.backend.module.auth.client.jwtClaimsRequest

import com.fasterxml.jackson.module.kotlin.readValue
import com.google.inject.Inject
import io.limberapp.backend.module.auth.api.jwtClaimsRequest.JwtClaimsRequestApi
import io.limberapp.backend.module.auth.rep.jwtClaimsRequest.JwtClaimsRequestRep
import io.limberapp.common.client.LimberHttpClient
import io.limberapp.common.client.LimberHttpClientRequestBuilder

class JwtClaimsRequestClient @Inject constructor(private val httpClient: LimberHttpClient) {
  suspend operator fun invoke(
      endpoint: JwtClaimsRequestApi.Post,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ) = httpClient.request(endpoint, builder) {
    readValue<JwtClaimsRequestRep.Complete>(checkNotNull(it))
  }
}
