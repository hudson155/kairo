package limber.client.organizationAuth

import limber.api.organizationAuth.OrganizationAuthApi
import limber.rep.organizationAuth.OrganizationAuthRep

public interface OrganizationAuthClient {
  public suspend operator fun invoke(
    endpoint: OrganizationAuthApi.Get,
  ): OrganizationAuthRep?

  public suspend operator fun invoke(
    endpoint: OrganizationAuthApi.GetByOrganization,
  ): OrganizationAuthRep?

  public suspend operator fun invoke(
    endpoint: OrganizationAuthApi.GetByHostname,
  ): OrganizationAuthRep?

  public suspend operator fun invoke(
    endpoint: OrganizationAuthApi.Create,
  ): OrganizationAuthRep

  public suspend operator fun invoke(
    endpoint: OrganizationAuthApi.Update,
  ): OrganizationAuthRep

  public suspend operator fun invoke(
    endpoint: OrganizationAuthApi.Delete,
  ): OrganizationAuthRep
}
