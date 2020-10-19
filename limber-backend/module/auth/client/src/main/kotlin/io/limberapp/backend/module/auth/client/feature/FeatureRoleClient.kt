package io.limberapp.backend.module.auth.client.feature

import com.fasterxml.jackson.module.kotlin.readValue
import com.google.inject.Inject
import io.limberapp.backend.module.auth.api.feature.FeatureRoleApi
import io.limberapp.backend.module.auth.rep.feature.FeatureRoleRep
import io.limberapp.common.client.LimberHttpClient
import io.limberapp.common.client.LimberHttpClientRequestBuilder

class FeatureRoleClient @Inject constructor(private val httpClient: LimberHttpClient) {
  suspend operator fun invoke(
      endpoint: FeatureRoleApi.Post,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ): FeatureRoleRep.Complete =
      httpClient.request(endpoint, builder) { readValue(checkNotNull(it)) }

  suspend operator fun invoke(
      endpoint: FeatureRoleApi.GetByFeatureGuid,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ): Set<FeatureRoleRep.Complete> =
      httpClient.request(endpoint, builder) { readValue(checkNotNull(it)) }

  suspend operator fun invoke(
      endpoint: FeatureRoleApi.Patch,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ): FeatureRoleRep.Complete? =
      httpClient.request(endpoint, builder) { it?.let { readValue(it) } }

  suspend operator fun invoke(
      endpoint: FeatureRoleApi.Delete,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ): Unit? =
      httpClient.request(endpoint, builder) { it?.let { readValue(it) } }
}
