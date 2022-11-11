package limber.endpoint.organizationHostname

import com.google.inject.Inject
import limber.auth.OrganizationAuth
import limber.auth.PlatformPermission
import limber.auth.PlatformPermissionAuth
import limber.auth.auth
import limber.exception.organizationAuth.OrganizationAuthDoesNotExist
import limber.feature.rest.RestEndpointHandler
import limber.service.organizationHostname.OrganizationHostnameService
import limber.api.organizationHostname.OrganizationHostnameApi as Api
import limber.rep.organizationHostname.OrganizationHostnameRep as Rep

public class DeleteOrganizationHostname @Inject internal constructor(
  private val hostnameService: OrganizationHostnameService,
) : RestEndpointHandler<Api.Delete, Rep>(Api.Delete::class) {
  override suspend fun handler(endpoint: Api.Delete): Rep {
    auth(PlatformPermissionAuth(PlatformPermission.OrganizationHostnameDelete))
    auth(OrganizationAuth(endpoint.organizationGuid)) { throw OrganizationAuthDoesNotExist() }
    return hostnameService.delete(endpoint.organizationGuid, endpoint.hostnameGuid)
  }
}
