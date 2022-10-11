package limber.client

import com.google.inject.Inject
import io.ktor.client.HttpClient
import limber.api.OrganizationAuthApi
import limber.rep.OrganizationAuthRep
import limber.rest.request

public class HttpOrganizationAuthClient @Inject constructor(
  private val client: HttpClient,
) : OrganizationAuthClient {
  override suspend operator fun invoke(
    endpoint: OrganizationAuthApi.Set,
  ): OrganizationAuthRep =
    client.request(endpoint)

  override suspend operator fun invoke(
    endpoint: OrganizationAuthApi.GetByOrganization,
  ): OrganizationAuthRep? =
    client.request(endpoint)
}
