package limber.client

import com.google.inject.Inject
import limber.api.FeatureApi
import limber.endpoint.CreateFeature
import limber.endpoint.DeleteFeature
import limber.endpoint.GetFeature
import limber.endpoint.GetFeaturesByOrganization
import limber.endpoint.UpdateFeature
import limber.rep.FeatureRep

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
