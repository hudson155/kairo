package io.limberapp.client.healthCheck

import com.fasterxml.jackson.module.kotlin.readValue
import com.google.inject.Inject
import com.google.inject.Singleton
import com.google.inject.name.Named
import io.limberapp.api.healthCheck.HealthCheckApi
import io.limberapp.client.HttpClient
import io.limberapp.client.RequestBuilder
import io.limberapp.module.healthCheck.HEALTH_CHECK_FEATURE
import io.limberapp.rep.healthCheck.HealthCheckRep

@Singleton
class HealthCheckClient @Inject constructor(
    @Named(HEALTH_CHECK_FEATURE) private val httpClient: HttpClient,
) {
  suspend operator fun invoke(
      endpoint: HealthCheckApi.Get,
      builder: RequestBuilder = {},
  ): HealthCheckRep.Complete =
      httpClient.request(endpoint, builder) { readValue(checkNotNull(it)) }
}
