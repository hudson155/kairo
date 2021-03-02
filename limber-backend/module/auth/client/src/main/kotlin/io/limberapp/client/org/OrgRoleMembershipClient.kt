package io.limberapp.client.org

import com.fasterxml.jackson.module.kotlin.readValue
import com.google.inject.Inject
import com.google.inject.Singleton
import com.google.inject.name.Named
import io.limberapp.api.org.OrgRoleMembershipApi
import io.limberapp.client.HttpClient
import io.limberapp.client.RequestBuilder
import io.limberapp.module.auth.AUTH_FEATURE
import io.limberapp.rep.org.OrgRoleMembershipRep

@Singleton
class OrgRoleMembershipClient @Inject constructor(
    @Named(AUTH_FEATURE) private val httpClient: HttpClient,
) {
  suspend operator fun invoke(
      endpoint: OrgRoleMembershipApi.Post,
      builder: RequestBuilder = {},
  ): OrgRoleMembershipRep.Complete =
      httpClient.request(endpoint, builder) { readValue(checkNotNull(it)) }

  suspend operator fun invoke(
      endpoint: OrgRoleMembershipApi.GetByOrgRoleGuid,
      builder: RequestBuilder = {},
  ): Set<OrgRoleMembershipRep.Complete> =
      httpClient.request(endpoint, builder) { readValue(checkNotNull(it)) }

  suspend operator fun invoke(
      endpoint: OrgRoleMembershipApi.Delete,
      builder: RequestBuilder = {},
  ): Unit? =
      httpClient.request(endpoint, builder) { it?.let { readValue(it) } }
}
