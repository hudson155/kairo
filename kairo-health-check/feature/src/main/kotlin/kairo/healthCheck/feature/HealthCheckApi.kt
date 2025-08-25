package kairo.healthCheck.feature

import kairo.rest.RestEndpoint
import kotlinx.serialization.Serializable

// TODO: Add tests for this resource.
public object HealthCheckApi {
  @Serializable
  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/health/liveness")
  public data object Liveness : RestEndpoint<Unit, Unit>()

  @Serializable
  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/health/readiness")
  public data object Readiness : RestEndpoint<Unit, HealthCheckRep>()
}
