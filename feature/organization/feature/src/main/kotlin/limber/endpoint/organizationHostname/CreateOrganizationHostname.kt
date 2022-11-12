package limber.endpoint.organizationHostname

import com.google.inject.Inject
import limber.auth.OrganizationAuth
import limber.auth.OrganizationPermission
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
    auth(
      auth = OrganizationAuth(
        organizationGuid = endpoint.organizationGuid,
        permission = OrganizationPermission.OrganizationHostnameCreate,
      ),
      onFail = { throw OrganizationDoesNotExist() },
    )

    return hostnameService.create(endpoint.organizationGuid, endpoint.body)
  }
}
