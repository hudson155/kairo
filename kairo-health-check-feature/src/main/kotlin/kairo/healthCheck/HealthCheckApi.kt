package kairo.healthCheck

import kairo.rest.RestEndpoint

public object HealthCheckApi {
  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/health/liveness")
  @RestEndpoint.Accept("application/json") // TODO: Use */*.
  public data object Liveness : RestEndpoint<Nothing, HealthCheckRep.Liveness>()
}
