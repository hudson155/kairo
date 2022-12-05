package limber.endpoint.organizationAuth

import com.google.inject.Inject
import limber.auth.OrganizationAuth
import limber.auth.OrganizationPermission
import limber.auth.auth
import limber.exception.organization.OrganizationDoesNotExist
import limber.feature.rest.RestEndpointHandler
import limber.service.organizationAuth.OrganizationAuthService
import limber.api.organizationAuth.OrganizationAuthApi as Api
import limber.rep.organizationAuth.OrganizationAuthRep as Rep

public class SetOrganizationAuth @Inject internal constructor(
  private val authService: OrganizationAuthService,
) : RestEndpointHandler<Api.Set, Rep>(Api.Set::class) {
  override suspend fun handler(endpoint: Api.Set): Rep {
    auth(
      auth = OrganizationAuth(
        organizationGuid = endpoint.organizationGuid,
        permission = OrganizationPermission.OrganizationAuthSet,
      ),
      onFail = { throw OrganizationDoesNotExist() },
    )

    return authService.set(endpoint.organizationGuid, getBody(endpoint))
  }
}
