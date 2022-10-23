package limber.endpoint.organization

import com.google.inject.Inject
import limber.feature.rest.RestEndpointHandler
import limber.service.organization.OrganizationService
import limber.api.organization.OrganizationApi as Api
import limber.rep.organization.OrganizationRep as Rep

public class GetOrganizationByHostname @Inject internal constructor(
  private val organizationService: OrganizationService,
) : RestEndpointHandler<Api.GetByHostname, Rep?>(Api.GetByHostname::class) {
  override suspend fun handler(endpoint: Api.GetByHostname): Rep? {
    return organizationService.getByHostname(endpoint.hostname)
  }
}
