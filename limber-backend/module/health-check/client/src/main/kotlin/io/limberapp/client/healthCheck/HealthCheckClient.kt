package io.limberapp.client.healthCheck

import com.fasterxml.jackson.module.kotlin.readValue
import com.google.inject.Inject
import io.limberapp.api.healthCheck.HealthCheckApi
import io.limberapp.client.HttpClient
import io.limberapp.client.RequestBuilder
import io.limberapp.rep.healthCheck.HealthCheckRep

class HealthCheckClient @Inject constructor(private val httpClient: HttpClient) {
  suspend operator fun invoke(
      endpoint: HealthCheckApi.Get,
      builder: RequestBuilder = {},
  ): HealthCheckRep.Complete =
      httpClient.request(endpoint, builder) { readValue(checkNotNull(it)) }
}
