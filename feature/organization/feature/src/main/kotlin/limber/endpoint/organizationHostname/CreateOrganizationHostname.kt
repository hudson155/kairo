package limber.endpoint.organizationHostname

import com.google.inject.Inject
import limber.auth.Auth
import limber.auth.OrganizationAuth
import limber.auth.auth
import limber.exception.organization.OrganizationDoesNotExist
import limber.feature.rest.RestEndpointHandler
import limber.service.organizationHostname.OrganizationHostnameService
import limber.api.organizationHostname.OrganizationHostnameApi as Api
import limber.rep.organizationHostname.OrganizationHostnameRep as Rep

public class CreateOrganizationHostname @Inject internal constructor(
  private val hostnameService: OrganizationHostnameService,
) : RestEndpointHandler<Api.Create, Rep>(Api.Create::class) {
  override suspend fun handler(endpoint: Api.Create): Rep {
    auth(Auth.Permission("organizationHostname:create"))
    auth(OrganizationAuth(endpoint.organizationGuid)) { throw OrganizationDoesNotExist() }
    return hostnameService.create(endpoint.organizationGuid, endpoint.body)
  }
}
