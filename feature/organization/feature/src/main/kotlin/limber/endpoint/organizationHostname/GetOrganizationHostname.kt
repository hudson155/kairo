package limber.endpoint.organizationHostname

import com.google.inject.Inject
import limber.auth.OrganizationAuth
import limber.auth.OrganizationPermission
import limber.auth.auth
import limber.feature.rest.RestEndpointHandler
import limber.service.organizationHostname.OrganizationHostnameService
import limber.api.organizationHostname.OrganizationHostnameApi as Api
import limber.rep.organizationHostname.OrganizationHostnameRep as Rep

public class GetOrganizationHostname @Inject internal constructor(
  private val hostnameService: OrganizationHostnameService,
) : RestEndpointHandler<Api.Get, Rep?>(Api.Get::class) {
  override suspend fun handler(endpoint: Api.Get): Rep? {
    auth(
      auth = OrganizationAuth(
        organizationGuid = { hostnameService.get(endpoint.hostnameGuid)?.organizationGuid },
        permission = OrganizationPermission.OrganizationHostnameRead,
      ),
      onFail = { return@handler null },
    )

    return hostnameService.get(endpoint.hostnameGuid)
  }
}
