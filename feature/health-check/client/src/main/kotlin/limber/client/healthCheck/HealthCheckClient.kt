package limber.client.healthCheck

import limber.api.healthCheck.HealthCheckApi
import limber.rep.healthCheck.HealthCheckRep

public interface HealthCheckClient {
  public suspend operator fun invoke(
    endpoint: HealthCheckApi.GetLiveness,
  )

  public suspend operator fun invoke(
    endpoint: HealthCheckApi.GetReadiness,
  ): HealthCheckRep
}
