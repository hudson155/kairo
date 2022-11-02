package limber.endpoint.organizationAuth

import com.google.inject.Inject
import limber.auth.Auth
import limber.auth.OrganizationAuth
import limber.auth.auth
import limber.exception.organizationAuth.OrganizationAuthDoesNotExist
import limber.feature.rest.RestEndpointHandler
import limber.service.organizationAuth.OrganizationAuthService
import limber.api.organizationAuth.OrganizationAuthApi as Api
import limber.rep.organizationAuth.OrganizationAuthRep as Rep

public class DeleteOrganizationAuthByOrganization @Inject internal constructor(
  private val authService: OrganizationAuthService,
) : RestEndpointHandler<Api.DeleteByOrganization, Rep>(Api.DeleteByOrganization::class) {
  override suspend fun handler(endpoint: Api.DeleteByOrganization): Rep {
    auth(Auth.Permission("organizationAuth:delete"))
    auth(OrganizationAuth(endpoint.organizationGuid)) { throw OrganizationAuthDoesNotExist() }
    return authService.deleteByOrganization(endpoint.organizationGuid)
  }
}
