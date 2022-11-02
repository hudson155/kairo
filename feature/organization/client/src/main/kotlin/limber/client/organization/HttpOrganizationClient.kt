package limber.client.organization

import com.google.inject.Inject
import io.ktor.client.HttpClient
import limber.api.organization.OrganizationApi
import limber.feature.rest.request
import limber.rep.organization.OrganizationRep

public class HttpOrganizationClient @Inject constructor(
  private val client: HttpClient,
) : OrganizationClient {
  override suspend operator fun invoke(
    endpoint: OrganizationApi.Get,
  ): OrganizationRep? =
    client.request(endpoint)

  override suspend operator fun invoke(
    endpoint: OrganizationApi.Create,
  ): OrganizationRep =
    client.request(endpoint)

  override suspend operator fun invoke(
    endpoint: OrganizationApi.Update,
  ): OrganizationRep =
    client.request(endpoint)
}
