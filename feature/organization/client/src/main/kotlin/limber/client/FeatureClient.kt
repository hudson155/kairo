package limber.client

import limber.api.FeatureApi
import limber.rep.FeatureRep

public interface FeatureClient {
  public suspend operator fun invoke(
    endpoint: FeatureApi.Create,
  ): FeatureRep

  public suspend operator fun invoke(
    endpoint: FeatureApi.Get,
  ): FeatureRep?

  public suspend operator fun invoke(
    endpoint: FeatureApi.GetByOrganization,
  ): List<FeatureRep>
}
