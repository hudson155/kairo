package io.limberapp.client.user

import com.fasterxml.jackson.module.kotlin.readValue
import com.google.inject.Inject
import io.limberapp.client.HttpClient
import io.limberapp.client.LimberHttpClientRequestBuilder
import io.limberapp.module.users.api.user.UserApi
import io.limberapp.module.users.rep.user.UserRep

class UserClient @Inject constructor(private val httpClient: HttpClient) {
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
