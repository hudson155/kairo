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

public class GetOrganizationHostname @Inject internal constructor(
  private val hostnameMapper: OrganizationHostnameMapper,
  private val hostnameService: OrganizationHostnameInterface,
) : RestEndpointHandler<Api.Get, Rep?>(Api.Get::class) {
  override suspend fun handler(endpoint: Api.Get): Rep? {
    auth(
      auth = OrganizationAuth(OrganizationPermission.OrganizationHostname_Read) {
        hostnameService.get(endpoint.hostnameId)?.organizationId
      },
      onFail = { return@handler null },
    )

    val hostname = hostnameService.get(endpoint.hostnameId)

    return hostname?.let { hostnameMapper(it) }
  }
}
