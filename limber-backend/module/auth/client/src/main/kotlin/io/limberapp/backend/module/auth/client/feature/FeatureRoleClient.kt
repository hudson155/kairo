package io.limberapp.backend.module.auth.client.feature

import io.limberapp.backend.module.auth.api.feature.role.FeatureRoleApi
import io.limberapp.backend.module.auth.rep.feature.FeatureRoleRep
import io.limberapp.client.LimberHttpClient
import io.limberapp.client.LimberHttpClientRequestBuilder

class FeatureRoleClient(private val httpClient: LimberHttpClient) {
  suspend operator fun invoke(
    endpoint: FeatureRoleApi.Post,
    builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ) = httpClient.request(endpoint, builder) {
    parse<FeatureRoleRep.Complete>(checkNotNull(it))
  }

  suspend operator fun invoke(
    endpoint: FeatureRoleApi.GetByFeatureGuid,
    builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ) = httpClient.request(endpoint, builder) {
    parse<Set<FeatureRoleRep.Complete>>(checkNotNull(it))
  }

  suspend operator fun invoke(
    endpoint: FeatureRoleApi.Patch,
    builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ) = httpClient.request(endpoint, builder) {
    it?.let { parse<FeatureRoleRep.Complete>(it) }
  }

  suspend operator fun invoke(
    endpoint: FeatureRoleApi.Delete,
    builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ) = httpClient.request(endpoint, builder) {
    it?.let { parse<Unit>(it) }
  }
}
