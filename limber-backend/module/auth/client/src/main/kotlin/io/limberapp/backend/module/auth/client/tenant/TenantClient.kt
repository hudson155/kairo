package io.limberapp.backend.module.auth.client.tenant

import com.fasterxml.jackson.module.kotlin.readValue
import com.google.inject.Inject
import io.limberapp.backend.module.auth.api.tenant.TenantApi
import io.limberapp.backend.module.auth.rep.tenant.TenantRep
import io.limberapp.common.client.LimberHttpClient
import io.limberapp.common.client.LimberHttpClientRequestBuilder

class TenantClient @Inject constructor(private val httpClient: LimberHttpClient) {
  suspend operator fun invoke(
      endpoint: TenantApi.Post,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ): TenantRep.Complete =
      httpClient.request(endpoint, builder) { readValue(checkNotNull(it)) }

  suspend operator fun invoke(
      endpoint: TenantApi.Get,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ): TenantRep.Complete? =
      httpClient.request(endpoint, builder) { it?.let { readValue(it) } }

  suspend operator fun invoke(
      endpoint: TenantApi.GetByDomain,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ): TenantRep.Complete? =
      httpClient.request(endpoint, builder) { it?.let { readValue(it) } }

  suspend operator fun invoke(
      endpoint: TenantApi.Patch,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ): TenantRep.Complete? =
      httpClient.request(endpoint, builder) { it?.let { readValue(it) } }

  suspend operator fun invoke(
      endpoint: TenantApi.Delete,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ): Unit? =
      httpClient.request(endpoint, builder) { it?.let { readValue(it) } }
}
