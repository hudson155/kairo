package io.limberapp.backend.client.healthCheck

import com.fasterxml.jackson.module.kotlin.readValue
import com.google.inject.Inject
import io.limberapp.backend.api.healthCheck.HealthCheckApi
import io.limberapp.backend.rep.healthCheck.HealthCheckRep
import io.limberapp.common.client.HttpClient
import io.limberapp.common.client.RequestBuilder

class HealthCheckClient @Inject constructor(private val httpClient: HttpClient) {
  suspend operator fun invoke(
      endpoint: HealthCheckApi.Get,
      builder: RequestBuilder = {},
  ): HealthCheckRep.Complete =
      httpClient.request(endpoint, builder) { readValue(checkNotNull(it)) }
}
