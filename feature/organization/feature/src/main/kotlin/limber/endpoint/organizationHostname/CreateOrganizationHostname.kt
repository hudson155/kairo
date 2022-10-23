package limber.endpoint.organizationHostname

import com.google.inject.Inject
import limber.feature.rest.RestEndpointHandler
import limber.service.organizationHostname.OrganizationHostnameService
import limber.api.organizationHostname.OrganizationHostnameApi as Api
import limber.rep.organizationHostname.OrganizationHostnameRep as Rep

public class CreateOrganizationHostname @Inject internal constructor(
  private val hostnameService: OrganizationHostnameService,
) : RestEndpointHandler<Api.Create, Rep>(Api.Create::class) {
  override suspend fun handler(endpoint: Api.Create): Rep {
    return hostnameService.create(endpoint.organizationGuid, endpoint.body)
  }
}
