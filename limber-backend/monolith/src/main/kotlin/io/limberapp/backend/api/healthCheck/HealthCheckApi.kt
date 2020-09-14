package io.limberapp.backend.api.healthCheck

import io.limberapp.common.restInterface.HttpMethod
import io.limberapp.common.restInterface.LimberEndpoint

@Suppress("StringLiteralDuplication")
object HealthCheckApi {
  object Get : LimberEndpoint(
    httpMethod = HttpMethod.GET,
    path = "/health-check"
  )
}
