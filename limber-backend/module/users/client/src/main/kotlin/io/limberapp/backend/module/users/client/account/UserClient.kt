package io.limberapp.backend.module.users.client.account

import com.fasterxml.jackson.module.kotlin.readValue
import com.google.inject.Inject
import io.limberapp.backend.module.users.api.account.UserApi
import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.common.client.LimberHttpClient
import io.limberapp.common.client.LimberHttpClientRequestBuilder

class UserClient @Inject constructor(private val httpClient: LimberHttpClient) {
  suspend operator fun invoke(
      endpoint: UserApi.Post,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ): UserRep.Complete =
      httpClient.request(endpoint, builder) { readValue(checkNotNull(it)) }

  suspend operator fun invoke(
      endpoint: UserApi.Get,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ): UserRep.Complete? =
      httpClient.request(endpoint, builder) { it?.let { readValue(it) } }

  suspend operator fun invoke(
      endpoint: UserApi.GetByOrgGuidAndEmailAddress,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ): UserRep.Complete? =
      httpClient.request(endpoint, builder) { it?.let { readValue(it) } }

  suspend operator fun invoke(
      endpoint: UserApi.GetByOrgGuid,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ): Set<UserRep.Summary> =
      httpClient.request(endpoint, builder) { readValue(checkNotNull(it)) }

  suspend operator fun invoke(
      endpoint: UserApi.Patch,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ): UserRep.Complete? =
      httpClient.request(endpoint, builder) { it?.let { readValue(it) } }

  suspend operator fun invoke(
      endpoint: UserApi.Delete,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ): Unit? =
      httpClient.request(endpoint, builder) { it?.let { readValue(it) } }
}
