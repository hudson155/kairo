package io.limberapp.common.server

import com.fasterxml.jackson.module.kotlin.readValue
import com.google.inject.Inject
import io.limberapp.common.client.HttpClient
import io.limberapp.common.client.LimberHttpClientRequestBuilder

internal class TestClient @Inject constructor(private val httpClient: HttpClient) {
  suspend operator fun invoke(
      endpoint: TestApi.NoopGet,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ): Unit? =
      httpClient.request(endpoint, builder) { it?.let { readValue(it) } }

  suspend operator fun invoke(
      endpoint: TestApi.EndpointWithoutAuth,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ): Unit? =
      httpClient.request(endpoint, builder) { it?.let { readValue(it) } }

  suspend operator fun invoke(
      endpoint: TestApi.RequiresPermission,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ): Unit? =
      httpClient.request(endpoint, builder) { it?.let { readValue(it) } }

  suspend operator fun invoke(
      endpoint: TestApi.UnusualStatusCode,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ): Unit? =
      httpClient.request(endpoint, builder) { it?.let { readValue(it) } }

  suspend operator fun invoke(
      endpoint: TestApi.PathParam,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ): TestRep.Complete =
      httpClient.request(endpoint, builder) { readValue(checkNotNull(it)) }

  suspend operator fun invoke(
      endpoint: TestApi.RequiredQp,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ): TestRep.Complete =
      httpClient.request(endpoint, builder) { readValue(checkNotNull(it)) }

  suspend operator fun invoke(
      endpoint: TestApi.OptionalQp,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ): TestRep.Complete =
      httpClient.request(endpoint, builder) { readValue(checkNotNull(it)) }

  suspend operator fun invoke(
      endpoint: TestApi.RequiredBody,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ): TestRep.Complete =
      httpClient.request(endpoint, builder) { readValue(checkNotNull(it)) }

  suspend operator fun invoke(
      endpoint: TestApi.OptionalBody,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ): TestRep.Complete =
      httpClient.request(endpoint, builder) { readValue(checkNotNull(it)) }

  suspend operator fun invoke(
      endpoint: TestApi.Missing,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ): Unit? =
      httpClient.request(endpoint, builder) { it?.let { readValue(it) } }
}
