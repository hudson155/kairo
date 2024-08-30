package kairo.healthCheck

import kairo.rest.RestEndpoint

public object HealthCheckApi {
  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/health/liveness")
  @RestEndpoint.Accept("*/*")
  public data object Liveness : RestEndpoint<Nothing, HealthCheckRep.Liveness>()
}
