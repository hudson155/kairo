package limber.endpoint

import com.google.inject.Inject
import limber.rest.RestEndpointHandler
import limber.service.OrganizationAuthService
import limber.api.OrganizationAuthApi as Api
import limber.rep.OrganizationAuthRep as Rep

public class SetOrganizationAuth @Inject internal constructor(
  private val authService: OrganizationAuthService,
) : RestEndpointHandler<Api.Set, Rep>(Api.Set::class) {
  override suspend fun handler(endpoint: Api.Set): Rep {
    return authService.set(endpoint.organizationGuid, endpoint.body)
  }
}
