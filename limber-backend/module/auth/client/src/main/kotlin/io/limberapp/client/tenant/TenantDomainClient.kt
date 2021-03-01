package io.limberapp.client.tenant

import com.fasterxml.jackson.module.kotlin.readValue
import com.google.inject.Inject
import com.google.inject.Singleton
import com.google.inject.name.Named
import io.limberapp.api.tenant.TenantDomainApi
import io.limberapp.client.HttpClient
import io.limberapp.client.RequestBuilder
import io.limberapp.module.auth.AUTH_FEATURE
import io.limberapp.rep.tenant.TenantDomainRep

@Singleton
class TenantDomainClient @Inject constructor(
    @Named(AUTH_FEATURE) private val httpClient: HttpClient,
) {
  suspend operator fun invoke(
      endpoint: TenantDomainApi.Post,
      builder: RequestBuilder = {},
  ): TenantDomainRep.Complete =
      httpClient.request(endpoint, builder) { readValue(checkNotNull(it)) }

  suspend operator fun invoke(
      endpoint: TenantDomainApi.Delete,
      builder: RequestBuilder = {},
  ): Unit? =
      httpClient.request(endpoint, builder) { it?.let { readValue(it) } }
}
