package limber.endpoint

import com.google.inject.Inject
import limber.rest.RestEndpointHandler
import limber.service.OrganizationHostnameService
import limber.api.OrganizationHostnameApi as Api
import limber.rep.OrganizationHostnameRep as Rep

public class DeleteOrganizationHostname @Inject internal constructor(
  private val hostnameService: OrganizationHostnameService,
) : RestEndpointHandler<Api.Delete, Rep>(Api.Delete::class) {
  override suspend fun handler(endpoint: Api.Delete): Rep {
    return hostnameService.delete(endpoint.organizationGuid, endpoint.hostnameGuid)
  }
}
