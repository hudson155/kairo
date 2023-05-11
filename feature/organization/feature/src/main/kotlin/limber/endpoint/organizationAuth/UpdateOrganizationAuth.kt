package limber.endpoint.organizationAuth

import com.google.inject.Inject
import limber.auth.PlatformPermission
import limber.auth.PlatformPermissionAuth
import limber.auth.auth
import limber.feature.rest.RestEndpointHandler
import limber.mapper.organizationAuth.OrganizationAuthMapper
import limber.model.organizationAuth.OrganizationAuthModel
import limber.service.organizationAuth.OrganizationAuthService
import limber.util.updater.update
import limber.api.organizationAuth.OrganizationAuthApi as Api
import limber.rep.organizationAuth.OrganizationAuthRep as Rep

public class UpdateOrganizationAuth @Inject internal constructor(
  private val authMapper: OrganizationAuthMapper,
  private val authService: OrganizationAuthService,
) : RestEndpointHandler<Api.Update, Rep>(Api.Update::class) {
  override suspend fun handler(endpoint: Api.Update): Rep {
    auth(PlatformPermissionAuth(PlatformPermission.OrganizationAuthUpdate))

    val update = getBody(endpoint)
    val auth = authService.update(endpoint.authGuid) { existing ->
      OrganizationAuthModel.Update(
        auth0OrganizationId = existing.auth0OrganizationId,
        auth0OrganizationName = update(existing.auth0OrganizationName, update.auth0OrganizationName),
      )
    }

    return authMapper(auth)
  }
}
