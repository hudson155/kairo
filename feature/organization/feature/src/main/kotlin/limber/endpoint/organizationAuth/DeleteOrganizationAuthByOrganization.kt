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

public class DeleteOrganizationAuthByOrganization @Inject internal constructor(
  private val authMapper: OrganizationAuthMapper,
  private val authService: OrganizationAuthService,
) : RestEndpointHandler<Api.DeleteByOrganization, Rep>(Api.DeleteByOrganization::class) {
  override suspend fun handler(endpoint: Api.DeleteByOrganization): Rep {
    auth(PlatformPermissionAuth(PlatformPermission.OrganizationAuthDelete))

    val auth = authService.deleteByOrganization(endpoint.organizationGuid)
    return authMapper(auth)
  }
}
