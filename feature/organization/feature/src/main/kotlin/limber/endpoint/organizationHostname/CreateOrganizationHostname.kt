package limber.endpoint.organizationHostname

import com.google.inject.Inject
import limber.auth.PlatformPermission
import limber.auth.PlatformPermissionAuth
import limber.auth.auth
import limber.feature.rest.RestEndpointHandler
import limber.mapper.organizationHostname.OrganizationHostnameMapper
import limber.service.organizationHostname.OrganizationHostnameService
import limber.api.organizationHostname.OrganizationHostnameApi as Api
import limber.rep.organizationHostname.OrganizationHostnameRep as Rep

public class CreateOrganizationHostname @Inject internal constructor(
  private val hostnameMapper: OrganizationHostnameMapper,
  private val hostnameService: OrganizationHostnameService,
) : RestEndpointHandler<Api.Create, Rep>(Api.Create::class) {
  override suspend fun handler(endpoint: Api.Create): Rep {
    auth(PlatformPermissionAuth(PlatformPermission.OrganizationHostname_Create))

    val hostname = hostnameService.create(
      creator = hostnameMapper(endpoint.organizationId, getBody(endpoint)),
    )

    return hostnameMapper(hostname)
  }
}
