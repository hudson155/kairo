package limber.client

import com.google.inject.Inject
import io.ktor.client.HttpClient
import limber.api.OrganizationHostnameApi
import limber.rep.OrganizationHostnameRep
import limber.rest.request

public class HttpOrganizationHostnameClient @Inject constructor(
  private val client: HttpClient,
) : OrganizationHostnameClient {
  override suspend operator fun invoke(
    endpoint: OrganizationHostnameApi.Create,
  ): OrganizationHostnameRep =
    client.request(endpoint)

  override suspend operator fun invoke(
    endpoint: OrganizationHostnameApi.Get,
  ): OrganizationHostnameRep? =
    client.request(endpoint)
}
