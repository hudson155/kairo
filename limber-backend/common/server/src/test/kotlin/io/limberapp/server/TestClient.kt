package io.limberapp.server

import com.fasterxml.jackson.module.kotlin.readValue
import com.google.inject.Inject
import io.limberapp.client.HttpClient
import io.limberapp.client.RequestBuilder

internal class TestClient @Inject constructor(private val httpClient: HttpClient) {
  suspend operator fun invoke(
      endpoint: TestApi.NoopGet,
      builder: RequestBuilder = {},
  ): Unit? =
      httpClient.request(endpoint, builder) { it?.let { readValue(it) } }

  suspend operator fun invoke(
      endpoint: TestApi.EndpointWithoutAuth,
      builder: RequestBuilder = {},
  ): Unit? =
      httpClient.request(endpoint, builder) { it?.let { readValue(it) } }

  suspend operator fun invoke(
      endpoint: TestApi.RequiresPermission,
      builder: RequestBuilder = {},
  ): Unit? =
      httpClient.request(endpoint, builder) { it?.let { readValue(it) } }

  suspend operator fun invoke(
      endpoint: TestApi.PathParam,
      builder: RequestBuilder = {},
  ): TestRep.Complete =
      httpClient.request(endpoint, builder) { readValue(checkNotNull(it)) }

  suspend operator fun invoke(
      endpoint: TestApi.RequiredQp,
      builder: RequestBuilder = {},
  ): TestRep.Complete =
      httpClient.request(endpoint, builder) { readValue(checkNotNull(it)) }

  suspend operator fun invoke(
      endpoint: TestApi.OptionalQp,
      builder: RequestBuilder = {},
  ): TestRep.Complete =
      httpClient.request(endpoint, builder) { readValue(checkNotNull(it)) }

  suspend operator fun invoke(
      endpoint: TestApi.RequiredBody,
      builder: RequestBuilder = {},
  ): TestRep.Complete =
      httpClient.request(endpoint, builder) { readValue(checkNotNull(it)) }

  suspend operator fun invoke(
      endpoint: TestApi.OptionalBody,
      builder: RequestBuilder = {},
  ): TestRep.Complete =
      httpClient.request(endpoint, builder) { readValue(checkNotNull(it)) }

  suspend operator fun invoke(
      endpoint: TestApi.Missing,
      builder: RequestBuilder = {},
  ): Unit? =
      httpClient.request(endpoint, builder) { it?.let { readValue(it) } }
}
