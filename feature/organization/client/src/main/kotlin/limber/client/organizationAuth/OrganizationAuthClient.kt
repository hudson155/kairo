package limber.client.organizationAuth

import limber.api.organizationAuth.OrganizationAuthApi
import limber.rep.organizationAuth.OrganizationAuthRep

public interface OrganizationAuthClient {
  public suspend operator fun invoke(
    endpoint: OrganizationAuthApi.GetByOrganization,
  ): OrganizationAuthRep?

  public suspend operator fun invoke(
    endpoint: OrganizationAuthApi.GetByHostname,
  ): OrganizationAuthRep?

  public suspend operator fun invoke(
    endpoint: OrganizationAuthApi.Set,
  ): OrganizationAuthRep

  public suspend operator fun invoke(
    endpoint: OrganizationAuthApi.DeleteByOrganization,
  ): OrganizationAuthRep
}
