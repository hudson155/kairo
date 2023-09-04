package limber.endpoint.organizationAuth

import com.google.inject.Inject
import limber.auth.OrganizationAuth
import limber.auth.OrganizationPermission
import limber.auth.auth
import limber.exception.organizationAuth.OrganizationAuthDoesNotExist
import limber.feature.rest.RestEndpointHandler
import limber.mapper.organizationAuth.OrganizationAuthMapper
import limber.service.organizationAuth.OrganizationAuthService
import limber.api.organizationAuth.OrganizationAuthApi as Api
import limber.rep.organizationAuth.OrganizationAuthRep as Rep

public class DeleteOrganizationAuth @Inject internal constructor(
  private val authMapper: OrganizationAuthMapper,
  private val authService: OrganizationAuthService,
) : RestEndpointHandler<Api.Delete, Rep>(Api.Delete::class) {
  override suspend fun handler(endpoint: Api.Delete): Rep {
    auth {
      val auth = authService.get(endpoint.authId) ?: throw OrganizationAuthDoesNotExist()
      return@auth OrganizationAuth(OrganizationPermission.OrganizationAuth_Delete, auth.organizationId)
    }

    val auth = authService.delete(endpoint.authId)

    return authMapper(auth)
  }
}
