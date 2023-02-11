package limber.client.organization

import com.google.inject.Inject
import limber.api.organization.OrganizationApi
import limber.endpoint.organization.CreateOrganization
import limber.endpoint.organization.GetOrganization
import limber.endpoint.organization.ListAllOrganizations
import limber.endpoint.organization.SearchOrganizations
import limber.endpoint.organization.UpdateOrganization
import limber.rep.organization.OrganizationRep

public class LocalOrganizationClient @Inject constructor(
  private val get: GetOrganization,
  private val getAll: ListAllOrganizations,
  private val search: SearchOrganizations,
  private val create: CreateOrganization,
  private val update: UpdateOrganization,
) : OrganizationClient {
  override suspend operator fun invoke(
    endpoint: OrganizationApi.Get,
  ): OrganizationRep? =
    get.handle(endpoint)

  override suspend operator fun invoke(
    endpoint: OrganizationApi.ListAll,
  ): List<OrganizationRep> =
    getAll.handle(endpoint)

  override suspend operator fun invoke(
    endpoint: OrganizationApi.Search,
  ): List<OrganizationRep> =
    search.handle(endpoint)

  override suspend operator fun invoke(
    endpoint: OrganizationApi.Create,
  ): OrganizationRep =
    create.handle(endpoint)

  override suspend operator fun invoke(
    endpoint: OrganizationApi.Update,
  ): OrganizationRep =
    update.handle(endpoint)
}
