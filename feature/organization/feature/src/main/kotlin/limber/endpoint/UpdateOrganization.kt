package limber.endpoint

import com.google.inject.Inject
import limber.rest.RestEndpointHandler
import limber.service.OrganizationService
import limber.api.OrganizationApi as Api
import limber.rep.OrganizationRep as Rep

public class UpdateOrganization @Inject internal constructor(
  private val organizationService: OrganizationService,
) : RestEndpointHandler<Api.Update, Rep>(Api.Update::class) {
  override suspend fun handler(endpoint: Api.Update): Rep {
    return organizationService.update(endpoint.organizationGuid, endpoint.body)
  }
}
