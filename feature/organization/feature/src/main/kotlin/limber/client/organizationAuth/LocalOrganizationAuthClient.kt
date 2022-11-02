package limber.client.organizationAuth

import com.google.inject.Inject
import limber.api.organizationAuth.OrganizationAuthApi
import limber.endpoint.organizationAuth.DeleteOrganizationAuthByOrganization
import limber.endpoint.organizationAuth.GetOrganizationAuthByHostname
import limber.endpoint.organizationAuth.GetOrganizationAuthByOrganization
import limber.endpoint.organizationAuth.SetOrganizationAuth
import limber.rep.organizationAuth.OrganizationAuthRep

public class LocalOrganizationAuthClient @Inject constructor(
  private val getByOrganization: GetOrganizationAuthByOrganization,
  private val getByHostname: GetOrganizationAuthByHostname,
  private val set: SetOrganizationAuth,
  private val deleteByOrganization: DeleteOrganizationAuthByOrganization,
) : OrganizationAuthClient {
  override suspend operator fun invoke(
    endpoint: OrganizationAuthApi.GetByOrganization,
  ): OrganizationAuthRep? =
    getByOrganization.handle(endpoint)

  override suspend operator fun invoke(
    endpoint: OrganizationAuthApi.GetByHostname,
  ): OrganizationAuthRep? =
    getByHostname.handle(endpoint)

  override suspend operator fun invoke(
    endpoint: OrganizationAuthApi.Set,
  ): OrganizationAuthRep =
    set.handle(endpoint)

  override suspend operator fun invoke(
    endpoint: OrganizationAuthApi.DeleteByOrganization,
  ): OrganizationAuthRep =
    deleteByOrganization.handle(endpoint)
}
