package limber.endpoint

import com.google.inject.Inject
import limber.rest.RestEndpointHandler
import limber.service.OrganizationService
import limber.api.OrganizationApi as Api
import limber.rep.OrganizationRep as Rep

public class GetOrganizationByHostname @Inject internal constructor(
  private val organizationService: OrganizationService,
) : RestEndpointHandler<Api.GetByHostname, Rep?>(Api.GetByHostname::class) {
  override suspend fun handler(endpoint: Api.GetByHostname): Rep? {
    return organizationService.getByHostname(endpoint.hostname)
  }
}
