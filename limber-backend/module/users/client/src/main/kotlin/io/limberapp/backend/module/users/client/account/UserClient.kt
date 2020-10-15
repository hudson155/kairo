package io.limberapp.backend.module.users.client.account

import com.fasterxml.jackson.module.kotlin.readValue
import com.google.inject.Inject
import io.limberapp.backend.module.users.api.account.UserApi
import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.client.LimberHttpClient
import io.limberapp.client.LimberHttpClientRequestBuilder

class UserClient @Inject constructor(private val httpClient: LimberHttpClient) {
  suspend operator fun invoke(
      endpoint: UserApi.Post,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ) = httpClient.request(endpoint, builder) {
    readValue<UserRep.Complete>(checkNotNull(it))
  }

  suspend operator fun invoke(
      endpoint: UserApi.Get,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ) = httpClient.request(endpoint, builder) {
    it?.let { readValue<UserRep.Complete>(it) }
  }

  suspend operator fun invoke(
      endpoint: UserApi.GetByOrgGuidAndEmailAddress,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ) = httpClient.request(endpoint, builder) {
    it?.let { readValue<UserRep.Complete>(it) }
  }

  suspend operator fun invoke(
      endpoint: UserApi.GetByOrgGuid,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ) = httpClient.request(endpoint, builder) {
    readValue<Set<UserRep.Summary>>(checkNotNull(it))
  }

  suspend operator fun invoke(
      endpoint: UserApi.Patch,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ) = httpClient.request(endpoint, builder) {
    it?.let { readValue<UserRep.Complete>(it) }
  }

  suspend operator fun invoke(
      endpoint: UserApi.Delete,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ) = httpClient.request(endpoint, builder) {
    it?.let { readValue<Unit>(it) }
  }
}
