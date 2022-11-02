package limber.client.organizationAuth

import com.google.inject.Inject
import io.ktor.client.HttpClient
import limber.api.organizationAuth.OrganizationAuthApi
import limber.feature.rest.request
import limber.rep.organizationAuth.OrganizationAuthRep

public class HttpOrganizationAuthClient @Inject constructor(
  private val client: HttpClient,
) : OrganizationAuthClient {
  override suspend operator fun invoke(
    endpoint: OrganizationAuthApi.GetByOrganization,
  ): OrganizationAuthRep? =
    client.request(endpoint)

  override suspend operator fun invoke(
    endpoint: OrganizationAuthApi.GetByHostname,
  ): OrganizationAuthRep? =
    client.request(endpoint)

  override suspend operator fun invoke(
    endpoint: OrganizationAuthApi.Set,
  ): OrganizationAuthRep =
    client.request(endpoint)

  override suspend operator fun invoke(
    endpoint: OrganizationAuthApi.DeleteByOrganization,
  ): OrganizationAuthRep =
    client.request(endpoint)
}
