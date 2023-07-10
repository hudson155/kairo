package limber.client.organizationHostname

import com.google.inject.Inject
import io.ktor.client.HttpClient
import limber.api.organizationHostname.OrganizationHostnameApi
import limber.feature.rest.request
import limber.rep.organizationHostname.OrganizationHostnameRep

public class HttpOrganizationHostnameClient @Inject constructor(
  private val client: HttpClient,
) : OrganizationHostnameClient {
  override suspend operator fun invoke(
    endpoint: OrganizationHostnameApi.Get,
  ): OrganizationHostnameRep? =
    client.request(endpoint)

  override suspend operator fun invoke(
    endpoint: OrganizationHostnameApi.Create,
  ): OrganizationHostnameRep =
    client.request(endpoint)

  override suspend operator fun invoke(
    endpoint: OrganizationHostnameApi.Delete,
  ): OrganizationHostnameRep =
    client.request(endpoint)
}
