package io.limberapp.client.tenant

import com.fasterxml.jackson.module.kotlin.readValue
import com.google.inject.Inject
import com.google.inject.Singleton
import com.google.inject.name.Named
import io.limberapp.api.tenant.TenantApi
import io.limberapp.client.HttpClient
import io.limberapp.client.RequestBuilder
import io.limberapp.module.auth.AUTH_FEATURE
import io.limberapp.rep.tenant.TenantRep

@Singleton
class TenantClient @Inject constructor(
    @Named(AUTH_FEATURE) private val httpClient: HttpClient,
) {
  suspend operator fun invoke(
      endpoint: TenantApi.Post,
      builder: RequestBuilder = {},
  ): TenantRep.Complete =
      httpClient.request(endpoint, builder) { readValue(checkNotNull(it)) }

  suspend operator fun invoke(
      endpoint: TenantApi.Get,
      builder: RequestBuilder = {},
  ): TenantRep.Complete? =
      httpClient.request(endpoint, builder) { it?.let { readValue(it) } }

  suspend operator fun invoke(
      endpoint: TenantApi.GetByDomain,
      builder: RequestBuilder = {},
  ): TenantRep.Complete? =
      httpClient.request(endpoint, builder) { it?.let { readValue(it) } }

  suspend operator fun invoke(
      endpoint: TenantApi.Patch,
      builder: RequestBuilder = {},
  ): TenantRep.Complete? =
      httpClient.request(endpoint, builder) { it?.let { readValue(it) } }

  suspend operator fun invoke(
      endpoint: TenantApi.Delete,
      builder: RequestBuilder = {},
  ): Unit? =
      httpClient.request(endpoint, builder) { it?.let { readValue(it) } }
}
