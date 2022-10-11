package limber.client

import com.google.inject.Inject
import limber.api.OrganizationApi
import limber.endpoint.CreateOrganization
import limber.endpoint.GetOrganization
import limber.endpoint.GetOrganizationByHostname
import limber.endpoint.UpdateOrganization
import limber.rep.OrganizationRep

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
