package limber.client.organizationHostname

import com.google.inject.Inject
import limber.api.organizationHostname.OrganizationHostnameApi
import limber.endpoint.organizationHostname.CreateOrganizationHostname
import limber.endpoint.organizationHostname.DeleteOrganizationHostname
import limber.endpoint.organizationHostname.GetOrganizationHostname
import limber.endpoint.organizationHostname.ListOrganizationHostnamesByOrganization
import limber.rep.organizationHostname.OrganizationHostnameRep

public class LocalOrganizationHostnameClient @Inject constructor(
  private val get: GetOrganizationHostname,
  private val listByOrganization: ListOrganizationHostnamesByOrganization,
  private val create: CreateOrganizationHostname,
  private val delete: DeleteOrganizationHostname,
) : OrganizationHostnameClient {
  override suspend operator fun invoke(
    endpoint: OrganizationHostnameApi.Get,
  ): OrganizationHostnameRep? =
    get.handle(endpoint)

  override suspend operator fun invoke(
    endpoint: OrganizationHostnameApi.ListByOrganization,
  ): List<OrganizationHostnameRep> =
    listByOrganization.handle(endpoint)

  override suspend operator fun invoke(
    endpoint: OrganizationHostnameApi.Create,
  ): OrganizationHostnameRep =
    create.handle(endpoint)

  override suspend operator fun invoke(
    endpoint: OrganizationHostnameApi.Delete,
  ): OrganizationHostnameRep =
    delete.handle(endpoint)
}
