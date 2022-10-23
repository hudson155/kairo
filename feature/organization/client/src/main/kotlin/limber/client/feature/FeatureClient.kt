package limber.client.feature

import limber.api.feature.FeatureApi
import limber.rep.feature.FeatureRep

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

  public suspend operator fun invoke(
    endpoint: FeatureApi.Update,
  ): FeatureRep

  public suspend operator fun invoke(
    endpoint: FeatureApi.Delete,
  ): FeatureRep
}
