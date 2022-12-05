package limber.endpoint.organization

import com.google.inject.Inject
import limber.auth.OrganizationAuth
import limber.auth.OrganizationPermission
import limber.auth.auth
import limber.exception.organization.OrganizationDoesNotExist
import limber.feature.rest.RestEndpointHandler
import limber.service.organization.OrganizationService
import limber.api.organization.OrganizationApi as Api
import limber.rep.organization.OrganizationRep as Rep

public class UpdateOrganization @Inject internal constructor(
  private val organizationService: OrganizationService,
) : RestEndpointHandler<Api.Update, Rep>(Api.Update::class) {
  override suspend fun handler(endpoint: Api.Update): Rep {
    auth(
      auth = OrganizationAuth(
        organizationGuid = endpoint.organizationGuid,
        permission = OrganizationPermission.OrganizationUpdate,
      ),
      onFail = { throw OrganizationDoesNotExist() },
    )

    return organizationService.update(endpoint.organizationGuid, getBody(endpoint))
  }
}
