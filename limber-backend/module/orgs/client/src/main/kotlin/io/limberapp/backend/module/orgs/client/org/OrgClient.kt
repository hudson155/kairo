package io.limberapp.backend.module.orgs.client.org

import com.fasterxml.jackson.module.kotlin.readValue
import com.google.inject.Inject
import io.limberapp.backend.module.orgs.api.org.OrgApi
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.common.client.LimberHttpClient
import io.limberapp.common.client.LimberHttpClientRequestBuilder

class OrgClient @Inject constructor(private val httpClient: LimberHttpClient) {
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
