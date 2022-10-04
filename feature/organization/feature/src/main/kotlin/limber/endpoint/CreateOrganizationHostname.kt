package limber.endpoint

import com.google.inject.Inject
import limber.rest.RestEndpointHandler
import limber.service.OrganizationHostnameService
import limber.api.OrganizationHostnameApi as Api
import limber.rep.OrganizationHostnameRep as Rep

public class CreateOrganizationHostname @Inject internal constructor(
  private val hostnameService: OrganizationHostnameService,
) : RestEndpointHandler<Api.Create, Rep>(Api.Create::class) {
  override suspend fun handler(endpoint: Api.Create): Rep {
    return hostnameService.create(endpoint.organizationGuid, endpoint.body)
  }
}
