package limber.endpoint.organization

import com.google.inject.Inject
import limber.auth.PlatformPermission
import limber.auth.PlatformPermissionAuth
import limber.auth.auth
import limber.feature.rest.RestEndpointHandler
import limber.service.organization.OrganizationService
import limber.api.organization.OrganizationApi as Api
import limber.rep.organization.OrganizationRep as Rep

public class CreateOrganization @Inject internal constructor(
  private val organizationService: OrganizationService,
) : RestEndpointHandler<Api.Create, Rep>(Api.Create::class) {
  override suspend fun handler(endpoint: Api.Create): Rep {
    auth(PlatformPermissionAuth(PlatformPermission.OrganizationCreate))

    return organizationService.create(endpoint.body)
  }
}
