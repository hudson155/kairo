package limber.client.organizationHostname

import limber.api.organizationHostname.OrganizationHostnameApi
import limber.rep.organizationHostname.OrganizationHostnameRep

public interface OrganizationHostnameClient {
  public suspend operator fun invoke(
    endpoint: OrganizationHostnameApi.Get,
  ): OrganizationHostnameRep?

  public suspend operator fun invoke(
    endpoint: OrganizationHostnameApi.Create,
  ): OrganizationHostnameRep

  public suspend operator fun invoke(
    endpoint: OrganizationHostnameApi.Delete,
  ): OrganizationHostnameRep
}
