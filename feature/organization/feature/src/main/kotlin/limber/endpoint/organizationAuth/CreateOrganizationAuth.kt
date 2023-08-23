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

public class CreateOrganizationAuth @Inject internal constructor(
  private val authMapper: OrganizationAuthMapper,
  private val authService: OrganizationAuthService,
) : RestEndpointHandler<Api.Create, Rep>(Api.Create::class) {
  override suspend fun handler(endpoint: Api.Create): Rep {
    auth(PlatformPermissionAuth(PlatformPermission.OrganizationAuth_Create))

    val auth = authService.create(
      creator = authMapper(endpoint.organizationId, getBody(endpoint)),
    )

    return authMapper(auth)
  }
}
