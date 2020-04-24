package io.limberapp.backend.api.healthCheck

import com.piperframework.restInterface.HttpMethod
import com.piperframework.restInterface.PiperEndpoint

@Suppress("StringLiteralDuplication")
object HealthCheckApi {
    object Get : PiperEndpoint(
        httpMethod = HttpMethod.GET,
        path = "/health-check"
    )
}
