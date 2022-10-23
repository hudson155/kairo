package limber.client.feature

import com.google.inject.Inject
import limber.api.feature.FeatureApi
import limber.endpoint.feature.CreateFeature
import limber.endpoint.feature.DeleteFeature
import limber.endpoint.feature.GetFeature
import limber.endpoint.feature.GetFeaturesByOrganization
import limber.endpoint.feature.UpdateFeature
import limber.rep.feature.FeatureRep

public class LocalFeatureClient @Inject constructor(
  private val create: CreateFeature,
  private val get: GetFeature,
  private val getByOrganization: GetFeaturesByOrganization,
  private val update: UpdateFeature,
  private val delete: DeleteFeature,
) : FeatureClient {
  override suspend operator fun invoke(
    endpoint: FeatureApi.Create,
  ): FeatureRep =
    create.handle(endpoint)

  override suspend operator fun invoke(
    endpoint: FeatureApi.Get,
  ): FeatureRep? =
    get.handle(endpoint)

  override suspend operator fun invoke(
    endpoint: FeatureApi.GetByOrganization,
  ): List<FeatureRep> =
    getByOrganization.handle(endpoint)

  override suspend operator fun invoke(
    endpoint: FeatureApi.Update,
  ): FeatureRep =
    update.handle(endpoint)

  override suspend operator fun invoke(
    endpoint: FeatureApi.Delete,
  ): FeatureRep =
    delete.handle(endpoint)
}
