package kairo.healthCheck.feature

import kairo.rest.RestEndpoint

// TODO: Add tests for this resource.
public object HealthCheckApi {
  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/health/liveness")
  public data object Liveness : RestEndpoint<Unit, Unit>()

  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/health/readiness")
  public data object Readiness : RestEndpoint<Unit, HealthCheckRep>()
}
