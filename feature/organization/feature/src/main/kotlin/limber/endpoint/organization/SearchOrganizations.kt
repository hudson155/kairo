package limber.endpoint.organization

import com.google.inject.Inject
import limber.auth.PlatformPermission
import limber.auth.PlatformPermissionAuth
import limber.auth.auth
import limber.feature.rest.RestEndpointHandler
import limber.mapper.organization.OrganizationMapper
import limber.service.organization.OrganizationInterface
import limber.api.organization.OrganizationApi as Api
import limber.rep.organization.OrganizationRep as Rep

public class SearchOrganizations @Inject internal constructor(
  private val organizationMapper: OrganizationMapper,
  private val organizationService: OrganizationInterface,
) : RestEndpointHandler<Api.Search, List<Rep>>(Api.Search::class) {
  override suspend fun handler(endpoint: Api.Search): List<Rep> {
    auth(PlatformPermissionAuth(PlatformPermission.Organization_List))

    val organizations = organizationService.search(endpoint.search)

    return organizations.map { organizationMapper(it) }
  }
}
