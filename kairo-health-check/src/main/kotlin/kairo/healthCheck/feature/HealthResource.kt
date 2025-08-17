package kairo.healthCheck.feature

import io.ktor.resources.Resource

// TODO: Add tests for this resource.
@Resource("/health")
public data object HealthResource {
  @Resource("liveness")
  public data class Liveness(val parent: HealthResource = HealthResource)

  @Resource("readiness")
  public data class Readiness(val parent: HealthResource = HealthResource)
}
