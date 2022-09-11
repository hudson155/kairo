package limber.client

import limber.api.HealthCheckApi
import limber.rep.HealthCheckRep

public interface HealthCheckClient {
  public suspend operator fun invoke(
    endpoint: HealthCheckApi.GetLiveness,
  )

  public suspend operator fun invoke(
    endpoint: HealthCheckApi.GetReadiness,
  ): HealthCheckRep
}
