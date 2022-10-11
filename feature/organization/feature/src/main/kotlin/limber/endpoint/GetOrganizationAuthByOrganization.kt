package limber.endpoint

import com.google.inject.Inject
import limber.rest.RestEndpointHandler
import limber.service.OrganizationAuthService
import limber.api.OrganizationAuthApi as Api
import limber.rep.OrganizationAuthRep as Rep

public class GetOrganizationAuthByOrganization @Inject internal constructor(
  private val authService: OrganizationAuthService,
) : RestEndpointHandler<Api.GetByOrganization, Rep?>(Api.GetByOrganization::class) {
  override suspend fun handler(endpoint: Api.GetByOrganization): Rep? {
    return authService.getByOrganization(endpoint.organizationGuid)
  }
}
