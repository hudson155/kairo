package limber.endpoint.organizationAuth

import com.google.inject.Inject
import limber.auth.PlatformPermission
import limber.auth.PlatformPermissionAuth
import limber.auth.auth
import limber.feature.rest.RestEndpointHandler
import limber.mapper.organizationAuth.OrganizationAuthMapper
import limber.service.organizationAuth.OrganizationAuthService
import limber.api.organizationAuth.OrganizationAuthApi as Api
import limber.rep.organizationAuth.OrganizationAuthRep as Rep

public class SetOrganizationAuth @Inject internal constructor(
  private val authMapper: OrganizationAuthMapper,
  private val authService: OrganizationAuthService,
) : RestEndpointHandler<Api.Set, Rep>(Api.Set::class) {
  override suspend fun handler(endpoint: Api.Set): Rep {
    auth(PlatformPermissionAuth(PlatformPermission.OrganizationAuthSet))

    val auth = authService.set(endpoint.organizationGuid, getBody(endpoint))
    return authMapper(auth)
  }
}
