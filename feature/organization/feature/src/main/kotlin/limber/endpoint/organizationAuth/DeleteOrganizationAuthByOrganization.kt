package limber.endpoint.organizationAuth

import com.google.inject.Inject
import limber.feature.rest.RestEndpointHandler
import limber.service.organizationAuth.OrganizationAuthService
import limber.api.organizationAuth.OrganizationAuthApi as Api
import limber.rep.organizationAuth.OrganizationAuthRep as Rep

public class DeleteOrganizationAuthByOrganization @Inject internal constructor(
  private val authService: OrganizationAuthService,
) : RestEndpointHandler<Api.DeleteByOrganization, Rep>(Api.DeleteByOrganization::class) {
  override suspend fun handler(endpoint: Api.DeleteByOrganization): Rep {
    return authService.deleteByOrganization(endpoint.organizationGuid)
  }
}
