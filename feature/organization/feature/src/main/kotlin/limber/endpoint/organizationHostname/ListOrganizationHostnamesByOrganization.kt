package limber.endpoint.organizationHostname

import com.google.inject.Inject
import limber.auth.OrganizationAuth
import limber.auth.OrganizationPermission
import limber.auth.auth
import limber.feature.rest.RestEndpointHandler
import limber.mapper.organizationHostname.OrganizationHostnameMapper
import limber.service.organizationHostname.OrganizationHostnameInterface
import limber.api.organizationHostname.OrganizationHostnameApi as Api
import limber.rep.organizationHostname.OrganizationHostnameRep as Rep

public class ListOrganizationHostnamesByOrganization @Inject internal constructor(
  private val hostnameMapper: OrganizationHostnameMapper,
  private val hostnameService: OrganizationHostnameInterface,
) : RestEndpointHandler<Api.ListByOrganization, List<Rep>>(Api.ListByOrganization::class) {
  override suspend fun handler(endpoint: Api.ListByOrganization): List<Rep> {
    auth { OrganizationAuth(OrganizationPermission.OrganizationHostname_List, endpoint.organizationId) }

    val hostnames = hostnameService.listByOrganization(endpoint.organizationId)

    return hostnames.map { hostnameMapper(it) }
  }
}
