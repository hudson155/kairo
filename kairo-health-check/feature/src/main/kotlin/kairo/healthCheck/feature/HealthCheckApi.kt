package kairo.healthCheck.feature

import io.ktor.resources.Resource

// TODO: Add tests for this resource.
@Resource("/health")
public data object HealthCheckApi {
  @Resource("liveness")
  public data class Liveness(
    val parent: HealthCheckApi = HealthCheckApi,
  )

  @Resource("readiness")
  public data class Readiness(
    val parent: HealthCheckApi = HealthCheckApi,
  )
}
