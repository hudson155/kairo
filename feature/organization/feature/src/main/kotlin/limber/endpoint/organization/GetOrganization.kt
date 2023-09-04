package limber.endpoint.organization

import com.google.inject.Inject
import limber.auth.OrganizationAuth
import limber.auth.OrganizationPermission
import limber.auth.auth
import limber.feature.rest.RestEndpointHandler
import limber.mapper.organization.OrganizationMapper
import limber.service.organization.OrganizationInterface
import limber.api.organization.OrganizationApi as Api
import limber.rep.organization.OrganizationRep as Rep

public class GetOrganization @Inject internal constructor(
  private val organizationMapper: OrganizationMapper,
  private val organizationService: OrganizationInterface,
) : RestEndpointHandler<Api.Get, Rep?>(Api.Get::class) {
  override suspend fun handler(endpoint: Api.Get): Rep? {
    auth { OrganizationAuth(OrganizationPermission.Organization_Read, endpoint.organizationId) }

    val organization = organizationService.get(endpoint.organizationId)

    return organization?.let { organizationMapper(it) }
  }
}
