package io.limberapp.backend.module.users.client.account

import io.limberapp.backend.module.users.api.user.role.UserRoleApi
import io.limberapp.client.LimberHttpClient
import io.limberapp.client.LimberHttpClientRequestBuilder

class UserRoleClient(private val httpClient: LimberHttpClient) {
  suspend operator fun invoke(
    endpoint: UserRoleApi.Put,
    builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ) = httpClient.request(endpoint, builder) {
    it?.let { parse<Unit>(it) }
  }

  suspend operator fun invoke(
    endpoint: UserRoleApi.Delete,
    builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ) = httpClient.request(endpoint, builder) {
    it?.let { parse<Unit>(it) }
  }
}
