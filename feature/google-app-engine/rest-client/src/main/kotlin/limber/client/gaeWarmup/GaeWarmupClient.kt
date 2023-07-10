package limber.client.gaeWarmup

import limber.api.gaeWarmup.GaeWarmupApi
import limber.rep.healthCheck.HealthCheckRep

public interface GaeWarmupClient {
  public suspend operator fun invoke(
    endpoint: GaeWarmupApi.Get,
  ): HealthCheckRep
}
