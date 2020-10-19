package io.limberapp.backend.module.auth.client.org

import com.fasterxml.jackson.module.kotlin.readValue
import com.google.inject.Inject
import io.limberapp.backend.module.auth.api.org.OrgRoleMembershipApi
import io.limberapp.backend.module.auth.rep.org.OrgRoleMembershipRep
import io.limberapp.common.client.LimberHttpClient
import io.limberapp.common.client.LimberHttpClientRequestBuilder

class OrgRoleMembershipClient @Inject constructor(private val httpClient: LimberHttpClient) {
  suspend operator fun invoke(
      endpoint: OrgRoleMembershipApi.Post,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ): OrgRoleMembershipRep.Complete =
      httpClient.request(endpoint, builder) { readValue(checkNotNull(it)) }

  suspend operator fun invoke(
      endpoint: OrgRoleMembershipApi.GetByOrgRoleGuid,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ): Set<OrgRoleMembershipRep.Complete> =
      httpClient.request(endpoint, builder) { readValue(checkNotNull(it)) }

  suspend operator fun invoke(
      endpoint: OrgRoleMembershipApi.Delete,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ): Unit? =
      httpClient.request(endpoint, builder) { it?.let { readValue(it) } }
}
