package io.limberapp.client.feature

import com.fasterxml.jackson.module.kotlin.readValue
import com.google.inject.Inject
import io.limberapp.api.feature.FeatureApi
import io.limberapp.client.HttpClient
import io.limberapp.client.LimberHttpClientRequestBuilder
import io.limberapp.rep.feature.FeatureRep

class FeatureClient @Inject constructor(private val httpClient: HttpClient) {
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
