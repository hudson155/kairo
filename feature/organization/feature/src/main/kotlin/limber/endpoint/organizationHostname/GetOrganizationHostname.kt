package limber.endpoint.organizationHostname

import com.google.inject.Inject
import limber.auth.Auth
import limber.auth.OrganizationAuth
import limber.auth.auth
import limber.feature.rest.RestEndpointHandler
import limber.service.organizationHostname.OrganizationHostnameService
import limber.api.organizationHostname.OrganizationHostnameApi as Api
import limber.rep.organizationHostname.OrganizationHostnameRep as Rep

public class GetOrganizationHostname @Inject internal constructor(
  private val hostnameService: OrganizationHostnameService,
) : RestEndpointHandler<Api.Get, Rep?>(Api.Get::class) {
  override suspend fun handler(endpoint: Api.Get): Rep? {
    auth(Auth.Permission("organizationHostname:read"))
    auth(OrganizationAuth(endpoint.organizationGuid)) { return@handler null }
    return hostnameService.get(endpoint.organizationGuid, endpoint.hostnameGuid)
  }
}
