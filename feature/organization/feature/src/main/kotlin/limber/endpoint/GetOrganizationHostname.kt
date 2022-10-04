package limber.endpoint

import com.google.inject.Inject
import limber.rest.RestEndpointHandler
import limber.service.OrganizationHostnameService
import limber.api.OrganizationHostnameApi as Api
import limber.rep.OrganizationHostnameRep as Rep

public class GetOrganizationHostname @Inject internal constructor(
  private val hostnameService: OrganizationHostnameService,
) : RestEndpointHandler<Api.Get, Rep?>(Api.Get::class) {
  override suspend fun handler(endpoint: Api.Get): Rep? {
    return hostnameService.get(endpoint.organizationGuid, endpoint.hostnameGuid)
  }
}
