package io.limberapp.backend.module.auth.client.tenant

import io.limberapp.backend.module.auth.api.tenant.domain.TenantDomainApi
import io.limberapp.backend.module.auth.rep.tenant.TenantDomainRep
import io.limberapp.client.LimberHttpClient
import io.limberapp.client.LimberHttpClientRequestBuilder

class TenantDomainClient(private val httpClient: LimberHttpClient) {
  suspend operator fun invoke(
    endpoint: TenantDomainApi.Post,
    builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ) = httpClient.request(endpoint, builder) {
    parse<TenantDomainRep.Complete>(checkNotNull(it))
  }

  suspend operator fun invoke(
    endpoint: TenantDomainApi.Delete,
    builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ) = httpClient.request(endpoint, builder) {
    it?.let { parse<Unit>(it) }
  }
}
