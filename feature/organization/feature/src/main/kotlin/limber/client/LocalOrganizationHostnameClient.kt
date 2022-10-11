package limber.client

import com.google.inject.Inject
import limber.api.OrganizationHostnameApi
import limber.endpoint.CreateOrganizationHostname
import limber.endpoint.DeleteOrganizationHostname
import limber.endpoint.GetOrganizationHostname
import limber.rep.OrganizationHostnameRep

public class LocalOrganizationHostnameClient @Inject constructor(
  private val create: CreateOrganizationHostname,
  private val get: GetOrganizationHostname,
  private val delete: DeleteOrganizationHostname,
) : OrganizationHostnameClient {
  override suspend operator fun invoke(
    endpoint: OrganizationHostnameApi.Create,
  ): OrganizationHostnameRep =
    create.handle(endpoint)

  override suspend operator fun invoke(
    endpoint: OrganizationHostnameApi.Get,
  ): OrganizationHostnameRep? =
    get.handle(endpoint)

  override suspend operator fun invoke(
    endpoint: OrganizationHostnameApi.Delete,
  ): OrganizationHostnameRep =
    delete.handle(endpoint)
}
