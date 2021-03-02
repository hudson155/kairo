package io.limberapp.client.org

import com.fasterxml.jackson.module.kotlin.readValue
import com.google.inject.Inject
import com.google.inject.Singleton
import com.google.inject.name.Named
import io.limberapp.api.org.OrgRoleApi
import io.limberapp.client.HttpClient
import io.limberapp.client.RequestBuilder
import io.limberapp.module.auth.AUTH_FEATURE
import io.limberapp.rep.org.OrgRoleRep

@Singleton
class OrgRoleClient @Inject constructor(
    @Named(AUTH_FEATURE) private val httpClient: HttpClient,
) {
  suspend operator fun invoke(
      endpoint: OrgRoleApi.Post,
      builder: RequestBuilder = {},
  ): OrgRoleRep.Complete =
      httpClient.request(endpoint, builder) { readValue(checkNotNull(it)) }

  suspend operator fun invoke(
      endpoint: OrgRoleApi.GetByOrgGuid,
      builder: RequestBuilder = {},
  ): Set<OrgRoleRep.Complete> =
      httpClient.request(endpoint, builder) { readValue(checkNotNull(it)) }

  suspend operator fun invoke(
      endpoint: OrgRoleApi.Patch,
      builder: RequestBuilder = {},
  ): OrgRoleRep.Complete? =
      httpClient.request(endpoint, builder) { it?.let { readValue(it) } }

  suspend operator fun invoke(
      endpoint: OrgRoleApi.Delete,
      builder: RequestBuilder = {},
  ): Unit? =
      httpClient.request(endpoint, builder) { it?.let { readValue(it) } }
}
