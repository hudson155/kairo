package kairo.googleAppEngine

import kairo.healthCheck.HealthCheckRep
import kairo.rest.RestEndpoint

public object GoogleAppEngineApi {
  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/_ah/warmup", validate = false)
  @RestEndpoint.Accept("*/*")
  public data object Warmup : RestEndpoint<Nothing, HealthCheckRep>()
}
