package limber.endpoint

import com.google.inject.Inject
import limber.rest.RestEndpointHandler
import limber.service.OrganizationService
import limber.api.OrganizationApi as Api
import limber.rep.OrganizationRep as Rep

public class GetOrganization @Inject constructor(
  private val organizationService: OrganizationService,
) : RestEndpointHandler<Api.Get, Rep?>(Api.Get::class) {
  override suspend fun handle(endpoint: Api.Get): Rep? {
    return organizationService.get(endpoint.organizationGuid)
  }
}
