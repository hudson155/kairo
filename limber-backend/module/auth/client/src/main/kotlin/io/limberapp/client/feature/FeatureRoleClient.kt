package io.limberapp.client.feature

import com.fasterxml.jackson.module.kotlin.readValue
import com.google.inject.Inject
import com.google.inject.Singleton
import com.google.inject.name.Named
import io.limberapp.api.feature.FeatureRoleApi
import io.limberapp.client.HttpClient
import io.limberapp.client.RequestBuilder
import io.limberapp.module.auth.AUTH_FEATURE
import io.limberapp.rep.feature.FeatureRoleRep

@Singleton
class FeatureRoleClient @Inject constructor(
    @Named(AUTH_FEATURE) private val httpClient: HttpClient,
) {
  suspend operator fun invoke(
      endpoint: FeatureRoleApi.Post,
      builder: RequestBuilder = {},
  ): FeatureRoleRep.Complete =
      httpClient.request(endpoint, builder) { readValue(checkNotNull(it)) }

  suspend operator fun invoke(
      endpoint: FeatureRoleApi.GetByFeatureGuid,
      builder: RequestBuilder = {},
  ): Set<FeatureRoleRep.Complete> =
      httpClient.request(endpoint, builder) { readValue(checkNotNull(it)) }

  suspend operator fun invoke(
      endpoint: FeatureRoleApi.Patch,
      builder: RequestBuilder = {},
  ): FeatureRoleRep.Complete? =
      httpClient.request(endpoint, builder) { it?.let { readValue(it) } }

  suspend operator fun invoke(
      endpoint: FeatureRoleApi.Delete,
      builder: RequestBuilder = {},
  ): Unit? =
      httpClient.request(endpoint, builder) { it?.let { readValue(it) } }
}
