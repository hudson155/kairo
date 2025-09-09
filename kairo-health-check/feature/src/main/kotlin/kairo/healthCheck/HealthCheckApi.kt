package kairo.healthCheck

import kairo.rest.Rest
import kairo.rest.RestEndpoint

public object HealthCheckApi {
  @Rest("GET", "/health/liveness")
  public data object Liveness : RestEndpoint<Unit, Unit>()

  @Rest("GET", "/health/readiness")
  public data object Readiness : RestEndpoint<Unit, HealthCheckRep>()
}
