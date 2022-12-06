package limber.client.gaeWarmup

import com.google.inject.Inject
import io.ktor.client.HttpClient
import limber.api.gaeWarmup.GaeWarmupApi
import limber.feature.rest.request
import limber.rep.healthCheck.HealthCheckRep

public class HttpGaeWarmupClient @Inject constructor(
  private val client: HttpClient,
) : GaeWarmupClient {
  override suspend operator fun invoke(
    endpoint: GaeWarmupApi.Get,
  ): HealthCheckRep =
    client.request(endpoint)
}
