package limber.endpoint.organization

import com.google.inject.Inject
import limber.feature.rest.RestEndpointHandler
import limber.service.organization.OrganizationService
import limber.api.organization.OrganizationApi as Api
import limber.rep.organization.OrganizationRep as Rep

public class UpdateOrganization @Inject internal constructor(
  private val organizationService: OrganizationService,
) : RestEndpointHandler<Api.Update, Rep>(Api.Update::class) {
  override suspend fun handler(endpoint: Api.Update): Rep {
    return organizationService.update(endpoint.organizationGuid, endpoint.body)
  }
}
