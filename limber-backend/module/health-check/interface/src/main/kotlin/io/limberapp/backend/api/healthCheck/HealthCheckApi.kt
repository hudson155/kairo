package io.limberapp.backend.api.healthCheck

import io.ktor.http.HttpMethod
import io.limberapp.common.restInterface.Endpoint

object HealthCheckApi {
  object Get : Endpoint(HttpMethod.Get, "/health-check")
}
