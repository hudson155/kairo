package limber.api.healthCheck

import io.ktor.http.HttpMethod
import limber.feature.rest.RestEndpoint

public object HealthCheckApi {
  public object GetLiveness : RestEndpoint<Nothing>() {
    override val method: HttpMethod = HttpMethod.Get
    override val path: String = "/health/liveness"
  }

  public object GetReadiness : RestEndpoint<Nothing>() {
    override val method: HttpMethod = HttpMethod.Get
    override val path: String = "/health/readiness"
  }
}
