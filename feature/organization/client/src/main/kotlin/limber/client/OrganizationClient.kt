package limber.client

import limber.api.OrganizationApi
import limber.rep.OrganizationRep

public interface OrganizationClient {
  public suspend operator fun invoke(
    endpoint: OrganizationApi.Create,
  ): OrganizationRep

  public suspend operator fun invoke(
    endpoint: OrganizationApi.Get,
  ): OrganizationRep?
}
