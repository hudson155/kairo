package io.limberapp.backend.module.orgs.client.org

import io.limberapp.backend.module.orgs.api.org.OrgApi
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.client.LimberHttpClient
import io.limberapp.client.LimberHttpClientRequestBuilder

class OrgClient(private val httpClient: LimberHttpClient) {
  suspend operator fun invoke(
    endpoint: OrgApi.Post,
    builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ) = httpClient.request(endpoint, builder) {
    parse<OrgRep.Complete>(checkNotNull(it))
  }

  suspend operator fun invoke(
    endpoint: OrgApi.Get,
    builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ) = httpClient.request(endpoint, builder) {
    it?.let { parse<OrgRep.Complete>(it) }
  }

  suspend operator fun invoke(
    endpoint: OrgApi.GetByOwnerUserGuid,
    builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ) = httpClient.request(endpoint, builder) {
    it?.let { parse<OrgRep.Complete>(it) }
  }

  suspend operator fun invoke(
    endpoint: OrgApi.Patch,
    builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ) = httpClient.request(endpoint, builder) {
    it?.let { parse<OrgRep.Complete>(it) }
  }

  suspend operator fun invoke(
    endpoint: OrgApi.Delete,
    builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ) = httpClient.request(endpoint, builder) {
    it?.let { parse<Unit>(it) }
  }
}
