package limber.client

import com.google.inject.Inject
import io.ktor.client.HttpClient
import limber.api.FeatureApi
import limber.rep.FeatureRep
import limber.rest.request

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
}
