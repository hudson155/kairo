package limber.endpoint.organization

import com.google.inject.Inject
import limber.auth.PlatformPermission
import limber.auth.PlatformPermissionAuth
import limber.auth.auth
import limber.feature.rest.RestEndpointHandler
import limber.mapper.organization.OrganizationMapper
import limber.service.organization.OrganizationService
import limber.api.organization.OrganizationApi as Api
import limber.rep.organization.OrganizationRep as Rep

public class GetAllOrganizations @Inject internal constructor(
  private val organizationMapper: OrganizationMapper,
  private val organizationService: OrganizationService,
) : RestEndpointHandler<Api.GetAll, List<Rep>>(Api.GetAll::class) {
  override suspend fun handler(endpoint: Api.GetAll): List<Rep> {
    auth(PlatformPermissionAuth(PlatformPermission.OrganizationList))

    val organizations = organizationService.getAll()
    return organizations.map { organizationMapper(it) }
  }
}
