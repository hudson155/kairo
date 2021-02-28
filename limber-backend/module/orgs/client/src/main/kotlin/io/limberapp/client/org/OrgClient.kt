package io.limberapp.client.org

import com.fasterxml.jackson.module.kotlin.readValue
import com.google.inject.Inject
import com.google.inject.Singleton
import com.google.inject.name.Named
import io.limberapp.api.org.OrgApi
import io.limberapp.client.HttpClient
import io.limberapp.client.LimberHttpClientRequestBuilder
import io.limberapp.module.orgs.ORGS_FEATURE
import io.limberapp.rep.org.OrgRep

@Singleton
class OrgClient @Inject constructor(
    @Named(ORGS_FEATURE) private val httpClient: HttpClient,
) {
  suspend operator fun invoke(
      endpoint: OrgApi.Post,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ): OrgRep.Complete =
      httpClient.request(endpoint, builder) { readValue(checkNotNull(it)) }

  suspend operator fun invoke(
      endpoint: OrgApi.Get,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ): OrgRep.Complete? =
      httpClient.request(endpoint, builder) { it?.let { readValue(it) } }

  suspend operator fun invoke(
      endpoint: OrgApi.GetByOwnerUserGuid,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ): OrgRep.Complete? =
      httpClient.request(endpoint, builder) { it?.let { readValue(it) } }

  suspend operator fun invoke(
      endpoint: OrgApi.Patch,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ): OrgRep.Complete? =
      httpClient.request(endpoint, builder) { it?.let { readValue(it) } }

  suspend operator fun invoke(
      endpoint: OrgApi.Delete,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ): Unit? =
      httpClient.request(endpoint, builder) { it?.let { readValue(it) } }
}
