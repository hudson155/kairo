package io.limberapp.api.healthCheck

import io.ktor.http.HttpMethod
import io.limberapp.restInterface.Endpoint

object HealthCheckApi {
  object Get : Endpoint(HttpMethod.Get, "/health-check")
}
