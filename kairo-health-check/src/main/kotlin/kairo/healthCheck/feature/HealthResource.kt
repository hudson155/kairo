package kairo.healthCheck.feature

import io.ktor.resources.Resource

@Resource("/health")
internal data object HealthResource {
  @Resource("liveness")
  internal data class Liveness(val parent: HealthResource = HealthResource)

  @Resource("readiness")
  internal data class Readiness(val parent: HealthResource = HealthResource)
}
