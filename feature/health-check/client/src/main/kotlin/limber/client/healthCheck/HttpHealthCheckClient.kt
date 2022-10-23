package limber.client.healthCheck

import com.google.inject.Inject
import io.ktor.client.HttpClient
import limber.api.healthCheck.HealthCheckApi
import limber.feature.rest.request
import limber.rep.healthCheck.HealthCheckRep

public class HttpHealthCheckClient @Inject constructor(
  private val client: HttpClient,
) : HealthCheckClient {
  override suspend operator fun invoke(
    endpoint: HealthCheckApi.GetLiveness,
  ): Unit =
    client.request(endpoint)

  override suspend operator fun invoke(
    endpoint: HealthCheckApi.GetReadiness,
  ): HealthCheckRep =
    client.request(endpoint)
}
