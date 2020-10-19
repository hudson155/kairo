package io.limberapp.backend.module.auth.client.org

import com.fasterxml.jackson.module.kotlin.readValue
import com.google.inject.Inject
import io.limberapp.backend.module.auth.api.org.OrgRoleApi
import io.limberapp.backend.module.auth.rep.org.OrgRoleRep
import io.limberapp.common.client.LimberHttpClient
import io.limberapp.common.client.LimberHttpClientRequestBuilder

class OrgRoleClient @Inject constructor(private val httpClient: LimberHttpClient) {
  suspend operator fun invoke(
      endpoint: OrgRoleApi.Post,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ) = httpClient.request(endpoint, builder) {
    readValue<OrgRoleRep.Complete>(checkNotNull(it))
  }

  suspend operator fun invoke(
      endpoint: OrgRoleApi.GetByOrgGuid,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ) = httpClient.request(endpoint, builder) {
    readValue<Set<OrgRoleRep.Complete>>(checkNotNull(it))
  }

  suspend operator fun invoke(
      endpoint: OrgRoleApi.Patch,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ) = httpClient.request(endpoint, builder) {
    it?.let { readValue<OrgRoleRep.Complete>(it) }
  }

  suspend operator fun invoke(
      endpoint: OrgRoleApi.Delete,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ) = httpClient.request(endpoint, builder) {
    it?.let { readValue<Unit>(it) }
  }
}
