package limber.client

import limber.api.OrganizationAuthApi
import limber.rep.OrganizationAuthRep

public interface OrganizationAuthClient {
  public suspend operator fun invoke(
    endpoint: OrganizationAuthApi.Set,
  ): OrganizationAuthRep

  public suspend operator fun invoke(
    endpoint: OrganizationAuthApi.GetByOrganization,
  ): OrganizationAuthRep?
}
