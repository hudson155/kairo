package limber.client

import com.google.inject.Inject
import limber.api.OrganizationHostnameApi
import limber.endpoint.CreateOrganizationHostname
import limber.endpoint.DeleteOrganizationHostname
import limber.endpoint.GetOrganizationHostname
import limber.rep.OrganizationHostnameRep

public class LocalOrganizationHostnameClient @Inject constructor(
  private val createOrganizationHostname: CreateOrganizationHostname,
  private val getOrganizationHostname: GetOrganizationHostname,
  private val deleteOrganizationHostname: DeleteOrganizationHostname,
) : OrganizationHostnameClient {
  override suspend operator fun invoke(
    endpoint: OrganizationHostnameApi.Create,
  ): OrganizationHostnameRep =
    createOrganizationHostname.handle(endpoint)

  override suspend operator fun invoke(
    endpoint: OrganizationHostnameApi.Get,
  ): OrganizationHostnameRep? =
    getOrganizationHostname.handle(endpoint)

  override suspend operator fun invoke(
    endpoint: OrganizationHostnameApi.Delete,
  ): OrganizationHostnameRep =
    deleteOrganizationHostname.handle(endpoint)
}
