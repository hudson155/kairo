package limber.endpoint.organization

import com.google.inject.Inject
import limber.auth.Auth
import limber.auth.OrganizationAuth
import limber.auth.auth
import limber.feature.rest.RestEndpointHandler
import limber.service.organization.OrganizationService
import limber.api.organization.OrganizationApi as Api
import limber.rep.organization.OrganizationRep as Rep

public class GetOrganization @Inject internal constructor(
  private val organizationService: OrganizationService,
) : RestEndpointHandler<Api.Get, Rep?>(Api.Get::class) {
  override suspend fun handler(endpoint: Api.Get): Rep? {
    auth(Auth.Permission("organization:read"))
    auth(OrganizationAuth(endpoint.organizationGuid)) { return@handler null }
    return organizationService.get(endpoint.organizationGuid)
  }
}
