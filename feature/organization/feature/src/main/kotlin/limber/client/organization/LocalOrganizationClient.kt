package limber.client.organization

import com.google.inject.Inject
import limber.api.organization.OrganizationApi
import limber.endpoint.organization.CreateOrganization
import limber.endpoint.organization.GetOrganization
import limber.endpoint.organization.GetOrganizationByHostname
import limber.endpoint.organization.UpdateOrganization
import limber.rep.organization.OrganizationRep

public class LocalOrganizationClient @Inject constructor(
  private val create: CreateOrganization,
  private val get: GetOrganization,
  private val getByHostname: GetOrganizationByHostname,
  private val update: UpdateOrganization,
) : OrganizationClient {
  override suspend operator fun invoke(
    endpoint: OrganizationApi.Create,
  ): OrganizationRep =
    create.handle(endpoint)

  override suspend operator fun invoke(
    endpoint: OrganizationApi.Get,
  ): OrganizationRep? =
    get.handle(endpoint)

  override suspend operator fun invoke(
    endpoint: OrganizationApi.GetByHostname,
  ): OrganizationRep? =
    getByHostname.handle(endpoint)

  override suspend operator fun invoke(
    endpoint: OrganizationApi.Update,
  ): OrganizationRep =
    update.handle(endpoint)
}