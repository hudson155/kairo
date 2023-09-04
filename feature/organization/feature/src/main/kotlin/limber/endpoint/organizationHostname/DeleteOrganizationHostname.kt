package limber.endpoint.organizationHostname

import com.google.inject.Inject
import limber.auth.OrganizationAuth
import limber.auth.OrganizationPermission
import limber.auth.auth
import limber.exception.organizationHostname.OrganizationHostnameDoesNotExist
import limber.feature.rest.RestEndpointHandler
import limber.mapper.organizationHostname.OrganizationHostnameMapper
import limber.service.organizationHostname.OrganizationHostnameInterface
import limber.api.organizationHostname.OrganizationHostnameApi as Api
import limber.rep.organizationHostname.OrganizationHostnameRep as Rep

public class DeleteOrganizationHostname @Inject internal constructor(
  private val hostnameMapper: OrganizationHostnameMapper,
  private val hostnameService: OrganizationHostnameInterface,
) : RestEndpointHandler<Api.Delete, Rep>(Api.Delete::class) {
  override suspend fun handler(endpoint: Api.Delete): Rep {
    auth {
      val hostname = hostnameService.get(endpoint.hostnameId) ?: throw OrganizationHostnameDoesNotExist()
      return@auth OrganizationAuth(OrganizationPermission.OrganizationHostname_Delete, hostname.organizationId)
    }

    val hostname = hostnameService.delete(endpoint.hostnameId)

    return hostnameMapper(hostname)
  }
}
