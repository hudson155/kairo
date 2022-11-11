package limber.endpoint.organizationAuth

import com.google.inject.Inject
import limber.auth.OrganizationAuth
import limber.auth.PlatformPermission
import limber.auth.PlatformPermissionAuth
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
    auth(PlatformPermissionAuth(PlatformPermission.OrganizationAuthDelete))
    auth(OrganizationAuth(endpoint.organizationGuid)) { throw OrganizationAuthDoesNotExist() }
    return authService.deleteByOrganization(endpoint.organizationGuid)
  }
}
