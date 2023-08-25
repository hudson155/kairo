package limber.client.organization

import limber.api.organization.OrganizationApi
import limber.rep.organization.OrganizationRep

public interface OrganizationClient {
  public suspend operator fun invoke(
    endpoint: OrganizationApi.Get,
  ): OrganizationRep?

  public suspend operator fun invoke(
    endpoint: OrganizationApi.ListAll,
  ): List<OrganizationRep>

  public suspend operator fun invoke(
    endpoint: OrganizationApi.Search,
  ): List<OrganizationRep>

  public suspend operator fun invoke(
    endpoint: OrganizationApi.Create,
  ): OrganizationRep

  public suspend operator fun invoke(
    endpoint: OrganizationApi.Update,
  ): OrganizationRep

  public suspend operator fun invoke(
    endpoint: OrganizationApi.Delete,
  ): OrganizationRep
}
