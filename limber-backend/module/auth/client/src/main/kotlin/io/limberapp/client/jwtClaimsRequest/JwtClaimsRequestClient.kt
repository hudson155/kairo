package io.limberapp.client.jwtClaimsRequest

import com.fasterxml.jackson.module.kotlin.readValue
import com.google.inject.Inject
import com.google.inject.Singleton
import com.google.inject.name.Named
import io.limberapp.api.jwtClaimsRequest.JwtClaimsRequestApi
import io.limberapp.auth.jwt.Jwt
import io.limberapp.client.HttpClient
import io.limberapp.client.RequestBuilder
import io.limberapp.module.auth.AUTH_FEATURE

@Singleton
class JwtClaimsRequestClient @Inject constructor(
    @Named(AUTH_FEATURE) private val httpClient: HttpClient,
) {
  suspend operator fun invoke(
      endpoint: JwtClaimsRequestApi.Post,
      builder: RequestBuilder = {},
  ): Jwt =
      httpClient.request(endpoint, builder) { readValue(checkNotNull(it)) }
}
