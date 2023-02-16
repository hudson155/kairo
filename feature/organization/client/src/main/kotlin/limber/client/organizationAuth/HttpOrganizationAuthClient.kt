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
    endpoint: OrganizationAuthApi.Get,
  ): OrganizationAuthRep? =
    client.request(endpoint)

  override suspend operator fun invoke(
    endpoint: OrganizationAuthApi.GetByOrganization,
  ): OrganizationAuthRep? =
    client.request(endpoint)

  override suspend operator fun invoke(
    endpoint: OrganizationAuthApi.GetByHostname,
  ): OrganizationAuthRep? =
    client.request(endpoint)

  override suspend operator fun invoke(
    endpoint: OrganizationAuthApi.Create,
  ): OrganizationAuthRep =
    client.request(endpoint)

  override suspend operator fun invoke(
    endpoint: OrganizationAuthApi.Update,
  ): OrganizationAuthRep =
    client.request(endpoint)

  override suspend operator fun invoke(
    endpoint: OrganizationAuthApi.Delete,
  ): OrganizationAuthRep =
    client.request(endpoint)
}
