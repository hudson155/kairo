package kairo.healthCheck

import kairo.rest.endpoint.RestEndpoint

public object HealthCheckApi {
  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/health/liveness")
  @RestEndpoint.Accept("*/*")
  public data object Liveness : RestEndpoint<Nothing, Unit>()

  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/health/readiness")
  @RestEndpoint.Accept("*/*")
  public data object Readiness : RestEndpoint<Nothing, HealthCheckRep>()
}
