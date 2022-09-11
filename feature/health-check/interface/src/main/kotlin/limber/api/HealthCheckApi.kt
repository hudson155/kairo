package limber.api

import io.ktor.http.HttpMethod
import limber.rest.RestEndpoint

public object HealthCheckApi {
  public object GetLiveness : RestEndpoint() {
    override val method: HttpMethod = HttpMethod.Get
    override val path: String = "/health/liveness"
  }

  public object GetReadiness : RestEndpoint() {
    override val method: HttpMethod = HttpMethod.Get
    override val path: String = "/health/readiness"
  }
}
