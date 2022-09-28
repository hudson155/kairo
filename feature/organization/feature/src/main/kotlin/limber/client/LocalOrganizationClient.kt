package limber.client

import com.google.inject.Inject
import limber.api.OrganizationApi
import limber.endpoint.CreateOrganization
import limber.endpoint.GetOrganization
import limber.endpoint.UpdateOrganization
import limber.rep.OrganizationRep

public class LocalOrganizationClient @Inject constructor(
  private val createOrganization: CreateOrganization,
  private val getOrganization: GetOrganization,
  private val updateOrganization: UpdateOrganization,
) : OrganizationClient {
  override suspend operator fun invoke(
    endpoint: OrganizationApi.Create,
  ): OrganizationRep =
    createOrganization.handle(endpoint)

  override suspend operator fun invoke(
    endpoint: OrganizationApi.Get,
  ): OrganizationRep? =
    getOrganization.handle(endpoint)

  override suspend operator fun invoke(
    endpoint: OrganizationApi.Update,
  ): OrganizationRep =
    updateOrganization.handle(endpoint)
}
