package io.limberapp.backend.module.auth.client.tenant

import com.google.inject.Inject
import io.limberapp.backend.module.auth.api.tenant.TenantApi
import io.limberapp.backend.module.auth.rep.tenant.TenantRep
import io.limberapp.client.LimberHttpClient
import io.limberapp.client.LimberHttpClientRequestBuilder

class TenantClient @Inject constructor(private val httpClient: LimberHttpClient) {
  suspend operator fun invoke(
      endpoint: TenantApi.Post,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ) = httpClient.request(endpoint, builder) {
    parse<TenantRep.Complete>(checkNotNull(it))
  }

  suspend operator fun invoke(
      endpoint: TenantApi.Get,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ) = httpClient.request(endpoint, builder) {
    it?.let { parse<TenantRep.Complete>(it) }
  }

  suspend operator fun invoke(
      endpoint: TenantApi.GetByDomain,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ) = httpClient.request(endpoint, builder) {
    it?.let { parse<TenantRep.Complete>(it) }
  }

  suspend operator fun invoke(
      endpoint: TenantApi.Patch,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ) = httpClient.request(endpoint, builder) {
    it?.let { parse<TenantRep.Complete>(it) }
  }

  suspend operator fun invoke(
      endpoint: TenantApi.Delete,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ) = httpClient.request(endpoint, builder) {
    it?.let { parse<Unit>(it) }
  }
}
