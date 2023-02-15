package limber.client.organizationAuth

import com.google.inject.Inject
import limber.api.organizationAuth.OrganizationAuthApi
import limber.endpoint.organizationAuth.CreateOrganizationAuth
import limber.endpoint.organizationAuth.DeleteOrganizationAuth
import limber.endpoint.organizationAuth.GetOrganizationAuth
import limber.endpoint.organizationAuth.GetOrganizationAuthByHostname
import limber.endpoint.organizationAuth.GetOrganizationAuthByOrganization
import limber.rep.organizationAuth.OrganizationAuthRep

public class LocalOrganizationAuthClient @Inject constructor(
  private val get: GetOrganizationAuth,
  private val getByOrganization: GetOrganizationAuthByOrganization,
  private val getByHostname: GetOrganizationAuthByHostname,
  private val create: CreateOrganizationAuth,
  private val delete: DeleteOrganizationAuth,
) : OrganizationAuthClient {
  override suspend operator fun invoke(
    endpoint: OrganizationAuthApi.Get,
  ): OrganizationAuthRep? =
    get.handle(endpoint)

  override suspend operator fun invoke(
    endpoint: OrganizationAuthApi.GetByOrganization,
  ): OrganizationAuthRep? =
    getByOrganization.handle(endpoint)

  override suspend operator fun invoke(
    endpoint: OrganizationAuthApi.GetByHostname,
  ): OrganizationAuthRep? =
    getByHostname.handle(endpoint)

  override suspend operator fun invoke(
    endpoint: OrganizationAuthApi.Create,
  ): OrganizationAuthRep =
    create.handle(endpoint)

  override suspend operator fun invoke(
    endpoint: OrganizationAuthApi.Delete,
  ): OrganizationAuthRep =
    delete.handle(endpoint)
}
