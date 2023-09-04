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

public class CreateOrganizationHostname @Inject internal constructor(
  private val hostnameMapper: OrganizationHostnameMapper,
  private val hostnameService: OrganizationHostnameInterface,
) : RestEndpointHandler<Api.Create, Rep>(Api.Create::class) {
  override suspend fun handler(endpoint: Api.Create): Rep {
    auth { OrganizationAuth(OrganizationPermission.OrganizationHostname_Create, endpoint.organizationId) }

    val hostname = hostnameService.create(
      creator = hostnameMapper(endpoint.organizationId, getBody(endpoint)),
    )

    return hostnameMapper(hostname)
  }
}
