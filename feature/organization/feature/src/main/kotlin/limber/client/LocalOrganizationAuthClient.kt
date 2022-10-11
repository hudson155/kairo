package limber.client

import com.google.inject.Inject
import limber.api.OrganizationAuthApi
import limber.endpoint.GetOrganizationAuthByOrganization
import limber.endpoint.SetOrganizationAuth
import limber.rep.OrganizationAuthRep

public class LocalOrganizationAuthClient @Inject constructor(
  private val set: SetOrganizationAuth,
  private val getByOrganization: GetOrganizationAuthByOrganization,
) : OrganizationAuthClient {
  override suspend operator fun invoke(
    endpoint: OrganizationAuthApi.Set,
  ): OrganizationAuthRep =
    set.handle(endpoint)

  override suspend operator fun invoke(
    endpoint: OrganizationAuthApi.GetByOrganization,
  ): OrganizationAuthRep? =
    getByOrganization.handle(endpoint)
}
