package io.limberapp.backend.module.orgs.client.feature

import com.fasterxml.jackson.module.kotlin.readValue
import com.google.inject.Inject
import io.limberapp.backend.module.orgs.api.feature.FeatureApi
import io.limberapp.backend.module.orgs.rep.feature.FeatureRep
import io.limberapp.common.client.LimberHttpClient
import io.limberapp.common.client.LimberHttpClientRequestBuilder

class FeatureClient @Inject constructor(private val httpClient: LimberHttpClient) {
  suspend operator fun invoke(
      endpoint: FeatureApi.Post,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ): FeatureRep.Complete =
      httpClient.request(endpoint, builder) { readValue(checkNotNull(it)) }

  suspend operator fun invoke(
      endpoint: FeatureApi.Get,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ): FeatureRep.Complete? =
      httpClient.request(endpoint, builder) { it?.let { readValue(it) } }

  suspend operator fun invoke(
      endpoint: FeatureApi.Patch,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ): FeatureRep.Complete? =
      httpClient.request(endpoint, builder) { it?.let { readValue(it) } }

  suspend operator fun invoke(
      endpoint: FeatureApi.Delete,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ): Unit? =
      httpClient.request(endpoint, builder) { it?.let { readValue(it) } }
}
