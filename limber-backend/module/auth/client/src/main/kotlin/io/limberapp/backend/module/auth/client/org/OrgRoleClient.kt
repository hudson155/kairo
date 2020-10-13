package io.limberapp.backend.module.auth.client.org

import io.limberapp.backend.module.auth.api.org.role.OrgRoleApi
import io.limberapp.backend.module.auth.rep.org.OrgRoleRep
import io.limberapp.client.LimberHttpClient
import io.limberapp.client.LimberHttpClientRequestBuilder

class OrgRoleClient(private val httpClient: LimberHttpClient) {
  suspend operator fun invoke(
      endpoint: OrgRoleApi.Post,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ) = httpClient.request(endpoint, builder) {
    parse<OrgRoleRep.Complete>(checkNotNull(it))
  }

  suspend operator fun invoke(
      endpoint: OrgRoleApi.GetByOrgGuid,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ) = httpClient.request(endpoint, builder) {
    parse<Set<OrgRoleRep.Complete>>(checkNotNull(it))
  }

  suspend operator fun invoke(
      endpoint: OrgRoleApi.Patch,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ) = httpClient.request(endpoint, builder) {
    it?.let { parse<OrgRoleRep.Complete>(it) }
  }

  suspend operator fun invoke(
      endpoint: OrgRoleApi.Delete,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ) = httpClient.request(endpoint, builder) {
    it?.let { parse<Unit>(it) }
  }
}
