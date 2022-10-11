package limber.client

import com.google.inject.Inject
import limber.api.FeatureApi
import limber.endpoint.CreateFeature
import limber.endpoint.GetFeature
import limber.endpoint.GetFeaturesByOrganization
import limber.rep.FeatureRep

public class LocalFeatureClient @Inject constructor(
  private val createFeature: CreateFeature,
  private val getFeature: GetFeature,
  private val getFeaturesByOrganization: GetFeaturesByOrganization,
) : FeatureClient {
  override suspend operator fun invoke(
    endpoint: FeatureApi.Create,
  ): FeatureRep =
    createFeature.handle(endpoint)

  override suspend operator fun invoke(
    endpoint: FeatureApi.Get,
  ): FeatureRep? =
    getFeature.handle(endpoint)

  override suspend operator fun invoke(
    endpoint: FeatureApi.GetByOrganization,
  ): List<FeatureRep> =
    getFeaturesByOrganization.handle(endpoint)
}
