package limber.endpoint

import com.google.inject.Inject
import limber.rest.RestEndpointHandler
import limber.service.OrganizationService
import limber.api.OrganizationApi as Api
import limber.rep.OrganizationRep as Rep

public class CreateOrganization @Inject constructor(
  private val organizationService: OrganizationService,
) : RestEndpointHandler<Api.Create, Rep>(Api.Create::class) {
  override suspend fun handler(endpoint: Api.Create): Rep {
    return organizationService.create(endpoint.body)
  }
}
