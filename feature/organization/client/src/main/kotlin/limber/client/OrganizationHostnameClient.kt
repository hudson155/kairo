package limber.client

import limber.api.OrganizationHostnameApi
import limber.rep.OrganizationHostnameRep

public interface OrganizationHostnameClient {
  public suspend operator fun invoke(
    endpoint: OrganizationHostnameApi.Create,
  ): OrganizationHostnameRep

  public suspend operator fun invoke(
    endpoint: OrganizationHostnameApi.Get,
  ): OrganizationHostnameRep?

  public suspend operator fun invoke(
    endpoint: OrganizationHostnameApi.Delete,
  ): OrganizationHostnameRep
}
