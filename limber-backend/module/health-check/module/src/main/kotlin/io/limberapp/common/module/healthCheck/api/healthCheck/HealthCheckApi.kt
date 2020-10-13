package io.limberapp.common.module.healthCheck.api.healthCheck

import io.ktor.http.HttpMethod
import io.limberapp.common.restInterface.LimberEndpoint

@Suppress("StringLiteralDuplication")
object HealthCheckApi {
  object Get : LimberEndpoint(
      httpMethod = HttpMethod.Get,
      path = "/health-check"
  )
}
