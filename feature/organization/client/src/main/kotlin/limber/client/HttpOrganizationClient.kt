package limber.client

import com.google.inject.Inject
import io.ktor.client.HttpClient
import limber.api.OrganizationApi
import limber.rep.OrganizationRep
import limber.rest.request

public class HttpOrganizationClient @Inject constructor(
  private val client: HttpClient,
) : OrganizationClient {
  override suspend operator fun invoke(
    endpoint: OrganizationApi.Create,
  ): OrganizationRep =
    client.request(endpoint)

  override suspend operator fun invoke(
    endpoint: OrganizationApi.Get,
  ): OrganizationRep? =
    client.request(endpoint)

  override suspend operator fun invoke(
    endpoint: OrganizationApi.GetByHostname,
  ): OrganizationRep? =
    client.request(endpoint)

  override suspend operator fun invoke(
    endpoint: OrganizationApi.Update,
  ): OrganizationRep =
    client.request(endpoint)
}
