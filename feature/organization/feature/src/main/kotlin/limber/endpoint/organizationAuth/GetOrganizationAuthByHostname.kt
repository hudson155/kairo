package limber.endpoint.organizationAuth

import com.google.inject.Inject
import limber.auth.Auth
import limber.auth.auth
import limber.feature.rest.RestEndpointHandler
import limber.service.organizationAuth.OrganizationAuthService
import limber.api.organizationAuth.OrganizationAuthApi as Api
import limber.rep.organizationAuth.OrganizationAuthRep as Rep

public class GetOrganizationAuthByHostname @Inject internal constructor(
  private val authService: OrganizationAuthService,
) : RestEndpointHandler<Api.GetByHostname, Rep?>(Api.GetByHostname::class) {
  override suspend fun handler(endpoint: Api.GetByHostname): Rep? {
    auth(Auth.Public)

    return authService.getByHostname(endpoint.hostname)
  }
}
