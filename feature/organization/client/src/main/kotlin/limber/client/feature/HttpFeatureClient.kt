package limber.client.feature

import com.google.inject.Inject
import io.ktor.client.HttpClient
import limber.api.feature.FeatureApi
import limber.feature.rest.request
import limber.rep.feature.FeatureRep

public class HttpFeatureClient @Inject constructor(
  private val client: HttpClient,
) : FeatureClient {
  override suspend operator fun invoke(
    endpoint: FeatureApi.Create,
  ): FeatureRep =
    client.request(endpoint)

  override suspend operator fun invoke(
    endpoint: FeatureApi.Get,
  ): FeatureRep? =
    client.request(endpoint)

  override suspend operator fun invoke(
    endpoint: FeatureApi.GetByOrganization,
  ): List<FeatureRep> =
    client.request(endpoint)

  override suspend operator fun invoke(
    endpoint: FeatureApi.Update,
  ): FeatureRep =
    client.request(endpoint)

  override suspend operator fun invoke(
    endpoint: FeatureApi.Delete,
  ): FeatureRep =
    client.request(endpoint)
}
