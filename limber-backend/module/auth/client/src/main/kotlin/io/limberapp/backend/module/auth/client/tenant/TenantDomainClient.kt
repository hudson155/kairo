package io.limberapp.backend.module.auth.client.tenant

import com.fasterxml.jackson.module.kotlin.readValue
import com.google.inject.Inject
import io.limberapp.backend.module.auth.api.tenant.TenantDomainApi
import io.limberapp.backend.module.auth.rep.tenant.TenantDomainRep
import io.limberapp.common.client.LimberHttpClient
import io.limberapp.common.client.LimberHttpClientRequestBuilder

class TenantDomainClient @Inject constructor(private val httpClient: LimberHttpClient) {
  suspend operator fun invoke(
      endpoint: TenantDomainApi.Post,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ): TenantDomainRep.Complete =
      httpClient.request(endpoint, builder) { readValue(checkNotNull(it)) }

  suspend operator fun invoke(
      endpoint: TenantDomainApi.Delete,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ): Unit? =
      httpClient.request(endpoint, builder) { it?.let { readValue(it) } }
}
