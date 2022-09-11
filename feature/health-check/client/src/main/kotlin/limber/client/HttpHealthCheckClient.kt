package limber.client

import com.google.inject.Inject
import io.ktor.client.HttpClient
import limber.api.HealthCheckApi
import limber.rep.HealthCheckRep
import limber.rest.request

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
