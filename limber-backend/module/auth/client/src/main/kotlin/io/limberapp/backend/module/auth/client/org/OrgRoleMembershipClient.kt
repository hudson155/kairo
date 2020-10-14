package io.limberapp.backend.module.auth.client.org

import com.google.inject.Inject
import io.limberapp.backend.module.auth.api.org.OrgRoleMembershipApi
import io.limberapp.backend.module.auth.rep.org.OrgRoleMembershipRep
import io.limberapp.client.LimberHttpClient
import io.limberapp.client.LimberHttpClientRequestBuilder

class OrgRoleMembershipClient @Inject constructor(private val httpClient: LimberHttpClient) {
  suspend operator fun invoke(
      endpoint: OrgRoleMembershipApi.Post,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ) = httpClient.request(endpoint, builder) {
    parse<OrgRoleMembershipRep.Complete>(checkNotNull(it))
  }

  suspend operator fun invoke(
      endpoint: OrgRoleMembershipApi.GetByOrgRoleGuid,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ) = httpClient.request(endpoint, builder) {
    parse<Set<OrgRoleMembershipRep.Complete>>(checkNotNull(it))
  }

  suspend operator fun invoke(
      endpoint: OrgRoleMembershipApi.Delete,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ) = httpClient.request(endpoint, builder) {
    it?.let { parse<Unit>(it) }
  }
}
